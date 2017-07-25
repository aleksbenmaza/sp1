package core.web.app.logic.controller;

import static core.business.model.mapping.IdentifiableById.Utils.toSortedList;

import core.business.model.mapping.Contract;
import core.business.model.mapping.person.insuree.Customer;

import core.web.common.logic.exception.CustomHttpExceptions;

import core.web.app.model.persistence.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


/**
 * Created by alexandremasanes on 21/02/2017.
 */
@Controller
@RequestMapping(value = "${routes.customerPanel}", method = RequestMethod.GET)
public class CustomerPanelController extends AppController {

    @Value("${apiSubdomain}")
    private String apiSubdomain;

    public final static String VIEW_NAME = "customerpanel";
    /*
    @PreHandler
    public ForwardingView preHandle(@SessionAttribute User user) {

        if(user instanceof Customer)
            return null;
        return new ForwardingView(getWebroot() + "/connexion", HttpStatus.UNAUTHORIZED);
    }*/


    @ModelAttribute
    protected Customer getCustomer(@SessionAttribute User user) {
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
                                   Customer customer,
            @RequestHeader("Host") String   host
    ) {
        ModelMap model;
        List<Contract> contracts;

        model     = new ModelMap();
        contracts = toSortedList(customer.getContracts());

        model.put("API_ACCESS_KEY", customer.getUserAccount().getToken().getValue());
        model.put("API_SERVER_NAME", apiSubdomain + "." + host);
        model.put("contracts", contracts);
        return render(customer, model);
    }

    protected ModelAndView render(Customer customer, Map<String, Object> model) {
        buildUserNavbar(customer, model);
        return super.render(model);
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}