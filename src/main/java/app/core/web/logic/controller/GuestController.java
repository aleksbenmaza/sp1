package app.core.web.logic.controller;


import app.core.web.logic.controller.annotation.PreHandler;
import app.core.web.model.persistence.Guest;

import app.core.web.model.persistence.Session;
import app.core.web.model.persistence.User;
import app.util.RedirectView;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Created by alexandremasanes on 21/02/2017.
 */

public abstract class GuestController extends AppController {

    @PreHandler
    public RedirectView preHandler(
            @SessionAttribute                                   User   user,
            @RequestHeader(value = "referer", required = false) String referer
    ) throws Exception {
        if(!(user instanceof Guest))
            return new RedirectView(resolveReferer(referer));
        return null;
    }
}