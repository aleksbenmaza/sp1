package org.aaa.core.web.app.http.controller;

import org.aaa.core.business.mapping.entity.person.RegisteredUser;
import org.aaa.core.web.common.business.logic.TokenService;
import org.aaa.core.web.common.business.logic.UserService.*;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.web.app.model.Login;
import org.aaa.core.business.mapping.entity.person.User;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.aaa.core.web.app.util.MessageCode;
import org.aaa.core.web.app.util.RedirectView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.google.common.collect.ImmutableMap.of;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by alexandremasanes on 12/03/2017.
 */
@Controller
@RequestMapping("${routes.login}")
public class LoginController extends GuestController {

    private static final String VIEW_NAME = "login";

    private Map<Class<? extends User>, String> redirectURIs;

    @Value("${routes.login}")
    private String loginPath;

    @Value("${routes.customerPanel.root}")
    private String customerPanelPath;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex() {
        return render(new ModelMap(new Login()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public RedirectView signIn(
            @RequestHeader("Referer") String             referer,
            @ModelAttribute           Login              login,
            @ModelAttribute           MessageCode        messageCode,
                                      HttpSession        httpSession
    ) throws NoSuchAlgorithmException {
        User        user;
        LoginResult loginResult;

        loginResult = userService.proceedLogin(login);

        if(loginResult.getRegisteredUser() == null) {
            messageCode.setValue(loginResult.getMessageCode());
            throw new CustomHttpExceptions.CommandNotValidatedException()
                                          .withModelAndView(render(new ModelMap(login)));
        } else {
            user = loginResult.getRegisteredUser();
            tokenService.createEncrypted(((RegisteredUser) user).getUserAccount());
            setSessionUser(httpSession, user);
        }

        if(referer.contains(loginPath))
            return new RedirectView(redirectURIs.get(user.getClass()));
        else
            return new RedirectView(referer);
    }

    @PostConstruct
    protected void init() {
        redirectURIs = of(Customer.class, customerPanelPath);
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}