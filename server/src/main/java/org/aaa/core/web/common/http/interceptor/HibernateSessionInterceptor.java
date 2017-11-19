package org.aaa.core.web.common.http.interceptor;


import static org.aaa.util.ObjectUtils.doIf;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * Created by alexandremasanes on 09/10/2017.
 */

@Component
public class HibernateSessionInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(HibernateSessionInterceptor.class);

    private HashMap<HttpServletRequest, Session> sessionsRequests;

    @Autowired
    private SessionFactory sessionFactory;

    {
        sessionsRequests = new HashMap<>();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Session session;
        HttpSession httpSession;
        if(handler instanceof HandlerMethod)
            logger.debug(((HandlerMethod) handler).getShortLogMessage());
        session = sessionFactory.openSession();

        sessionsRequests.put(request, session);

        httpSession = request.getSession();

        if(httpSession != null)
            extractEntities(httpSession).forEach((attributeName, entity) -> {
                    httpSession.setAttribute(attributeName, session.get(entity.getClass(), entity.getId()));
            });

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(request.getSession() != null)
            extractEntities(
                    request.getSession()
            ).values()
             .forEach(
                     sessionsRequests.get(request)::detach
             );
        sessionsRequests.remove(request);
        super.postHandle(request, response, handler, modelAndView);
    }

    private Map<String, IdentifiedByIdEntity> extractEntities(HttpSession httpSession) {
        HashMap<String, IdentifiedByIdEntity> entities;
        Enumeration<String>  sessionAttributeNames;
        String sessionAttributeName;
        Object sessionAttribute;

        entities = new HashMap<>();

        sessionAttributeNames = httpSession.getAttributeNames();
        while(sessionAttributeNames.hasMoreElements()) {
            sessionAttributeName = sessionAttributeNames.nextElement();
            sessionAttribute     = httpSession.getAttribute(sessionAttributeName);
            if(sessionAttribute instanceof IdentifiedByIdEntity)
                entities.put(sessionAttributeName, (IdentifiedByIdEntity) sessionAttribute);
        }
        return entities;
    }

    @PreDestroy
    private void destroy() {
        sessionsRequests.values()
                        .stream()
                        .filter(Session::isOpen)
                        .forEach(Session::close);
        doIf(sessionFactory::close, sessionFactory.isOpen());
    }
}