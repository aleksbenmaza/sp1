package org.aaa.core.web.api.http.controller;

import org.aaa.core.business.mapping.Token;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by alexandremasanes on 23/07/2017.
 */
@RestController
public class ApiRootController extends BaseController {


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/", method = RequestMethod.HEAD)
    public void renewTokenIfExpired(
            @RequestHeader("Authorization") String              tokenValue,
                                            HttpServletResponse response
    ) {
        Token token;
        System.out.println("expect S.O");
        token = tokenService.replaceIfExpired(tokenValue);
        System.out.println("finally no S.O");
        if(token == null)
            throw new CustomHttpExceptions.BadRequestException();
        if(token.getOldValue() == null  || !token.getOldValue().equals(tokenValue))
            throw new CustomHttpExceptions.ResourceForbiddenException();
        response.setHeader("Authorization", token.getValue());
    }
}