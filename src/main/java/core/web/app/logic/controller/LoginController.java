package core.web.app.logic.controller;

import core.business.logic.TokenService;
import core.business.logic.UserService.*;
import core.business.model.mapping.UserAccount;
import core.business.model.mapping.person.insuree.Customer;
import core.web.app.model.databinding.Login;
import core.web.app.model.persistence.Guest;
import core.web.app.model.persistence.User;
import util.MessageCode;
import util.RedirectView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

/**
 * Created by alexandremasanes on 12/03/2017.
 */
@Controller
@RequestMapping("${routes.login}")
public class LoginController extends GuestController {

    public  static final String VIEW_NAME = "login";

    private static final Hashtable<Class<? extends User>, String> urls;

    static {
        urls = new Hashtable<Class<? extends User>, String>() {
            {
                put(Guest.class   , "/connexion");
                put(Customer.class, "/espace-assure");
            }
        };
    }

    @Autowired
    private TokenService tokenService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex() {
        return render(new ModelMap(new Login()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object signIn(
            @RequestHeader("Referer") String             referer,
            @ModelAttribute           Login              login,
            @ModelAttribute           MessageCode        messageCode,
                                      HttpSession        httpSession
    ) throws NoSuchAlgorithmException {
        User        user;
        UserAccount userAccount;
        LoginResult loginResult;

        loginResult = userService.proceedLogin(login);

        if(loginResult.getRegisteredUser() == null) {
            messageCode.setValue(loginResult.getMessageCode());
            return render();
        } else {
            httpSession.invalidate();
            userAccount = loginResult.getRegisteredUser().getUserAccount();
            //System.out.println(userAccount.getToken().getValue());
            if(userAccount.getToken() == null)
                tokenService.createToken(userAccount);

            user = loginResult.getRegisteredUser();
            setSessionUser(httpSession, user);
        }

        if(referer.contains("connexion"))
            return new RedirectView(getWebroot() + urls.get(user.getClass()));
        else
            return new RedirectView(referer);
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}
