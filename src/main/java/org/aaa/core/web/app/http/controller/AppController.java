package org.aaa.core.web.app.http.controller;

import org.aaa.core.web.common.business.logic.UserService;
import org.aaa.core.business.mapping.person.Person;
import org.aaa.core.business.mapping.person.RegisteredUser;
import org.aaa.core.web.app.http.session.Guest;
import org.aaa.core.web.common.http.controller.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.InitBinder;
import org.aaa.core.business.mapping.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by alexandremasanes on 20/02/2017.
 */

public abstract class AppController extends BaseController {

    @Value("${session.keys.user}")
    private String sessionUserKey;

    @Autowired
    protected UserService userService;

    public String resolveReferer(String referer) {
        return referer == null || referer.isEmpty() ? getWebroot() : referer;
    }

    @InitBinder
    public void checkSessionUser(HttpSession session) {
        User user;
        System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
        if(session.isNew() || getSessionUser(session) == null) {
            user = new Guest();
            setSessionUser(session, user);
        } else {
            System.out.println(getClass());

            user = getSessionUser(session);

            if (user instanceof Person) {
                user = userService.getUser(((Person) user).getId());

                if (!userService.userIsGranted((RegisteredUser) user)) {
                    user = new Guest();
                    setSessionUser(session, user);
                }
            }
        }
        System.out.println(user);
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

    protected User getSessionUser(HttpSession session) {
        return (User) session.getAttribute(sessionUserKey);
    }

    protected void setSessionUser(HttpSession session, User user) {
        session.setAttribute(sessionUserKey, user);
    }

    protected abstract String getViewName();
}