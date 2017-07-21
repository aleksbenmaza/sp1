package app.core.web.logic.controller;

import app.core.business.logic.TokenService;
import app.core.business.model.mapping.person.RegisteredUser;
import app.core.web.logic.controller.annotation.PreHandler;

import app.core.web.model.persistence.User;
import app.util.RedirectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

/**
 * Created by alexandremasanes on 30/03/2017.
 */

@Controller
public class LogoutController extends AppController {

    @Autowired
    private TokenService tokenService;

    @PreHandler
    public RedirectView preHandle(
            @SessionAttribute         User   user,
            @RequestHeader("referer") String referer
    ) throws Exception {
        if(!(user instanceof RegisteredUser))
            return new RedirectView(resolveReferer(referer));
        return null;
    }

    @RequestMapping(value = "/deconnexion", method = RequestMethod.GET)
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