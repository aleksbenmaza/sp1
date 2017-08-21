package org.aaa.core.web.common.http.controller;

import org.aaa.core.web.common.business.logic.UserService;
import org.springframework.beans.factory.annotation.Autowired;

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