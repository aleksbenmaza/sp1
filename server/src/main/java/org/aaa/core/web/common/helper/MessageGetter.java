package org.aaa.core.web.common.helper;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Created by alexandremasanes on 22/04/2017.
 */
@Component
public class MessageGetter {

    @Autowired
    private MessageSource messageSource;

    public String get(String code, Object... vars) {
        return messageSource.getMessage(code, vars, getLocale());
    }
}
