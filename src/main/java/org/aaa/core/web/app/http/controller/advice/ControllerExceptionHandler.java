package org.aaa.core.web.app.http.controller.advice;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions.WithAbstractUrlBasedViewException;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by alexandremasanes on 22/07/2017.
 */
@ControllerAdvice(annotations = Controller.class)
public class ControllerExceptionHandler {

    @RequestMapping(method = RequestMethod.GET)
    @ExceptionHandler(WithAbstractUrlBasedViewException.class)
    protected AbstractUrlBasedView handle(WithAbstractUrlBasedViewException exception) {
        AbstractUrlBasedView view;
        view = exception.getView();
        return view;

    }

    @RequestMapping(method = RequestMethod.POST)
    @ExceptionHandler(CustomHttpExceptions.CommandNotValidatedException.class)
    protected ModelAndView handle(CustomHttpExceptions.CommandNotValidatedException exception) {
        return exception.getModelAndView();
    }

    @ExceptionHandler({CustomHttpExceptions.HttpException.class, ServletException.class})
    protected ModelAndView handle(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView;
        HttpStatus status;
        System.out.println("handle");

        switch (HttpMethod.valueOf(request.getMethod())) {
            case GET :
                modelAndView = new ModelAndView("error");
                break;
            default :
                modelAndView = new ModelAndView("blank");
                break;
        }

        modelAndView.setStatus(HttpStatus.valueOf(response.getStatus()));

        return modelAndView;
    }
}