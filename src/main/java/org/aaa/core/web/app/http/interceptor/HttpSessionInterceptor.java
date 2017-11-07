package org.aaa.core.web.app.http.interceptor;

import org.aaa.core.business.mapping.entity.person.User;
import org.aaa.core.web.app.http.session.Guest;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by alexandremasanes on 21/08/2017.
 */

@Component
public class HttpSessionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(
            HttpServletRequest  request,
            HttpServletResponse response,
            Object              handler
    ) {
        HttpSession session;

        session = request.getSession();

        if(session.isNew() || session.getAttribute(User.class.getSimpleName().toLowerCase()) == null)
            session.setAttribute(User.class.getSimpleName().toLowerCase(), new Guest());

        return true;
    }
}