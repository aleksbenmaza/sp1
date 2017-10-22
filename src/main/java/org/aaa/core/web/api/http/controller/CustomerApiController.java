package org.aaa.core.web.api.http.controller;


import static org.aaa.orm.entity.identifiable.IdentifiableById.NULL_ID;
import static org.aaa.orm.entity.identifiable.IdentifiableById.toSortedList;
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

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.http.HttpStatus.CREATED;

import org.aaa.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
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

    @ModelAttribute
    public Customer getCustomer(
            @RequestHeader("Authorization") String token
    ) {
        User user;

        user = tokenService.getGrantedUser(token);

        if(user == null)
            throw new UnauthorizedRequestException();

        else if(!(user instanceof Customer))
            throw new ResourceForbiddenException();

        return (Customer) user;
    }

    @ModelAttribute
    public void checkCustomer(
            Customer customer,
            @PathVariable long customerId
    ) {
        if(customer.getId() != customerId)
            throw new ResourceForbiddenException();
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

    @RequestMapping(value = "/customers/{customerId}/contracts", method = GET)
    public List<ContractDTO> getCustomerContracts(
            Customer customer
    ) {
        return fromCollection(
                contractService.getContracts(customer),
                ContractDTO::new
        );
    }

    @RequestMapping(value = "/customers/{customerId}/contracts/{contractId}", method = GET)
    public ContractDTO getContract(
            @PathVariable int      contractId,
                          Customer customer
    ) {
        return new ContractDTO(contractService.getContract(customer, contractId));
    }

    @RequestMapping(value = "/customers/{customerId}/contracts/{contractId}/sinisters", method = GET)
    public List<SinisterDTO> getSinisters(
            @PathVariable int      contractId,
                          Customer customer
    ) {

        List<SinisterDTO> sinisters;
        int i;

        sinisters = new ArrayList<>();

        sinisterService.getSinisters(customer, contractId).forEach(
                (sinister) ->  sinisters.add(
                        sinister instanceof Accident ?
                                new AccidentDTO((Accident) sinister) :
                                new PlainSinisterDTO((PlainSinister) sinister)
                )
        );
        return sinisters;
    }

    @RequestMapping(value = "/customers/{customerId}/sinisters/{sinisterId}", method = GET)
    public SinisterDTO getSinister(
            @PathVariable  int      sinisterId,
                           Customer customer
    ) {

        Sinister sinister;

        sinister = sinisterService.getSinister(customer, sinisterId);
        if(sinister == null)
            return null;

        return sinister instanceof Accident ?
                new AccidentDTO((Accident) sinister) :
                new PlainSinisterDTO((PlainSinister) sinister);

    }


    @ResponseStatus(CREATED)
    @RequestMapping(value = "/customers/{customerId}/contracts", method = POST)
    public void postContract(
            @RequestBody ContractSubmission contractSubmission,
                         Customer           customer
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
    @RequestMapping(value = "/customers/{customerId}/contracts/{contractId}/sinisters", method = POST)
    public long postSinister(
            @PathVariable  long               contractId,
            @RequestBody   SinisterSubmission sinisterSubmission,
                           Customer           customer
    ) throws Exception {
        Errors   errors;
        long id;

        errors = new Errors(SinisterSubmission.class);
        sinisterSubmissionValidator.validate(sinisterSubmission, errors);

        if(!errors.isEmpty())
            throw new CommandNotValidatedException(errors);


        if((id = sinisterService.addSinister(sinisterSubmission, customer, contractId)) == NULL_ID)
            throw new BadRequestException();

        return id;
    }

    @RequestMapping(value = "/customers/{customerId}?sepa", method = GET)
    public byte[] getSepa(
            Customer customer
    ) throws IOException, DocumentException {

        return customerService.generateSepa(customer);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/customers/{customerId}", method = PATCH)
    public void patchCustomer(
            @RequestBody byte[]   sepa,
                         Customer customer
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