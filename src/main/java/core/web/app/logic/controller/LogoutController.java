package core.web.app.logic.controller;

import core.business.logic.TokenService;
import core.business.model.mapping.person.RegisteredUser;

import core.web.common.logic.exception.CustomHttpExceptions;

import core.web.app.model.persistence.User;
import util.RedirectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by alexandremasanes on 30/03/2017.
 */

@Controller
public class LogoutController extends AppController {

    @Autowired
    private TokenService tokenService;

    @ModelAttribute
    public void checkUser(
            @SessionAttribute         User   user,
            @RequestHeader("referer") String referer
    ) {
        if(!(user instanceof RegisteredUser))
            throw new CustomHttpExceptions.ResourceForbiddenException()
                                          .withRedirect(resolveReferer(referer));
    }

    @RequestMapping(value = "${routes.logout}", method = RequestMethod.GET)
    public RedirectView signOut(
                                      HttpSession session,
            @RequestHeader("referer") String      referer
    ) {

        session.invalidate();
        return new RedirectView(resolveReferer(referer));
    }

    @Override
    protected String getViewName() {
        throw new RuntimeException(this.getClass().getName() + " does not handle any view.");
    }
}