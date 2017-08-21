package org.aaa.core.web.common.business.logic;

import static org.aaa.core.business.mapping.IdentifiableById.Utils.toSortedList;
import static java.net.URLConnection.guessContentTypeFromStream;

import org.aaa.core.business.mapping.*;
import org.aaa.core.business.mapping.person.insuree.Customer;
import org.aaa.core.web.api.model.input.databinding.ContractSubmission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by alexandremasanes on 04/04/2017.
 */
@Service
public class ContractService extends BaseService {

    @Value("${contractService.documentsDir.contract}")
    private String contractDocumentDir;


    @SuppressWarnings("unchecked")
    public List<Contract> getCustomerContracts(Customer customer) {
        return (List<Contract>) toSortedList(customer.getContracts());
    }

    @Transactional
    public synchronized void addContract(ContractSubmission contractSubmission, Customer customer) throws IOException {
        Insurance insurance;
        Model model;
        Vehicle vehicle;
        Contract contract;
        byte[] registrationDocument;
        FileOutputStream outputStream;
        String registrationFileName;

        insurance = (Insurance) dao.find(Insurance.class, contractSubmission.getInsuranceId());
        model     = (Model) dao.find(Model.class, contractSubmission.getModelId());

        vehicle = new Vehicle(model, customer);
        vehicle.setVinNumber(contractSubmission.getVinNumber());
        vehicle.setRegistrationNumber(contractSubmission.getRegistrationNumber());
        vehicle.setPurchaseDate(contractSubmission.getPurchaseDate());

        registrationDocument = contractSubmission.getRegistrationDocument();
        registrationFileName = contractDocumentDir +
                               dao.getNextId(Vehicle.class) +
                               guessContentTypeFromStream(
                                       new ByteArrayInputStream(registrationDocument)
                               );



        outputStream = new FileOutputStream(registrationFileName);
        outputStream.write(registrationDocument);
        outputStream.close();

        contract = new Contract(insurance, vehicle, customer);
        contract.setActive(true);
        contract.setSubscriptionDate(new Date());
        contract.setStatus(ToBeChecked.Status.AWAITING);

        dao.save(contract);
    }

    public String getContractDocumentDir() {
        return contractDocumentDir;
    }
}