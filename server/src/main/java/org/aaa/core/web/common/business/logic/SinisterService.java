package org.aaa.core.web.common.business.logic;

import static org.aaa.core.business.mapping.entity.ToBeChecked.Status.*;
import static org.aaa.orm.entity.identifiable.IdentifiableById.toSortedList;

import org.aaa.core.business.mapping.embeddable.Ownership;
import org.aaa.core.business.mapping.entity.*;
import org.aaa.orm.entry.manytoone.Entry;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.business.mapping.entity.person.insuree.Insuree;
import org.aaa.core.business.mapping.entity.person.insuree.ThirdParty;
import org.aaa.core.business.mapping.entity.sinister.PlainSinister;
import org.aaa.core.business.mapping.entity.sinister.Sinister;
import org.aaa.core.business.mapping.entity.sinister.accident.WithCustomerAccident;
import org.aaa.core.business.mapping.entity.sinister.accident.WithThirdPartyAccident;
import org.aaa.core.web.api.model.input.databinding.sinister.AccidentSubmission;
import org.aaa.core.web.api.model.input.databinding.sinister.PlainSinisterSubmission;
import org.aaa.core.web.api.model.input.databinding.sinister.SinisterSubmission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.net.URLConnection.guessContentTypeFromStream;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
@Service
public class SinisterService extends BaseService {

    @Autowired
    private ContractService contractService;

    @Value("${sinisterService.documentsDir.report}")
    private String reportDocumentDir;

    public List<Sinister> getByCustomerContract(Customer customer, long contractId) {
        return toSortedList(contractService.get(customer, contractId).getSinisters());
    }

    public Sinister getByCustomer(Customer customer, long sinisterId) {
        return dao.findSinister(customer, sinisterId);
    }

    @Transactional
    public Sinister create(
            SinisterSubmission submission,
            Customer           customer,
            long               contractId
    ) throws IOException {
        Contract contract;
        Sinister         sinister;
        WithCustomerAccident otherPartyAccident;
        Model otherPartyModel;
        Vehicle contractVehicle, otherPartyVehicle;
        PlainSinisterType type;
        byte[]           reportDocument;
        FileOutputStream outputStream;
        String           reportFileName;
        Entry<Insuree, Ownership> ownershipsInsuree;
        Ownership ownership;

        contract = dao.findContract(customer, contractId);

        if(contract == null)
            return null;

        reportDocument = submission.getReportDocument();

        contractVehicle = contract.getVehicle();

        if(submission instanceof AccidentSubmission) {

            otherPartyVehicle = dao.findVehicle(
                    ((AccidentSubmission) submission).getRegistrationNumber()
            );

            if(otherPartyVehicle == null) {
                otherPartyModel = dao.find(Model.class, ((AccidentSubmission) submission).getModelId());
                for(Year year : otherPartyModel.getYears())
                    if(year.getValue() == ((AccidentSubmission) submission).getYear()) {
                        ownership = new Ownership();
                        ownership.setRegistrationNumber(((AccidentSubmission) submission).getRegistrationNumber());
                        ownershipsInsuree = new Entry<>(new ThirdParty(((AccidentSubmission)submission).getNirNumber()));
                        ownershipsInsuree.setValue(ownership);
                        otherPartyVehicle = new Vehicle(((AccidentSubmission)submission).getVinNumber(), otherPartyModel, year, ownershipsInsuree);
                        break;
                    }

            }
            if(otherPartyVehicle != null)
                if(otherPartyVehicle.getCurrentContract() != null) {
                    sinister           = new WithCustomerAccident(submission.getDate(), submission.getTime(), contract);
                    otherPartyAccident = new WithCustomerAccident(submission.getDate(), submission.getTime(), otherPartyVehicle.getCurrentContract());
                    otherPartyAccident.setAccident((WithCustomerAccident)sinister);
                } else
                    sinister = new WithThirdPartyAccident(submission.getDate(), submission.getTime(), contract, otherPartyVehicle);
            else
                sinister = null;
        } else {
            type = dao.find(PlainSinisterType.class, ((PlainSinisterSubmission)submission).getTypeId());

            sinister = new PlainSinister(submission.getDate(), submission.getTime(), contract, type);
        }
        if(sinister != null ) {
            sinister.setComment(submission.getComment());
            sinister.setDate(submission.getDate());
            sinister.setTime(submission.getTime());
            sinister.setStatus(AWAITING);

            dao.save(sinister);
            reportFileName = reportDocumentDir +
                    sinister.getId() +
                    guessContentTypeFromStream(
                            new ByteArrayInputStream(reportDocument)
                    );

            outputStream = new FileOutputStream(reportFileName);
            outputStream.write(reportDocument);
            outputStream.close();
            return sinister;
        }
        return null;
    }
}