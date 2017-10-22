package org.aaa.core.web.common.http.interceptor;

import static org.aaa.util.ObjectUtils.doIf;

import org.aaa.orm.entity.BaseEntity;
import org.hibernate.LockMode;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.function.Consumer;


/**
 * Created by alexandremasanes on 09/10/2017.
 */

@Component
public class HibernateSessionInterceptor extends HandlerInterceptorAdapter {

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
        System.out.println(((HandlerMethod)handler).getShortLogMessage());
        session = sessionFactory.openSession();

        sessionsRequests.put(request, session);

        httpSession = request.getSession();

        if(httpSession != null)
            extractEntities(httpSession).forEach(new Consumer<BaseEntity>() {
                @Override
                public void accept(BaseEntity baseEntity) {
                    session.lock(baseEntity, LockMode.NONE);
                }
        }.andThen(session::refresh));

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        extractEntities(request.getSession()).forEach(sessionsRequests.get(request)::detach);
        sessionsRequests.remove(request);
        super.postHandle(request, response, handler, modelAndView);
    }

    private ArrayList<BaseEntity> extractEntities(HttpSession httpSession) {
        ArrayList<BaseEntity> entities;

        entities = new ArrayList<>();

        Enumeration<String>  sessionAttributeNames;
        Object sessionAttribute;
        sessionAttributeNames = httpSession.getAttributeNames();
        while(sessionAttributeNames.hasMoreElements()) {
            sessionAttribute = httpSession.getAttribute(sessionAttributeNames.nextElement());
            if(sessionAttribute instanceof BaseEntity)
                entities.add((BaseEntity) sessionAttribute);
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
