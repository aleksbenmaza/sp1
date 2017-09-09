package org.aaa.core.web.api.http.controller;


import static org.aaa.core.business.mapping.IdentifiableById.NULL_ID;
import static org.aaa.core.business.mapping.IdentifiableById.Utils.toSortedList;
import static org.aaa.core.web.api.model.ouput.DTO.fromCollection;

import org.aaa.core.web.common.business.logic.ContractService;
import org.aaa.core.web.common.business.logic.CustomerService;
import org.aaa.core.web.common.business.logic.SinisterService;
import org.aaa.core.business.mapping.Contract;
import org.aaa.core.business.mapping.person.insuree.Customer;
import org.aaa.core.business.mapping.sinister.accident.Accident;
import org.aaa.core.business.mapping.sinister.PlainSinister;
import org.aaa.core.business.mapping.sinister.Sinister;
import org.aaa.core.web.common.business.logic.VehicleService;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions.*;
import org.aaa.core.web.common.helper.VelocityTemplateResolver;
import org.aaa.core.web.api.model.input.databinding.ContractSubmission;
import org.aaa.core.web.api.model.input.validation.ContractSubmissionValidator;
import org.aaa.core.web.api.model.input.validation.Errors;
import org.aaa.core.web.api.model.input.databinding.sinister.SinisterSubmission;
import org.aaa.core.web.api.model.input.validation.SepaUploadValidator;
import org.aaa.core.web.api.model.input.validation.SinisterSubmissionValidator;
import org.aaa.core.web.api.model.ouput.customer.*;
import org.aaa.core.web.api.model.ouput.customer.sinister.AccidentDTO;
import org.aaa.core.web.api.model.ouput.customer.sinister.PlainSinisterDTO;
import org.aaa.core.web.api.model.ouput.customer.sinister.PlainSinisterDTO.TypeDTO;
import org.aaa.core.web.api.model.ouput.customer.sinister.SinisterDTO;
import org.aaa.core.business.mapping.User;

import com.itextpdf.text.DocumentException;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@RestController // = @Controller + @ResponseBody
@RequestMapping(value = "/customer")
public class CustomerApiController extends BaseController {

    @Autowired
    private CustomerService             customerService;

    @Autowired
    private ContractService             contractService;

    @Autowired
    private SinisterService             sinisterService;

    @Autowired
    private VehicleService              vehicleService;

    @Autowired
    private ContractSubmissionValidator contractSubmissionValidator;

    @Autowired
    private SinisterSubmissionValidator sinisterSubmissionValidator;

    @Autowired
    private SepaUploadValidator         sepaUploadValidator;

    @Autowired
    private VelocityTemplateResolver    velocityTemplateResolver;


    @ModelAttribute
    public Customer getCustomer(@RequestHeader("Authorization") String token) {
        User user;

        user = tokenService.getGrantedUser(token);

        if(user == null)
            throw new UnauthorizedRequestException();

        else if(!(user instanceof Customer))
            throw new ResourceForbiddenException();

        return (Customer) user;
    }

    @RequestMapping(value = "/granted", method = GET)
    public Boolean customerIsGranted(
            Customer customer
    ) {
        return customer.isSepaDocumentPresent();
    }

    @RequestMapping(value = "/makes", method = GET, params = "name")
    public List<MakeDTO> getMakes(
            @RequestParam String name
    ) {
        checkWhiteSpaces(name);
        return fromCollection(vehicleService.getMakesByName(name), MakeDTO::new);
    }

    @RequestMapping(value = "makes/{id}/models", method = GET, params = "name")
    public List<ModelDTO> getModels(
            @PathVariable long   id,
            @RequestParam String name
            
    ) {
        checkWhiteSpaces(name);
        return fromCollection(vehicleService.getModelsByNameAndMakeId(name, id), ModelDTO::new);
    }

    @RequestMapping(value = "/models", method = GET, params = "name")
    public List<ModelDTO> getModels(
            @RequestParam String name
    ) {
        checkWhiteSpaces(name);
        return getModels(NULL_ID, name);
    }

    @RequestMapping(method = GET)
    public CustomerDTO getCustomer(
            Customer customer
    ) {
        return new CustomerDTO(customer);
    }

