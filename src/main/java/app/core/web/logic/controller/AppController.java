package app.core.web.logic.controller;

import app.core.business.logic.UserService;
import app.core.business.model.mapping.IdentifiableById;
import app.core.business.model.mapping.person.Manager;
import app.core.business.model.mapping.person.Person;
import app.core.business.model.mapping.person.RegisteredUser;
import app.core.business.model.mapping.person.insuree.Customer;
import app.core.web.model.persistence.Guest;
import app.core.web.model.persistence.Session;
import app.util.HtmlLink;
import app.core.web.model.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

/**
 * Created by alexandremasanes on 20/02/2017.
 */

public abstract class AppController extends BaseController {

    private static final String USER_SESSION_KEY = "user";

    @Autowired
    protected UserService userService;

    public String resolveReferer(String referer) {
        return referer == null || referer.isEmpty() ? getWebroot() : referer;
    }

    @ModelAttribute
    public void refreshSessionUser(HttpSession session) {
        User user;

        if(session.isNew())
            setSessionUser(session, new Guest());
        else {
            System.out.println(getClass());

            user = getSessionUser(session);


            if (user instanceof Person) {
                user = userService.getUser(((Person) user).getId());

                if (!userService.userIsGranted((RegisteredUser) user))
                    session.invalidate();
                else
                    setSessionUser(session, user);
            }
        }
    }

    protected ModelAndView render(String viewName, Map<String, Object> model) {
        if(model == null)
            model = new ModelMap();

        model.put("WEBROOT", getWebroot());
        model.put("headTitleCode", viewName);

        return new ModelAndView(viewName, model);
    }

    protected ModelAndView render(String viewName){
        return render(viewName, null);
    }

    protected ModelAndView render(Map<String, Object> model) {
        return render(getViewName(), model);
    }

    protected ModelAndView render(){
        return render(getViewName(), null);
    }

    protected void buildUserNavbar(User user, Map<String, Object> model) {
        if(model == null)
            model = new ModelMap();

        HtmlLink link1, link2;
        String fullname;

        link1 = new HtmlLink();
        link2 = new HtmlLink();

        if(user instanceof Customer) {
            Customer customer = (Customer)user;
            fullname = customer.getFirstName() + " " + customer.getLastName() + "   ";
            fullname = fullname.length() > 20 ? fullname.substring(0, 15) + "..." : fullname;
            link1.setName(fullname);
            link1.setUrl(getWebroot() + "/espace-assure");
            link2.setName("Déconnexion");
            link2.setUrl(getWebroot() + "/deconnexion");

        } else if(user instanceof Manager) {
            link1.setName("Manager");
            link1.setUrl(getWebroot() + "/administration");
            link2.setName("Déconnexion");
            link2.setUrl(getWebroot() + "/deconnexion");
        } else {
            link1 = null;
            link2.setName("Connexion/Inscription");
            link2.setUrl(getWebroot() + "/connexion");
        }

        model.put("link1", link1);
        model.put("link2", link2);
    }

    protected User getSessionUser(HttpSession session) {
        return (User) session.getAttribute(USER_SESSION_KEY);
    }

    protected void setSessionUser(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_KEY, user);
    }

    protected abstract String getViewName();
}