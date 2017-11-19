package org.aaa.core.web.app.http.controller;

import org.aaa.core.business.mapping.entity.person.insuree.Customer;

import org.aaa.core.web.common.business.logic.TokenService;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;

import org.aaa.core.business.mapping.entity.person.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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

    @Autowired
    private TokenService tokenService;

    @ModelAttribute
    public Customer checkedCustomer(@SessionAttribute User user) {
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
    }) public ModelAndView getIndex(Customer customer) {
        String encryptedToken;
        encryptedToken = tokenService.createEncrypted(customer.getUserAccount());
        return render(new ModelMap("encryptedToken", encryptedToken));
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}