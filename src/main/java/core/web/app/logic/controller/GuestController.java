package core.web.app.logic.controller;


import core.web.common.logic.exception.CustomHttpExceptions.ResourceForbiddenException;

import core.web.app.model.persistence.Guest;

import core.web.app.model.persistence.User;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Created by alexandremasanes on 21/02/2017.
 */

public abstract class GuestController extends AppController {
/*
    @PreHandler
    public RedirectView preHandler(
            @SessionAttribute                                   User   user,
            @RequestHeader(value = "referer", required = false) String referer
    ) throws Exception {
        if(!(user instanceof Guest))
            return new RedirectView(resolveReferer(referer));
        return null;
    } */

    @ModelAttribute
    public void checkUser(
            @SessionAttribute                                   User   user,
            @RequestHeader(value = "referer", required = false) String referer,
            HttpMethod httpMethod
    ) {

        ResourceForbiddenException exception;
        if(!(user instanceof Guest)) {
            exception = new ResourceForbiddenException();
            throw (httpMethod == HttpMethod.GET) ?
                    exception.withRedirect(resolveReferer(referer)) :
                    exception;
        }
    }
}