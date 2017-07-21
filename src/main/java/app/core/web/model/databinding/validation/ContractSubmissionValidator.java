package app.core.web.model.databinding.validation;

import static java.util.Arrays.asList;
import static java.net.URLConnection.guessContentTypeFromStream;

import app.core.business.model.dao.DAO;
import app.core.business.model.mapping.Insurance;
import app.core.business.model.mapping.Model;
import app.core.web.logic.helper.MessageHelper;
import app.core.web.model.databinding.command.Command;
import app.core.web.model.databinding.command.ContractSubmission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by alexandremasanes on 22/04/2017.
 */
@Component
public class ContractSubmissionValidator implements Validating {

    private static final String MESSAGE_BASE_CODE    = "validation.contractSubmission.";

    @Value("${patterns.registrationNumber}")
    private String regNumberPattern;

    @Value("${patterns.vinNumber}")
    private String vinNumberPattern;

    @Value("#{'${file.allowedImageMimeTypes}'.split(',')}")
    private String[] allowedImageMimeType;

    @Value("${file.maxSize}")
    private long maxFileSize;

    @Autowired
    private DAO dao;

    @Autowired
    private MessageHelper messageHelper;

    @Override
    public void validate(Command command, Errors errors) throws Exception {
        ContractSubmission contractSubmission;

        contractSubmission = (ContractSubmission) command;

        Validating.super.validate(contractSubmission, errors);
        validateInsuranceId(contractSubmission.getInsuranceId(), errors);
        validateModelId(contractSubmission.getModelId(), errors);
        validateVinNumber(contractSubmission.getVinNumber(), errors);
        validateRegistrationNumber(contractSubmission.getRegistrationNumber(), errors);
        validatePurchaseDate(contractSubmission.getPurchaseDate(), errors);
        validateRegistrationDocument(contractSubmission.getRegistrationDocument(), errors);
    }

    public String getMessage(String code) {
        return messageHelper.getMessage(code);
    }

    private void validateInsuranceId(Long id, Errors errors) {
        if(errors.get("insuranceId") == null && !dao.exists(Insurance.class, id))
            errors.rejectValue("insuranceId", getMessage(MESSAGE_BASE_CODE+"nonExistentInsurance"));
    }

    private void validateModelId(Long id, Errors errors) {
        if(errors.get("modelId") == null && !dao.exists(Model.class, id))
            errors.rejectValue("insuranceId", getMessage(MESSAGE_BASE_CODE+"nonExistentModel"));
    }

    private void validateVinNumber(String vinNumber, Errors errors) {
        if(errors.get("vinNumber") == null && !vinNumber.matches(vinNumberPattern))
            errors.rejectValue("vinNumber", getMessage(MESSAGE_BASE_CODE+"invalidVinNumberPattern"));

    }

    private void validateRegistrationNumber(String registrationNumber, Errors errors) {
        if(errors.get("registrationNumber") == null
        && !registrationNumber.matches(regNumberPattern))
            errors.rejectValue("registrationNumber", getMessage(MESSAGE_BASE_CODE+"invalidRegistrationNumberPattern"));
    }

    private void validatePurchaseDate(Date purchaseDate, Errors errors) {
        if(errors.get("purchaseDate") == null && (purchaseDate.compareTo(new Date()) >= 0))
            errors.rejectValue("purchaseDate", getMessage(MESSAGE_BASE_CODE+"invalidPurchaseDate"));
    }

    private void validateRegistrationDocument(byte[] registrationDocument, Errors errors) throws IOException {
        String ext;
        ByteArrayInputStream byteArrayInputStream;

        if(errors.get("registrationDocument") == null) {

            if(registrationDocument.length > maxFileSize)
                errors.rejectValue("registrationDocument", getMessage(COMMON_MESSAGE_BASE_CODE+"tooLargeFile"));

            else {
                byteArrayInputStream = new ByteArrayInputStream(registrationDocument);
                ext = guessContentTypeFromStream(byteArrayInputStream);
                if (!asList(allowedImageMimeType).contains(ext))
                    errors.rejectValue("registrationDocument", getMessage(COMMON_MESSAGE_BASE_CODE + "invalidFileExtension"));
            }
        }
    }
}
