package org.aaa.core.web.app.http.interceptor;

import org.aaa.core.business.mapping.entity.person.User;
import org.aaa.core.web.app.http.filter.RequestRefererHeaderFilter;
import org.aaa.core.web.app.http.session.Guest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(RequestRefererHeaderFilter.class);

    @Override
    public boolean preHandle(
            HttpServletRequest  request,
            HttpServletResponse response,
            Object              handler
    ) {
        logger.debug(getClass().getName() + "#" + "preHandle");

        HttpSession session;

        session = request.getSession();

        if(session.isNew() || session.getAttribute(User.class.getSimpleName().toLowerCase()) == null)
            session.setAttribute(User.class.getSimpleName().toLowerCase(), new Guest());

        return true;
    }
}