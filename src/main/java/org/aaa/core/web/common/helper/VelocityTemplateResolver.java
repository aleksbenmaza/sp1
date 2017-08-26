package org.aaa.core.web.common.helper;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

/**
 * Created by alexandremasanes on 18/04/2017.
 */

@Component
public class VelocityTemplateResolver {

    public static final String TEMPLATES_MAIL_DIR          = "mail";

    public static final String TEMPLATES_CUSTOMERPANEL_DIR = "customerpanel";

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private MessageSource  messageSource;

    public String getRegistrationValidationMailTemplate(String serverName, String validationCode) {

        HashMap<String, Object> map;

        map = new HashMap<>();

        map.put("serverName", serverName);
        map.put("validationCode", validationCode);

        return loadTemplate(TEMPLATES_MAIL_DIR + "/validation.mail.html.vm", map);
    }

    public String getCustomerpanelTemplate(String templateName) {
        return getCustomerpanelTemplate(templateName, new HashMap<String, Object>());
    }

    public String getCustomerpanelTemplate(String templateName, Map<String, Object> vars) {
        return loadTemplate(TEMPLATES_CUSTOMERPANEL_DIR +"/" + templateName, vars);
    }

    private String loadTemplate(String templatePath, Map<String, Object> vars) {

        StringWriter writer;
        Template template;
        Context context;

        writer = new StringWriter();
        try {
            template = velocityEngine.getTemplate(templatePath+".vm");
        } catch (ResourceNotFoundException e) {
            return null;
        }

        context = new VelocityContext();
        context.put("NULL", null);
        context.put("messages", messageSource);
        context.put("locale", getLocale());
        for(Entry<String, Object> entry : vars.entrySet())
            context.put(entry.getKey(), entry.getValue());

        template.merge(context, writer);

        return writer.toString();
    }
}
