package org.aaa.core.web.common.business.logic;

import static org.aaa.core.business.mapping.ToBeChecked.Status.*;

import org.aaa.core.business.mapping.*;
import org.aaa.core.business.mapping.person.insuree.Insuree;
import org.aaa.core.business.mapping.person.insuree.ThirdParty;
import org.aaa.core.business.mapping.sinister.PlainSinister;
import org.aaa.core.business.mapping.sinister.PlainSinister.Type;
import org.aaa.core.business.mapping.sinister.Sinister;
import org.aaa.core.business.mapping.sinister.accident.WithCustomerAccident;
import org.aaa.core.business.mapping.sinister.accident.WithThirdPartyAccident;
import org.aaa.core.web.api.model.input.databinding.sinister.AccidentSubmission;
import org.aaa.core.web.api.model.input.databinding.sinister.PlainSinisterSubmission;
import org.aaa.core.web.api.model.input.databinding.sinister.SinisterSubmission;

import org.aaa.orm.entry.manytoone.Entry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.net.URLConnection.guessContentTypeFromStream;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
@Service
public class SinisterService extends BaseService {

    @Value("${sinisterService.documentsDir.report}")
    private String reportDocumentDir;

    public synchronized void addSinister(
            SinisterSubmission submission,
            Contract           contract
    ) throws IOException {
        Sinister         sinister;
        WithCustomerAccident otherPartyAccident;
        Model            otherPartyModel;
        Vehicle          contractVehicle, otherPartyVehicle;
        Type             type;
        byte[]           reportDocument;
        FileOutputStream outputStream;
        String           reportFileName;
        Entry<Insuree, Ownership> ownershipsInsuree;
        Ownership ownership;

        reportDocument = submission.getReportDocument();
        reportFileName = reportDocumentDir +
                dao.getNextId(Sinister.class) +
                guessContentTypeFromStream(
                        new ByteArrayInputStream(reportDocument)
                );

        outputStream = new FileOutputStream(reportFileName);
        outputStream.write(reportDocument);
        outputStream.close();

        contractVehicle = contract.getVehicle();

        if(submission instanceof AccidentSubmission) {

            otherPartyVehicle = dao.findVehicle(
                    ((AccidentSubmission) submission).getRegistrationNumber()
            );

            if(otherPartyVehicle == null) {
                otherPartyModel = dao.find(Model.class, ((AccidentSubmission) submission).getModelId());
                for(ModelAndYear modelAndYear : otherPartyModel.getModelsAndYears())
                    if(modelAndYear.getYear() == ((AccidentSubmission) submission).getYear()) {
                        ownership = new Ownership();
                        ownership.setRegistrationNumber(((AccidentSubmission) submission).getRegistrationNumber());
                        ownershipsInsuree = new Entry<>(new ThirdParty());
                        ownershipsInsuree.setValue(ownership);
                        otherPartyVehicle = new Vehicle(modelAndYear, ownershipsInsuree);
                        break;
                    }

            }
            if(otherPartyVehicle != null)
                if(otherPartyVehicle.getCurrentContract() != null) {
                    sinister           = new WithCustomerAccident(contract);
                    otherPartyAccident = new WithCustomerAccident(otherPartyVehicle.getCurrentContract());
                    otherPartyAccident.setAccident((WithCustomerAccident)sinister);
                } else
                    sinister = new WithThirdPartyAccident(contract, otherPartyVehicle);
            else
                sinister = null;
        } else {
            type = dao.find(Type.class, ((PlainSinisterSubmission)submission).getTypeId());

            sinister = new PlainSinister(contract, type);
        }
        if(sinister != null ) {
            sinister.setComment(submission.getComment());
            sinister.setDate(submission.getDate());
            sinister.setTime(submission.getTime());
            sinister.setStatus(AWAITING);

            dao.save(sinister);
        }
    }
}