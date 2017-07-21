package app.core.web.logic.controller;

import app.core.business.logic.UserService;
import app.core.web.model.persistence.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.ServletContext;

/**
 * Created by alexandremasanes on 20/02/2017.
 */

public abstract class BaseController {

    private static String webroot;

    @Autowired
    private UserService userService;

    @Autowired
    public void setWebroot(ServletContext servletContext) {
        if(webroot == null)
            webroot = servletContext.getContextPath();
    }

    protected String getWebroot() {
        return webroot;
    }
}