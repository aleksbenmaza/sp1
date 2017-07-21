package app.core.web.model.databinding.validation;

import app.core.web.logic.helper.MessageHelper;
import app.core.web.model.databinding.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
@Component
public class SinisterSubmissionValidator implements Validating {

    @Autowired
    private MessageHelper messageHelper;

    @Override
    public String getMessage(String code) {
        return messageHelper.getMessage(code);
    }
}
