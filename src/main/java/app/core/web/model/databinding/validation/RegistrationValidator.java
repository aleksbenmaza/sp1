package app.core.web.model.databinding.validation;

import static app.core.web.model.databinding.validation.Validating.COMMON_MESSAGE_BASE_CODE;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import app.core.business.model.dao.DAO;
import app.core.web.model.databinding.command.Registration;

import app.util.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by alexandremasanes on 05/03/2017.
 */

@Component
public class RegistrationValidator implements Validator {

    private static final String MESSAGE_BASE_CODE  = "registration.";

    @Value("${patterns.email}")
    private String   emailPattern;

    @Value("#{'${file.allowedImageMimeTypes}'.split(',')}")
    private String[] allowedImageMimeType;

    @Value("${file.maxSize}")
    private long     maxFileSize;

    @Autowired
    private DAO      dao;


    @Override
    public boolean supports(Class<?> clazz) {
        return Registration.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Registration registration;
        Map<String, Object> map;
        registration = (Registration)target;

        map = new ObjectMapper().map(registration);

       for(Entry<String, Object> entry : map.entrySet())

           if(entry.getValue() == null || (entry.getValue() instanceof MultipartFile && ((MultipartFile)entry.getValue()).isEmpty()))
               errors.rejectValue(entry.getKey(), Validating.COMMON_MESSAGE_BASE_CODE + "emptyFile");

           else if(entry.getValue() == null)
                   errors.rejectValue(entry.getKey(), Validating.COMMON_MESSAGE_BASE_CODE + "emptyField");

           else
               rejectIfEmptyOrWhitespace(
                       errors,
                       entry.getKey(),
                       Validating.COMMON_MESSAGE_BASE_CODE + "emptyField"
               );

       validateEmail(registration.getEmailAddress(), errors);
       validateZipCode(registration.getZipCode(), errors);
       validatePhoneNumber(registration.getPhoneNumber(), errors);
       validatePasswords(registration.getPassword(),
                        registration.getPasswordConfirm(),
                        errors);
       validateIdCard(registration.getIdCard(), errors);
    }

    private void validateEmail(String email, Errors errors) {
        if(!errors.hasFieldErrors("emailAddress")
        && !email.matches(emailPattern))
            errors.rejectValue(
                    "emailAddress",
                    MESSAGE_BASE_CODE+"badEmailPattern");
        else if(dao.emailExists(email)) {
            errors.rejectValue(
                    "emailAddress",
                    "notification.alreadyUsedEmailAddress"
            );
        }
    }

    private void validateZipCode(Short zipCode, Errors errors) {
        if(!errors.hasFieldErrors("zipCode")
        && Short.toString(zipCode).length() != 5)
            errors.rejectValue("zipCode", MESSAGE_BASE_CODE + "badZipCodeLen");


    }

    private void validatePhoneNumber(String phoneNumber, Errors errors) {
        int strLen;
        strLen = phoneNumber != null ? phoneNumber.length() : 0;
        if(!errors.hasFieldErrors("phoneNumber")
        &&  strLen > 9 && strLen < 12)
            errors.rejectValue("phoneNumber", MESSAGE_BASE_CODE+"badPhoneNumberLen");
    }

    private void validatePasswords(String password, String passwordConfirm, Errors errors) {
        if(!errors.hasFieldErrors("password")
        && !errors.hasFieldErrors("passwordConfirm")
        && !password.equals(passwordConfirm))
            errors.rejectValue("password",MESSAGE_BASE_CODE + "nonMatchingPasswords");

    }

    private void validateIdCard(MultipartFile idCard, Errors errors) {
        String extension;
        Long size;
        extension = idCard.getContentType();
        size      = idCard.getSize();
        if(!errors.hasFieldErrors("idCard")) {
            if(size > maxFileSize) {
                errors.rejectValue("idCard", COMMON_MESSAGE_BASE_CODE+"badFileSize");
                return;
            }

            for(String ext : allowedImageMimeType)
                if(!(extension.equals(ext) && extension.equals(ext.toLowerCase()))) {
                    errors.rejectValue("idCard", COMMON_MESSAGE_BASE_CODE+"badFileExt");
                    break;
                }
        }
    }
}