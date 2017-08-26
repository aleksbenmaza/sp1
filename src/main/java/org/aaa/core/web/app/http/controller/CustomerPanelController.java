package org.aaa.core.web.app.http.controller;

import org.aaa.core.business.mapping.person.insuree.Customer;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions;

import org.aaa.core.business.mapping.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by alexandremasanes on 21/02/2017.
 */
@Controller
@RequestMapping(value = "/espace-assure", method = RequestMethod.GET)
public class CustomerPanelController extends BaseController {

    public final static String VIEW_NAME = "customerpanel";

    /*
    @PreHandler
    public ForwardingView preHandle(@SessionAttribute User user) {

        if(user instanceof Customer)
            return null;
        return new ForwardingView(getWebroot() + "/connexion", HttpStatus.UNAUTHORIZED);
    }*/




    @ModelAttribute
    protected User getAndCheckUser(@SessionAttribute User user) {
        System.out.println("user :" + user);
        if(!(user instanceof Customer))
            throw new CustomHttpExceptions.UnauthorizedRequestException()
                                          .withForwarding("/connexion");

        return (Customer) user;


    }

    @RequestMapping({
            "${routes.customerPanel0}",
            "${routes.customerPanel1}",
            "${routes.customerPanel2}",
            "${routes.customerPanel3}",
            "${routes.customerPanel4}",
            "${routes.customerPanel5}",
            "${routes.customerPanel6}"
    }) public ModelAndView getIndex(
            @ModelAttribute("user") Customer customer
    ) {
        return render();
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}