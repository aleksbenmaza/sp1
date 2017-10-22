package org.aaa.core.web.app.http.interceptor;

import org.aaa.core.web.app.http.session.Constants;
import org.aaa.core.web.app.http.session.Guest;
import org.springframework.beans.factory.annotation.Value;
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

        if(session.isNew())
            session.setAttribute(Constants.ATTRIBUTE_USER, new Guest());

        return true;
    }
}