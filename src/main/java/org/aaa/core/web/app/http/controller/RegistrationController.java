package org.aaa.core.web.app.http.controller;

import org.aaa.core.web.common.business.logic.CustomerService;
import org.aaa.core.web.common.business.logic.CustomerService.*;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.aaa.core.web.common.helper.MessageGetter;
import org.aaa.core.web.app.model.Login;
import org.aaa.core.web.app.model.Registration;
import org.aaa.core.web.app.model.validation.RegistrationValidator;


import org.aaa.util.MessageCode;
import org.aaa.util.RedirectView;
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
    private MessageGetter         messageGetter;

    @Autowired
    private RegistrationValidator registrationValidator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex() {
        return render(new ModelMap(new Registration()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object signUp(
            @RequestHeader("Host") String             serverName,
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

        RegistrationResult      registrationResult;


        if (!bindingResult.hasErrors()) {
            registrationResult = customerService.preRegister(registration, serverName);

            if (registrationResult.isSuccessful()) {
                redirectUri = "";
                messageCode.setValue(registrationResult.getMessageCode());
                redirectAttributes.addFlashAttribute(messageCode);
                return new RedirectView(redirectUri, HttpStatus.CREATED);

            } else
                messageCode.setValue(registrationResult.getMessageCode());
        }

        throw new CustomHttpExceptions.CommandNotValidatedException().withView(getViewName());
    }

    @RequestMapping(value = "/validation", method = RequestMethod.POST)
    public RedirectView validateNewAccount(
            @RequestParam String             validationCode,
            @RequestParam String             emailAddress,
                          RedirectAttributes redirectAttributes
    ) throws NoSuchAlgorithmException, IOException {
        MessageCode messageCode;
        messageCode = new MessageCode(
                customerService.register(validationCode) ?
                "notification.emailAddressValidation.success" :
                "notification.emailAddressValidation.failure"
        );

        redirectAttributes.addFlashAttribute(messageCode);
        return new RedirectView();
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}