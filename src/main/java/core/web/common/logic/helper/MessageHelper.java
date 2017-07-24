package core.web.logic.helper;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Created by alexandremasanes on 22/04/2017.
 */
@Component
public class MessageHelper {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code, Object... vars) {
        return messageSource.getMessage(code, vars, getLocale());
    }
}
