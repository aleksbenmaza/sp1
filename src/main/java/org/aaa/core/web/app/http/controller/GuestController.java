package org.aaa.core.web.app.http.controller;


import org.aaa.core.web.common.http.exception.CustomHttpExceptions;

import org.aaa.core.web.app.http.session.Guest;

import org.aaa.core.business.mapping.entity.person.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Created by alexandremasanes on 21/02/2017.
 */

public abstract class GuestController extends BaseController {

    @ModelAttribute
    public void checkUser(
            @SessionAttribute                                   User   user,
            @RequestHeader(value = "referer", required = false) String referer
    ) {
        if(!(user instanceof Guest))
            throw new CustomHttpExceptions.ResourceForbiddenException().withRedirect(resolveReferer(referer));
    }
}