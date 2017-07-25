package core.web.app.logic.controller;

import core.business.logic.UserService;
import core.business.model.mapping.UserAccount;

import core.web.common.logic.exception.CustomHttpExceptions;
import core.web.app.model.databinding.PasswordReinitialization;
import util.MessageCode;
import util.RedirectView;
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
    public Object submitEmailAddress(
                                                 HttpSession session,
                @RequestParam("email_address")   String      emailAddress,
                @ModelAttribute                  MessageCode messageCode

    ) {

        if(!emailAddress.matches(emailPattern)) {
            messageCode.setValue("validation.email.badPattern");
            return render("lostpassword");
        }

        if(userService.emailAddressExists(emailAddress)) {
            session.setAttribute("userAccount", userService.getUserAccount(emailAddress));
            return new RedirectView(getWebroot() + "/mot-de-passe-perdu/reinitialiser");
        }

        messageCode.setValue("validation.email.unkown");
        return render("lostpasword");

    }

    @RequestMapping(
            value  = "${routes.passwordReinitialization}",
            method = RequestMethod.POST

    ) public RedirectView submitPassword(
            @SessionAttribute UserAccount        userAccount,
            @RequestParam     String             password,
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
                PasswordReinitialization.class.getName() + " handles several views"
        );
    }
}