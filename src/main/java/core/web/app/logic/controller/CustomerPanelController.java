package core.web.logic.controller;

import static core.business.model.mapping.IdentifiableById.Utils.toSortedList;

import core.business.model.mapping.Contract;
import core.business.model.mapping.person.insuree.Customer;

import core.web.logic.exception.CustomHttpExceptions.WithViewUnauthorizedRequestException;
import core.web.model.persistence.User;
import util.ForwardView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


/**
 * Created by alexandremasanes on 21/02/2017.
 */
@Controller
@RequestMapping(value = "/espace-assure", method = RequestMethod.GET)
public class CustomerPanelController extends AppController {

    public final static String VIEW_NAME = "customerpanel";
    /*
    @PreHandler
    public ForwardView preHandle(@SessionAttribute User user) {

        if(user instanceof Customer)
            return null;
        return new ForwardView(getWebroot() + "/connexion", HttpStatus.UNAUTHORIZED);
    }*/


    @ModelAttribute("1")
    protected void preHandle(@SessionAttribute User user) {
        System.out.println("user :" + user);
        if(!(user instanceof Customer))
            throw new WithViewUnauthorizedRequestException(
                    new ForwardView(
                            getWebroot() + "/connexion"
                    )
            );

    }

    @RequestMapping(value = {"",
                             "/nouveau-contrat",
                             "/vos-contrats",
                             "/contrat/{contractKey:\\d+}",
                             "/contrat/{contractKey:\\d+}/nouveau-sinistre",
                             "/contrat/{contractKey:\\d+}/sinistres",
                             "/contrat/{contractKey:\\d+}/sinistre/{sinisterKey:\\d+}"},
                    method = RequestMethod.GET
    ) public ModelAndView getIndex(@SessionAttribute User user) {
        Customer customer;
        ModelMap model;
        List<Contract> contracts;

        customer  = (Customer) user;
        model     = new ModelMap();
        contracts = toSortedList(customer.getContracts());

        model.put("API_ACCESS_KEY", customer.getUserAccount().getToken().getValue());
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