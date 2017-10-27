package org.aaa.core.web.app.http.controller;

import org.aaa.core.business.mapping.entity.User;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by alexandremasanes on 24/09/2017.
 */
@Controller
@RequestMapping(value = "/resources/customer", method = RequestMethod.GET)
public class CustomerResourcesController extends BaseController {

    @ModelAttribute
    public Customer checkedUser(@SessionAttribute User user) {
        if(!(user instanceof Customer))
            throw new CustomHttpExceptions.ResourceForbiddenException();
        return (Customer) user;
    }

    @RequestMapping(value = "/templates/customerpanel/{templateName}.html")
    public ModelAndView serveCustomerPanelTemplate(
                          Customer customer,
            @PathVariable String   templateName
    ) {
        System.out.println(templateName);
        return new ModelAndView("customerpanel/" + templateName, new ModelMap(customer));
    }

    @Deprecated
    @Override
    protected String getViewName() {
        throw new UnsupportedOperationException();
    }
}
