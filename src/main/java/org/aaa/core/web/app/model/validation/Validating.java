package org.aaa.core.web.app.model.validation;

import static java.util.Arrays.asList;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import org.aaa.core.web.api.model.input.validation.Errors;
import org.aaa.core.web.app.model.Command;
import org.aaa.util.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by alexandremasanes on 22/04/2017.
 */
public interface Validating {

    String COMMON_MESSAGE_BASE_CODE = "validation.common.";

    default void validate(Command command, Errors errors) throws Exception {
        Map<String, Object> map;
        Object value;

        if(command == null)
            throw new NullPointerException();

        map = new ObjectMapper().map(command);

        for(Entry<String, Object> entry : map.entrySet()) {
            value = entry.getValue();

            if ( value != null
            && (
                    (value instanceof MultipartFile && ((MultipartFile)value).isEmpty())
                 || (
                         value instanceof byte[] || value instanceof Byte[])
                      && asList(value).isEmpty()
                 )
            )
                errors.rejectValue(entry.getKey(), getMessage(COMMON_MESSAGE_BASE_CODE+"emptyFile"));
            else if(value == null || (value instanceof String && ((String)value).isEmpty()))
                errors.rejectValue(entry.getKey(), getMessage(COMMON_MESSAGE_BASE_CODE+"emptyField"));

        }
    }

   String getMessage(String code);
}
