package app.core.web.logic.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import app.core.business.logic.TokenService;
import app.core.business.model.mapping.person.RegisteredUser;
import app.core.business.model.mapping.Token;
import app.core.web.model.persistence.Guest;
import app.core.web.model.persistence.User;
import app.core.web.model.persistence.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.OperationNotSupportedException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexandremasanes on 03/03/2017.
 */
@Controller
@RequestMapping(method = GET)
public class PublicController extends AppController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/")
    public ModelAndView getHomePage(@SessionAttribute User user) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        return render("home", user, map);
    }

    @RequestMapping("/nos-services")
    public ModelAndView getServicesPage(
            @SessionAttribute User user
    ) throws NoSuchAlgorithmException {

        HashMap<String, Object> map = new HashMap<String, Object>();
        String tokenValue;
        Token token;

        tokenValue = user instanceof RegisteredUser ?
                ((RegisteredUser)user).getUserAccount().getToken().getValue() :
                ((Guest)user).getTokenValue();
        if(tokenValue == null) {
            if (user instanceof RegisteredUser)
                token = tokenService.createToken(((RegisteredUser) user).getUserAccount());
            else
                token = tokenService.createToken(null);
            tokenValue = token.getValue();
        }
        map.put("headTitleCode", "services");
        map.put("API_ACCESS_KEY", tokenValue);
        return render("services", user, map);
    }

    @RequestMapping("/a-propos")
    public ModelAndView getAboutPage(@SessionAttribute User user) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("headTitleCode", "about");
        return render("about", map);
    }

    protected ModelAndView render(
            String              viewName,
            User                user,
            Map<String, Object> model
    ) {

        buildUserNavbar(user, model);
        return super.render(viewName, model);
    }

    @Override
    protected String getViewName() {
        throw new RuntimeException(
                PublicController.class.getName() + " handles several views"
        );
    }
}
