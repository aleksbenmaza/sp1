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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/", method = RequestMethod.HEAD)
    public void renewTokenIfExpired(
                                            HttpServletResponse response,
            @RequestHeader("Authorization") String              encryptedString

    ) {
        String newEncryptedString;
        System.out.println("expect S.O");
        newEncryptedString = tokenService.replaceIfExpired(encryptedString);
        System.out.println("finally no S.O");
        if(encryptedString == null)
            throw new CustomHttpExceptions.BadRequestException();
        if(encryptedString.equals(newEncryptedString))
            throw new CustomHttpExceptions.ResourceForbiddenException();
        response.setHeader("Authorization", newEncryptedString);
    }
}
