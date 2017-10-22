package org.aaa.core.web.app.http.controller;


import org.aaa.core.business.mapping.person.Manager;
import org.aaa.core.business.mapping.person.RegisteredUser;
import org.aaa.core.web.common.business.logic.TokenService;
import org.aaa.core.web.common.business.logic.UserService.*;
import org.aaa.core.business.mapping.person.insuree.Customer;
import org.aaa.core.web.app.model.Login;
import org.aaa.core.business.mapping.User;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.aaa.util.MessageCode;
import org.aaa.util.RedirectView;

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
import java.util.Hashtable;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by alexandremasanes on 12/03/2017.
 */
@Controller
@RequestMapping("${routes.login}")
public class LoginController extends GuestController {

    public  static final String VIEW_NAME = "login";

    private Map<Class<? extends User>, String> redirectURIs;

    @Value("${routes.login}")
    private String loginPath;

    @Value("${routes.customerPanel.root}")
    private String customerPanelPath;

    @Autowired
    private TokenService tokenService;

    @PostConstruct
    public void init() {
        redirectURIs = of(Customer.class, customerPanelPath);
    }

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
                                          .withView(VIEW_NAME);
        } else {
            user = loginResult.getRegisteredUser();
            tokenService.createToken(((RegisteredUser) user).getUserAccount());
            setSessionUser(httpSession, user);
        }

        if(referer.contains(loginPath))
            return new RedirectView(redirectURIs.get(user.getClass()));
        else
            return new RedirectView(referer);
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}
