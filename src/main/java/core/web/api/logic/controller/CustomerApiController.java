package core.web.logic.controller;


import static core.business.model.mapping.IdentifiableById.NULL_ID;
import static core.business.model.mapping.IdentifiableById.Utils.toSortedList;
import static core.web.model.domaintransfer.Presentation.fromCollection;

import core.business.logic.ContractService;
import core.business.logic.CustomerService;
import core.business.logic.SinisterService;
import core.business.model.mapping.Contract;
import core.business.model.mapping.Coverage;
import core.business.model.mapping.person.insuree.Customer;
import core.business.model.mapping.sinister.Accident;
import core.business.model.mapping.sinister.PlainSinister;
import core.web.logic.exception.CustomHttpExceptions.*;
import core.web.logic.helper.VelocityTemplateResolver;
import core.web.model.databinding.command.ContractSubmission;
import core.web.model.databinding.validation.ContractSubmissionValidator;
import core.web.model.databinding.validation.Errors;
import core.web.model.databinding.command.sinister.SinisterSubmission;
import core.web.model.databinding.validation.SepaUploadValidator;
import core.web.model.databinding.validation.SinisterSubmissionValidator;
import core.web.model.domaintransfer.customer.*;
import core.web.model.domaintransfer.customer.sinister.AccidentDTO;
import core.web.model.domaintransfer.customer.sinister.PlainSinisterDTO;
import core.web.model.domaintransfer.customer.sinister.PlainSinisterDTO.TypeDTO;
import core.web.model.domaintransfer.customer.sinister.SinisterDTO;
import core.web.model.persistence.User;

import com.itextpdf.text.DocumentException;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static java.lang.Long.compare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@RestController // = @Controller + @ResponseBody
@RequestMapping(value = "/api/customer")
public class CustomerApiController extends ApiController {

    @Autowired
    private CustomerService             customerService;

    @Autowired
    private ContractService             contractService;

    @Autowired
    private SinisterService             sinisterService;

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
        String sepa = customer.getSepa();
        return !(sepa == null || sepa.isEmpty());
    }

    @RequestMapping(value = "/makes/{name}", method = GET)
    public List<MakeDTO> getMakes(
            @PathVariable String name
    ) {
        checkWhiteSpaces(name);
        return fromCollection(apiService.getMakesByName(name), MakeDTO::new);
    }

    @RequestMapping(value = "makes/{id}/models/{name}", method = GET)
    public List<ModelDTO> getModels(
            @PathVariable long   id,
            @PathVariable String name
            
    ) {
        checkWhiteSpaces(name);
        return fromCollection(apiService.getModelsByNameAndMakeId(name, id), ModelDTO::new);
    }

    @RequestMapping(value = "/models/{name}", method = GET)
    public List<ModelDTO> getModels(
            @PathVariable String name
    ) {
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
                contract.getCoverages().forEach(
                        coverage ->  sinisters.add(
                                coverage.getSinister() instanceof Accident ?
                                        new AccidentDTO((Accident) coverage.getSinister()) :
                                        new PlainSinisterDTO((PlainSinister) coverage.getSinister())
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
        List<Coverage> coverages;
        int i, y;

        coverages = new ArrayList<>();
        i = y = 0;

        for(Contract contract : toSortedList(customer.getContracts()))
            if(++i == contractKey) {
                coverages.addAll(contract.getCoverages());
                coverages.sort((c1, c2) -> compare(c1.getSinister().getId(), c2.getSinister().getId()));
                for(Coverage coverage : coverages)
                    if(++y == sinisterKey)
                        return coverage.getSinister() instanceof Accident ?
                                new AccidentDTO((Accident)coverage.getSinister()) :
                                new PlainSinisterDTO((PlainSinister)coverage.getSinister());
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
                return fromCollection(contract.getInsurance().getSinisterTypes(), TypeDTO::new);
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