package core.web.logic.controller;


import core.web.logic.exception.CustomHttpExceptions.WithViewResourceForbiddenException;
import core.web.model.persistence.Guest;

import core.web.model.persistence.User;
import util.RedirectView;
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

    @ModelAttribute("0")
    public void preHandle(
            @SessionAttribute                                   User   user,
            @RequestHeader(value = "referer", required = false) String referer
    ) {
        if(!(user instanceof Guest))
            throw new WithViewResourceForbiddenException(
                    new RedirectView(resolveReferer(referer))
            );
    }
}