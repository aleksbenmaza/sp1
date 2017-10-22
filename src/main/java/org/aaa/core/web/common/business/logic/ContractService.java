package org.aaa.core.web.common.business.logic;

import static org.aaa.orm.entity.identifiable.IdentifiableById.toSortedList;
import static java.net.URLConnection.guessContentTypeFromStream;

import org.aaa.core.business.mapping.*;
import org.aaa.core.business.mapping.person.insuree.Customer;
import org.aaa.core.business.mapping.person.insuree.Insuree;
import org.aaa.core.web.api.model.input.databinding.ContractSubmission;
import org.aaa.orm.entry.manytoone.Entry;
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


    public List<Contract> getContracts(Customer customer) {
        return toSortedList(customer.getContracts());
    }

    public Contract getContract(Customer customer, long contractId) {
        return dao.findContract(customer, contractId);
    }

    @Transactional
    public void addContract(
            ContractSubmission contractSubmission,
            Customer           customer
    ) throws IOException {
        Insurance insurance;
        Model model;
        Vehicle vehicle;
        Contract contract;
        byte[] registrationDocument;
        FileOutputStream outputStream;
        String registrationFileName;
        Ownership ownership;
        Entry<Insuree, Ownership> ownershipsInsuree;

        insurance = dao.find(Insurance.class, contractSubmission.getInsuranceId());
        model     = dao.find(Model.class, contractSubmission.getModelId());

        for(Year year : model.getYears())
            if(year.getValue() == contractSubmission.getYear()) {
                ownership = new Ownership();
                ownership.setRegistrationNumber(contractSubmission.getRegistrationNumber());
                ownership.setPurchaseDate(contractSubmission.getPurchaseDate());

                ownershipsInsuree = new Entry<>(customer);
                ownershipsInsuree.setValue(ownership);

                vehicle = new Vehicle(model, year, ownershipsInsuree);

                vehicle.setVinNumber(contractSubmission.getVinNumber());

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
                return;
            }
    }

    public String getContractDocumentDir() {
        return contractDocumentDir;
    }
}