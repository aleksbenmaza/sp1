package core.web.app.logic.controller;

import core.business.logic.CustomerService;
import core.business.logic.CustomerService.*;
import core.web.common.logic.helper.MessageHelper;
import core.web.app.model.databinding.Login;
import core.web.app.model.databinding.Registration;
import core.web.app.logic.validation.RegistrationValidator;


import util.MessageCode;
import util.RedirectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


/**
 * Created by alexandremasanes on 12/03/2017.
 */
@Controller
@RequestMapping("${routes.registration}")
public class RegistrationController extends GuestController {

    public final static String VIEW_NAME = "registration";

    @Autowired
    private CustomerService       customerService;

    @Autowired
    private MessageHelper         messageHelper;

    @Autowired
    private RegistrationValidator registrationValidator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex() {
        return render(new ModelMap(new Registration()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object signUp(
            @ModelAttribute        Registration       registration,
            @ModelAttribute        MessageCode        messageCode,
                                   Login              login,
                                   BindingResult      bindingResult,
                                   RedirectAttributes redirectAttributes
    ) {

        ModelAndView modelAndView;

        modelAndView = render();

        if (!login.isEmpty()) {
            registration.setEmailAddress(login.getEmailAddress());
            registration.setPassword(login.getPassword());
            return modelAndView;
        }

        registrationValidator.validate(registration, bindingResult);

        String                  redirectUri;
        HttpStatus              httpStatus;
        RegistrationResult      registrationResult;

        httpStatus = HttpStatus.BAD_REQUEST;

        if (!bindingResult.hasErrors()) {
            registrationResult = customerService.preRegister(registration);

            if (registrationResult.isSuccessful()) {
                redirectUri = getWebroot();
                httpStatus  = HttpStatus.CREATED;
                messageCode.setValue(registrationResult.getMessageCode());
                redirectAttributes.addFlashAttribute(messageCode);
                return new RedirectView(redirectUri, httpStatus);

            } else
                messageCode.setValue(registrationResult.getMessageCode());
        }

        ;
        modelAndView.setStatus(httpStatus);
        return modelAndView;
    }
    /*
    @RequestMapping(params = "login", method = RequestMethod.POST)
    public ModelAndView getIndexFromLoginPage(@ModelAttribute Registration registration) {
        return getIndex(registration);
    } */

    @RequestMapping(value = "/validation", method = RequestMethod.POST)
    public RedirectView validateNewAccount(
            @RequestParam String             validationCode,
                          RedirectAttributes redirectAttributes
    ) throws NoSuchAlgorithmException, IOException {
        MessageCode messageCode;
        messageCode = new MessageCode(customerService.register(validationCode) ?
                "notfication.emailAddressValidation.success" :
                "notfication.emailAddressValidation.failure");

        redirectAttributes.addFlashAttribute(messageCode);
        return new RedirectView(getWebroot());
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}