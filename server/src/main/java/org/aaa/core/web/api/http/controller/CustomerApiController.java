package org.aaa.core.web.api.http.controller;

import static org.aaa.orm.entity.identifiable.IdentifiableById.NULL_ID;
import static org.aaa.core.web.api.model.ouput.DTO.fromCollection;

import org.aaa.core.business.mapping.entity.Contract;
import org.aaa.core.web.common.business.logic.ContractService;
import org.aaa.core.web.common.business.logic.CustomerService;
import org.aaa.core.web.common.business.logic.SinisterService;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.business.mapping.entity.sinister.accident.Accident;
import org.aaa.core.business.mapping.entity.sinister.PlainSinister;
import org.aaa.core.business.mapping.entity.sinister.Sinister;
import org.aaa.core.web.common.business.logic.vehicle.VehicleService;
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
import org.aaa.core.web.api.model.ouput.customer.sinister.SinisterDTO;
import org.aaa.core.business.mapping.entity.person.User;

import com.itextpdf.text.DocumentException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@RestController
@RequestMapping(value = "${routes.customer.root}")
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
    public Customer customer(
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
    public void checkResourceAccess(
                          Customer customer,
            @PathVariable long     customerId
    ) {
        if(customer.getId() != customerId)
            throw new ResourceForbiddenException();
    }

    @ModelAttribute
    public void checkWhiteSpace(
            @RequestParam String name
    ) {
        checkWhiteSpaces(name);
    }

    @RequestMapping(path = "${routes.customer.makes}", method = GET)
    public List<MakeDTO> getMakes(
            @RequestParam String name
    ) {
        return fromCollection(vehicleService.getMakesByName(name), MakeDTO::new);
    }

    @RequestMapping(path = "${routes.customer.makeModels}", method = GET)
    public List<ModelDTO> getModels(
            @PathVariable long   id,
            @RequestParam String name
            
    ) {
        System.out.println(vehicleService.getModelsByNameAndMakeId(name, id));
        return fromCollection(vehicleService.getModelsByNameAndMakeId(name, id), ModelDTO::new);
    }

    @RequestMapping(path = "${routes.customer.models}", method = GET)
    public List<ModelDTO> getModels(
            @RequestParam String name
    ) {
        return getModels(NULL_ID, name);
    }

    @RequestMapping(path = "${routes.customer.customer}", method = GET)
    public CustomerDTO getCustomer(
            Customer customer
    ) {
        return new CustomerDTO(customer);
    }

    @RequestMapping(path = "${routes.customer.customerContracts}", method = GET)
    public List<ContractDTO> getCustomerContracts(
            Customer customer
    ) {
        return fromCollection(
                contractService.get(customer),
                ContractDTO::new
        );
    }

    @RequestMapping(path = "${routes.customer.customerContract}", method = GET)
    public ContractDTO getContract(
            @PathVariable int      contractId,
                          Customer customer
    ) {
        return new ContractDTO(contractService.get(customer, contractId));
    }

    @RequestMapping(path = "${routes.customer.customerContractSinisters}", method = GET)
    public List<SinisterDTO> getSinisters(
            @PathVariable int      contractId,
                          Customer customer
    ) {

        List<SinisterDTO> sinisters;

        sinisters = new ArrayList<>();

        sinisterService.getByCustomerContract(customer, contractId).forEach(
                (sinister) ->  sinisters.add(
                        sinister instanceof Accident ?
                                new AccidentDTO((Accident) sinister) :
                                new PlainSinisterDTO((PlainSinister) sinister)
                )
        );
        return sinisters;
    }

    @RequestMapping(path = "${routes.customer.customerContractSinister}", method = GET)
    public SinisterDTO getSinister(
            @PathVariable  int      sinisterId,
                           Customer customer
    ) {
        Sinister sinister;

        sinister = sinisterService.getByCustomer(customer, sinisterId);
        if(sinister == null)
            return null;

        return sinister instanceof Accident ?
                new AccidentDTO((Accident) sinister) :
                new PlainSinisterDTO((PlainSinister) sinister);

    }

    @ResponseStatus(CREATED)
    @RequestMapping(path = "${routes.customer.customerContracts}", method = POST)
    public void postContract(
            @RequestBody ContractSubmission  contractSubmission,
                         Customer            customer,
                         HttpServletResponse response
    ) throws Exception {
        Errors errors;
        Contract contract;

        errors = new Errors(ContractSubmission.class);
        contractSubmissionValidator.validate(contractSubmission, errors);

        if(!errors.isEmpty())
            throw new CommandNotValidatedException(errors);

        contract = contractService.create(contractSubmission,
                customer
        );

        if(contract == null)
            throw new BadRequestException();

        setLocationResponseHeader(response, contract.getId());
    }

    @ResponseStatus(CREATED)
    @RequestMapping(path = "${routes.customer.customerContractSinisters}", method = POST)
    public void postSinister(
            @PathVariable  long                contractId,
            @RequestBody   SinisterSubmission  sinisterSubmission,
                           Customer            customer,
                           HttpServletResponse response
    ) throws Exception {
        Errors   errors;
        Sinister sinister;

        errors = new Errors(SinisterSubmission.class);
        sinisterSubmissionValidator.validate(sinisterSubmission, errors);

        if(!errors.isEmpty())
            throw new CommandNotValidatedException(errors);

        if((sinister = sinisterService.create(sinisterSubmission, customer, contractId)) == null)
            throw new BadRequestException();

        setLocationResponseHeader(response, sinister.getId());
    }

    @RequestMapping(path = "${routes.customer.customer}", method = GET, params = "sepa")
    public byte[] getSepa(
            Customer customer
    ) throws IOException, DocumentException {

        return customerService.generateSepa(customer);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(path = "${routes.customer.customer}", method = PATCH)
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