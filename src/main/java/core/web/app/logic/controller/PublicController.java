package core.web.app.logic.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import core.business.logic.TokenService;
import core.business.model.mapping.person.RegisteredUser;
import core.business.model.mapping.Token;
import core.web.app.model.persistence.Guest;
import core.web.app.model.persistence.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexandremasanes on 03/03/2017.
 */
@Controller
@RequestMapping(method = GET)
public class PublicController extends AppController {

    @Autowired
    private TokenService tokenService;

    @Value("${apiSubdomain}")
    private String apiSubdomain;

    @RequestMapping("${routes.public.home}")
    public ModelAndView getHomePage(@SessionAttribute User user) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        return render("home", user, map);
    }

    @RequestMapping("${routes.public.services}")
    public ModelAndView getServicesPage(@SessionAttribute User user)
            throws NoSuchAlgorithmException {

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
        map.put("API_SUBDOMAIN", apiSubdomain);
        return render("services", user, map);
    }

    @RequestMapping("${routes.public.about}")
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