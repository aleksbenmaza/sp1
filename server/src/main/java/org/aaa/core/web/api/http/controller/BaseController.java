package org.aaa.core.web.api.http.controller;

import org.aaa.core.web.common.business.logic.TokenService;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by alexandremasanes on 25/03/2017.
 */

public abstract class BaseController {

    @Autowired
    protected TokenService tokenService;

    protected void setLocationResponseHeader(HttpServletResponse responseHeader, long value) {
        responseHeader.setHeader("Location", Long.toString(value));
    }

    protected void setAuthorizationResponseHeader(HttpServletResponse responseHeader, String authToken) {
        responseHeader.setHeader("Authorization", authToken);
    }
}