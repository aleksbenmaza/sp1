package org.aaa.core.web.api.model.input.validation;

import org.aaa.core.web.app.model.validation.Validating;
import org.aaa.core.web.common.helper.MessageGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
@Component
public class SinisterSubmissionValidator implements Validating {

    @Autowired
    private MessageGetter messageHelper;

    @Override
    public String getMessage(String code) {
        return messageHelper.get(code);
    }
}
