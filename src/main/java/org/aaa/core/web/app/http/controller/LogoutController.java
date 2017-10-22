package org.aaa.core.web.app.http.controller;

import org.aaa.core.business.mapping.person.RegisteredUser;
import org.aaa.core.business.mapping.User;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.aaa.util.RedirectView;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by alexandremasanes on 30/03/2017.
 */

@Controller
public class LogoutController extends BaseController {

    @Value("${routes.customerPanel.root}")
    private String customerPanelPath;

    @Value("${routes.login}")
    private String loginPath;

    @ModelAttribute
    public void checkUser(@SessionAttribute User user) {
        if(!(user instanceof RegisteredUser))
            throw new CustomHttpExceptions.UnauthorizedRequestException();
    }

    @RequestMapping(value = "${routes.logout}", method = RequestMethod.POST)
    public RedirectView signOut(
                                      HttpSession session,
            @RequestHeader("referer") String      referer
    ) {
        String path;
        session.invalidate();

        path = referer.contains(customerPanelPath) ? loginPath : referer;
        return new RedirectView(path);
    }

    @Override
    protected String getViewName() {
        throw new RuntimeException(this.getClass().getName() + " does not handle any view.");
    }
}