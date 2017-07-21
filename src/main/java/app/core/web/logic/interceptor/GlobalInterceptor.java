package app.core.web.logic.interceptor;

import app.core.business.logic.TokenService;
import app.core.web.logic.controller.annotation.PreHandler;
import app.core.web.logic.exception.CustomHttpExceptions.BadRequestException;
import app.core.web.model.persistence.Guest;
import app.core.web.model.persistence.Session;
import app.util.ForwardView;
import app.util.HandlerDescription;
import app.util.RedirectView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created by alexandremasanes on 03/03/2017.
 */

@Component
public class GlobalInterceptor extends HandlerInterceptorAdapter {

    private Map<Object, HandlerDescription> preHandlers,
                                            postHandlers;

    {
        preHandlers = new HashMap<>();
        preHandlers = new HashMap<>();
    }

    @Autowired
    private TokenService tokenService;

    @Autowired
    public void retrievePreAndPostHandlers(ApplicationContext applicationContext) throws Exception {
        Map<String, Object> beans;
        HandlerDescription handlerDescription;
        String attributeName;

        beans = new HashMap<>();

        beans.putAll(applicationContext.getBeansWithAnnotation(Controller.class));
        beans.putAll(applicationContext.getBeansWithAnnotation(RestController.class));

        for(Object bean : beans.values())
            if(bean.getClass().isAnnotationPresent(Controller.class) || bean.getClass().isAnnotationPresent(RestController.class))
                for(Method method : bean.getClass().getMethods())
                    if (method.isAnnotationPresent(PreHandler.class)) {
                        handlerDescription = new HandlerDescription(method);
                        preHandlers.put(bean, handlerDescription);

                        for(Parameter parameter : method.getParameters())

                                if (parameter.isAnnotationPresent(SessionAttribute.class))
                                    handlerDescription.addAnnotedParameter((attributeName = parameter.getAnnotation(SessionAttribute.class).value()).isEmpty() ? camelCased(parameter.getType().getSimpleName()) : attributeName,
                                            parameter.getAnnotation(SessionAttribute.class));


                                else if (parameter.isAnnotationPresent(ModelAttribute.class))
                                    handlerDescription.addAnnotedParameter((attributeName = parameter.getAnnotation(RequestHeader.class).value()).isEmpty() ? camelCased(parameter.getType().getSimpleName()) : attributeName,
                                            parameter.getAnnotation(ModelAttribute.class));
                                else if (parameter.isAnnotationPresent(RequestHeader.class))
                                    handlerDescription.addAnnotedParameter((attributeName = parameter.getAnnotation(RequestHeader.class).value()).isEmpty() ? camelCased(parameter.getType().getSimpleName()) : attributeName,
                                            parameter.getAnnotation(RequestHeader.class));
                                else
                                    throw new Exception();

                    }
    }

    @Override
    public boolean preHandle(
            HttpServletRequest  request,
            HttpServletResponse response,
            Object              handler
    ) throws Exception {

        Object controller;
        ArrayList<Object> parameters;
        Object preHandleResult;
        HttpSession session;
        HandlerDescription handlerDescription;
        Method preHandler;
        Object paramValue;
        //Session userSession;

        if(((HandlerMethod)handler).getMethod().isAnnotationPresent(PreHandler.Ignored.class))
            return true;

        controller = ((HandlerMethod)handler).getBean();
        session    = request.getSession();

        //if(controller.getClass().isAnnotationPresent(Controller.class))
         /*  if(request.getSession().getAttribute("session") == null) {
                session.setAttribute("session", userSession = new Session());
                ((Guest) userSession.getUser()).setToken(tokenService.createToken(null));
            } */


        parameters         = new ArrayList<>();
        handlerDescription = preHandlers.get(controller);

        if(handlerDescription == null)
            return true;

        preHandler = handlerDescription.getMethod();

        for(Map.Entry<String, Annotation> annotedParameter : handlerDescription.getAnnotedParameters())
            if(annotedParameter.getValue().annotationType().equals(SessionAttribute.class))
                parameters.add(session.getAttribute(annotedParameter.getKey()));
            else if(annotedParameter.getValue().annotationType().equals(RequestHeader.class))
                if((paramValue = request.getHeader(annotedParameter.getKey())) == null && ((RequestHeader)annotedParameter.getValue()).required())
                    throw new BadRequestException();
                else
                    parameters.add(paramValue);
            else if(annotedParameter.getValue().annotationType().equals(ModelAttribute.class))
                parameters.add(request.getAttribute(annotedParameter.getKey()));
        System.out.println(parameters);
        if (preHandler.getReturnType() != Void.TYPE) {
                preHandleResult = null;
            try {
                preHandleResult = preHandler.invoke(controller, parameters.toArray());
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
            }

            if (preHandleResult != null) {
                if (preHandleResult instanceof RedirectView) {
                    RedirectView redirectView = (RedirectView)preHandleResult;
                    response.sendRedirect(redirectView.getUrl());
                    response.setStatus(redirectView.getHttpStatus().value());

                } else if(preHandleResult instanceof ForwardView) {
                    ForwardView forwardView = (ForwardView)preHandleResult;
                    response.setStatus(forwardView.getHttpStatus().value());
                    request.getRequestDispatcher(forwardView.getUrl()).forward(request, response);

                } else if (preHandleResult instanceof ModelAndView)
                    request.getRequestDispatcher(((ModelAndView) preHandleResult).getViewName())
                           .forward(request, response);

                else if (preHandleResult instanceof String
                        && !((String) preHandleResult).startsWith("redirect:")
                        && !((String) preHandleResult).startsWith("forward:"))
                    request.getRequestDispatcher(((String) preHandleResult))
                           .forward(request, response);
                else
                    throw new Exception("PreHandler return type not supported !");
                return false;
            }
            return true;
        } else
        try {
            preHandler.invoke(controller, parameters.toArray());
        } catch (InvocationTargetException e) {
            if(e.getCause() instanceof Exception)
                throw (Exception) e.getCause();
        }

        return true;
    }

    private String camelCased(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

}