    @RequestMapping(value = "/contracts", method = GET)
    public List<ContractDTO> getCustomerContracts(
            Customer customer
    ) {
        return fromCollection(
                contractService.getCustomerContracts(customer),
                ContractDTO::new
        );
    }

    @RequestMapping(value = "/contracts/{key}", method = GET)
    public ContractDTO getContract(
            Customer customer,
            @PathVariable                   int key
    ) {
        int i;

        i = 0;
        for(Contract contract : toSortedList(customer.getContracts())) {
            if(++i == key)
                return new ContractDTO(contract);
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/contracts/{key}/sinisters", method = GET)
    public List<SinisterDTO> getSinisters(
            Customer customer,
            @PathVariable                   int    key
    ) {

        List<SinisterDTO> sinisters;
        int i;

        sinisters = new ArrayList<>();
        i = 0;
        for(Contract contract : toSortedList(customer.getContracts())) {
            if(++i == key) {
                contract.getSinisters().forEach(
                        (sinister) ->  sinisters.add(
                                sinister instanceof Accident ?
                                        new AccidentDTO((Accident) sinister) :
                                        new PlainSinisterDTO((PlainSinister) sinister)
                        )
                );
                return sinisters;
            }
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/contracts/{contractKey}/sinisters/{sinisterKey}", method = GET)
    public SinisterDTO getSinister(
            Customer customer,
            @PathVariable                   int    contractKey,
            @PathVariable                   int    sinisterKey
    ) {

        int i, y;

        i = y = 0;

        for(Contract contract : toSortedList(customer.getContracts()))
            if(++i == contractKey) {

                for(Sinister sinister : toSortedList(contract.getSinisters()))
                    if(++y == sinisterKey)
                        return sinister instanceof Accident ?
                                new AccidentDTO((Accident) sinister) :
                                new PlainSinisterDTO((PlainSinister) sinister);
            }

        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/contracts/{key}/sinisters/types", method = GET)
    public List<TypeDTO> getTypes(
            @PathVariable                   int    key,
            Customer customer
    ) {
        int i;
        i = 0;

        for(Contract contract : customer.getContracts()) {
            if(++i == key)
                return fromCollection(/*contract.getInsurance().getSinisterTypes()*/null, TypeDTO::new);
        }

        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/template/{templateName}.{extension}", method = GET)
    public String getTemplate(
            @PathVariable String templateName,
            @PathVariable String extension
    ) {
        String template;
        template = velocityTemplateResolver.getCustomerpanelTemplate(templateName+"."+extension);
        if(template == null)
            throw new ResourceNotFoundException();
        return template;
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/contracts", method = POST)
    public void postContract(
            Customer customer,
            @RequestBody                    ContractSubmission contractSubmission
    ) throws Exception {
        Errors errors;

        errors = new Errors(ContractSubmission.class);
        contractSubmissionValidator.validate(contractSubmission, errors);

        if(!errors.isEmpty())
            throw new CommandNotValidatedException(errors);

        contractService.addContract(contractSubmission,
                customer
        );
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/contracts/{key}/sinisters", method = POST)
    public void postSinister(
            @PathVariable                   int                key,
            Customer customer,
            @RequestBody                    SinisterSubmission sinisterSubmission
    ) throws Exception { System.out.println("ready");
        Errors   errors;
        int      i;

        errors = new Errors(SinisterSubmission.class);
        sinisterSubmissionValidator.validate(sinisterSubmission, errors);

        if(!errors.isEmpty())
            throw new CommandNotValidatedException(errors);

        i = 0;

        for(Contract contract : customer.getContracts())
            if(++i == key) {
                sinisterService.addSinister(sinisterSubmission, contract);
                return;
            }

        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/sepa", method = GET)
    public byte[] getSepa(
            Customer customer
    ) throws IOException, DocumentException {

        return customerService.generateSepa(customer);
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/sepa", method = POST)
    public void postSepa(
            Customer customer,
            @RequestBody                    byte[] sepa
    ) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        sepaUploadValidator.validate(sepa, errorMessage);
        if(errorMessage.length() != 0)
            throw new CommandNotValidatedException(errorMessage.toString());
        customerService.saveSepa(sepa, customer);
    }

    private void checkWhiteSpaces(String string) {
        if(string.trim().isEmpty())
            throw new BadRequestException();
    }
}