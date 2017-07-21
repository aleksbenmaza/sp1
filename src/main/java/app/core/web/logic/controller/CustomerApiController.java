package app.core.web.logic.controller;


import static app.core.business.model.mapping.IdentifiableById.NULL_ID;
import static app.core.business.model.mapping.IdentifiableById.Utils.toSortedList;
import static app.core.web.model.domaintransfer.Presentation.fromCollection;

import app.core.business.logic.ContractService;
import app.core.business.logic.CustomerService;
import app.core.business.logic.SinisterService;
import app.core.business.model.mapping.Contract;
import app.core.business.model.mapping.Coverage;
import app.core.business.model.mapping.person.insuree.Customer;
import app.core.business.model.mapping.sinister.Accident;
import app.core.business.model.mapping.sinister.PlainSinister;
import app.core.web.logic.controller.annotation.PreHandler;
import app.core.web.logic.exception.CustomHttpExceptions.*;
import app.core.web.logic.helper.VelocityTemplateResolver;
import app.core.web.model.databinding.command.ContractSubmission;
import app.core.web.model.databinding.validation.ContractSubmissionValidator;
import app.core.web.model.databinding.validation.Errors;
import app.core.web.model.databinding.command.sinister.SinisterSubmission;
import app.core.web.model.databinding.validation.SepaUploadValidator;
import app.core.web.model.databinding.validation.SinisterSubmissionValidator;
import app.core.web.model.domaintransfer.customer.*;
import app.core.web.model.domaintransfer.customer.sinister.AccidentDTO;
import app.core.web.model.domaintransfer.customer.sinister.PlainSinisterDTO;
import app.core.web.model.domaintransfer.customer.sinister.PlainSinisterDTO.TypeDTO;
import app.core.web.model.domaintransfer.customer.sinister.SinisterDTO;
import app.core.web.model.persistence.User;

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
@RequestMapping(value = "/customer")
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

    @PreHandler
    public void preHandle(
            @RequestHeader("Authorization") String token
    ) {
        User user;

        user = tokenService.getGrantedUser(token);

        if(user == null)
            throw new UnauthorizedRequestException();
        
        else if(!(user instanceof Customer))
            throw new ResourceForbiddenException();
    }

    @RequestMapping(value = "/granted", method = GET)
    public Boolean customerIsGranted(
            @RequestHeader("Authorization") String token
    ) {
        String sepa = ((Customer) tokenService.getGrantedUser(token)).getSepa();
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
            @RequestHeader("Authorization") String token
    ) {
        return new CustomerDTO((Customer) tokenService.getGrantedUser(token));
    }

    @RequestMapping(value = "/contracts", method = GET)
    public List<ContractDTO> getCustomerContracts(
            @RequestHeader("Authorization") String token
    ) {
        return fromCollection(contractService.getCustomerContracts((Customer)
                tokenService.getGrantedUser(token)
        ), ContractDTO::new);
    }

    @RequestMapping(value = "/contracts/{key}", method = GET)
    public ContractDTO getContract(
            @RequestHeader("Authorization") String token,
            @PathVariable                   int key
    ) {
        int i;

        i = 0;
        for(Contract contract : toSortedList(((Customer)tokenService.getGrantedUser(token)).getContracts())) {
            if(++i == key)
                return new ContractDTO(contract);
        }
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/contracts/{key}/sinisters", method = GET)
    public List<SinisterDTO> getSinisters(
            @RequestHeader("Authorization") String token,
            @PathVariable                   int    key
    ) {

        List<SinisterDTO> sinisters;
        int i;

        sinisters = new ArrayList<>();
        i = 0;
        for(Contract contract : toSortedList(((Customer)tokenService.getGrantedUser(token)).getContracts())) {
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
            @RequestHeader("Authorization") String token,
            @PathVariable                   int    contractKey,
            @PathVariable                   int    sinisterKey
    ) {
        List<Coverage> coverages;
        int i, y;

        coverages = new ArrayList<>();
        i = y = 0;

        for(Contract contract : toSortedList(((Customer)tokenService.getGrantedUser(token)).getContracts()))
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
            @RequestHeader("Authorization") String token
    ) {
        int i;
        i = 0;

        for(Contract contract : ((Customer)tokenService.getGrantedUser(token)).getContracts()) {
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
            @RequestHeader("Authorization") String token,
            @RequestBody                    ContractSubmission contractSubmission
    ) throws Exception {
        Errors errors;

        errors = new Errors(ContractSubmission.class);
        contractSubmissionValidator.validate(contractSubmission, errors);

        if(!errors.isEmpty())
            throw new CommandNotValidatedException(errors);

        contractService.addContract(contractSubmission,
                (Customer) tokenService.getGrantedUser(token)
        );
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/contracts/{key}/sinisters", method = POST)
    public void postSinister(
            @PathVariable                   int                key,
            @RequestHeader("Authorization") String             token,
            @RequestBody                    SinisterSubmission sinisterSubmission
    ) throws Exception { System.out.println("ready");
        Errors   errors;
        int      i;

        errors = new Errors(SinisterSubmission.class);
        sinisterSubmissionValidator.validate(sinisterSubmission, errors);

        if(!errors.isEmpty())
            throw new CommandNotValidatedException(errors);

        i = 0;

        for(Contract contract : ((Customer)tokenService.getGrantedUser(token)).getContracts())
            if(++i == key) {
                sinisterService.addSinister(sinisterSubmission, contract);
                return;
            }

        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/sepa", method = GET)
    public byte[] getSepa(
            @RequestHeader("Authorization") String token
    ) throws IOException, DocumentException {

        return customerService.generateSepa((Customer) tokenService.getGrantedUser(token));
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/sepa", method = POST)
    public void postSepa(
            @RequestHeader("Authorization") String token,
            @RequestBody                    byte[] sepa
    ) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        sepaUploadValidator.validate(sepa, errorMessage);
        if(errorMessage.length() != 0)
            throw new CommandNotValidatedException(errorMessage.toString());
        customerService.saveSepa(sepa, (Customer)tokenService.getGrantedUser(token));
    }

    private void checkWhiteSpaces(String string) {
        if(string.trim().isEmpty())
            throw new BadRequestException();
    }
}