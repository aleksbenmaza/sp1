package org.aaa.core.web.app.http.controller;

import org.aaa.core.web.common.business.logic.UserService;
import org.aaa.core.business.mapping.entity.UserAccount;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.aaa.util.MessageCode;
import org.aaa.util.RedirectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alexandremasanes on 22/06/2017.
 */
@Controller
@RequestMapping("${routes.lostPassword}")
public class PasswordReinitializationController extends GuestController {

    @Autowired
    private UserService userService;

    @Value("${patterns.email}")
    private String emailPattern;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex() {
        return render("lostpassword");
    }

    @RequestMapping(
            value  = "${routes.passwordReinitialization}",
            method = RequestMethod.GET

    ) public ModelAndView getNewPasswordForm() {
        return render("passwordreinitialization");
    }

    @RequestMapping(method = RequestMethod.POST)
    public RedirectView submitEmailAddress(
                                                HttpSession session,
                @ModelAttribute @RequestParam   String      emailAddress,
                @ModelAttribute                 MessageCode messageCode

    ) {

        if(!emailAddress.matches(emailPattern)) {
            messageCode.setValue("validation.email.badPattern");
            throw new CustomHttpExceptions.CommandNotValidatedException().withView("lostpassword");
        }

        if(userService.emailAddressExists(emailAddress)) {
            session.setAttribute("userAccount", userService.getUserAccount(emailAddress));
            return new RedirectView("/mot-de-passe-perdu/reinitialiser");
        }

        messageCode.setValue("validation.email.unkown");
        throw new CustomHttpExceptions.CommandNotValidatedException().withView("lostpasword");

    }

    @RequestMapping(
            value  = "${routes.passwordReinitialization}",
            method = RequestMethod.POST

    ) public RedirectView submitPassword(
            @SessionAttribute             UserAccount        userAccount,
            @ModelAttribute @RequestParam String             password,
                                          RedirectAttributes redirectAttributes
    ) throws NoSuchAlgorithmException {

        if(userAccount != null) {
            userService.resetPassword(userAccount, password);
            redirectAttributes.addFlashAttribute(new MessageCode("notification.reinitializedPassword"));
        }
        throw new CustomHttpExceptions.ResourceForbiddenException();
    }

    @Override
    protected String getViewName() {
        throw new RuntimeException(
                PasswordReinitializationController.class.getName() + " handles several views"
        );
    }
}