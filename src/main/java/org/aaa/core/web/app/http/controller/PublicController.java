package org.aaa.core.web.app.http.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.aaa.core.web.common.business.logic.TokenService;
import org.aaa.core.business.mapping.person.RegisteredUser;
import org.aaa.core.business.mapping.Token;
import org.aaa.core.web.app.http.session.Guest;
import org.aaa.core.business.mapping.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by alexandremasanes on 03/03/2017.
 */
@Controller
@RequestMapping(method = GET)
public class PublicController extends BaseController {

    @Value("#{@systemEnvironment['AAA_API_SUBDOMAIN']}")
    private String test;

    @Autowired
    private TokenService tokenService;
/*
    @ModelAttribute
    public User getUser(@SessionAttribute User user) {
        return user;
    } */
    @PostConstruct
    public void print() {
        System.out.println(test);
    }

    @RequestMapping("${routes.public.home}")
    public ModelAndView getHomePage() {
        return render("home");
    }

    @RequestMapping("${routes.public.services}")
    public ModelAndView getServicesPage(
            @SessionAttribute User user
    ) throws NoSuchAlgorithmException {

        HashMap<String, Object> map = new HashMap<String, Object>();
        String tokenValue;

        tokenValue = user instanceof RegisteredUser ? (
                        ((RegisteredUser)user).getUserAccount().getToken() == null ?
                            null :
                        ((RegisteredUser)user).getUserAccount().getToken().getValue()
                ) :
                ((Guest)user).getTokenValue();
        if(tokenValue == null) {
            if (user instanceof RegisteredUser)
                tokenService.createToken(((RegisteredUser) user).getUserAccount());
            else
                ((Guest)user).setTokenValue(tokenService.createToken(null).getValue());
        }
        map.put("headTitleCode", "services");
        return render("services", map);
    }

    @RequestMapping("${routes.public.about}")
    public ModelAndView getAboutPage() {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("headTitleCode", "about");
        return render("about", map);
    }

    @Override
    protected String getViewName() {
        throw new RuntimeException(
                PublicController.class.getName() + " handles several views"
        );
    }
}