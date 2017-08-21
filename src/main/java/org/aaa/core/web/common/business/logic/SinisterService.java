package org.aaa.core.web.common.business.logic;

import static org.aaa.core.business.mapping.ToBeChecked.Status.*;

import org.aaa.core.business.mapping.Contract;
import org.aaa.core.business.mapping.Model;
import org.aaa.core.business.mapping.Vehicle;
import org.aaa.core.business.mapping.sinister.PlainSinister;
import org.aaa.core.business.mapping.sinister.PlainSinister.Type;
import org.aaa.core.business.mapping.sinister.Sinister;
import org.aaa.core.business.mapping.sinister.accident.CustomerAccident;
import org.aaa.core.business.mapping.sinister.accident.ThirdPartyAccident;
import org.aaa.core.web.api.model.input.databinding.sinister.AccidentSubmission;
import org.aaa.core.web.api.model.input.databinding.sinister.PlainSinisterSubmission;
import org.aaa.core.web.api.model.input.databinding.sinister.SinisterSubmission;

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
        CustomerAccident otherPartyAccident;
        Model            otherPartyModel;
        Vehicle          contractVehicle, otherPartyVehicle;
        Type             type;
        byte[]           reportDocument;
        FileOutputStream outputStream;
        String           reportFileName;

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

            otherPartyVehicle = dao.findVehicleByRegistrationNumber(
                    ((AccidentSubmission) submission).getRegistrationNumber()
            );

            if(otherPartyVehicle == null) {
                otherPartyModel = dao.find(Model.class, ((AccidentSubmission) submission).getModelId());
                otherPartyVehicle = new Vehicle(otherPartyModel);
                otherPartyVehicle.setRegistrationNumber(
                        ((AccidentSubmission) submission).getRegistrationNumber()
                );
            }

            if(otherPartyVehicle.getCurrentContract() != null) {
                sinister           = new CustomerAccident(contract);
                otherPartyAccident = new CustomerAccident(otherPartyVehicle.getCurrentContract());
                otherPartyAccident.setAccident((CustomerAccident)sinister);
            } else
                sinister = new ThirdPartyAccident(contract, otherPartyVehicle);

        } else {
            type = dao.find(Type.class, ((PlainSinisterSubmission)submission).getTypeId());

            sinister = new PlainSinister(contract, type);
        }

        sinister.setComment(submission.getComment());
        sinister.setDate(submission.getDate());
        sinister.setTime(submission.getTime());
        sinister.setStatus(AWAITING);

        dao.save(sinister);
    }
}