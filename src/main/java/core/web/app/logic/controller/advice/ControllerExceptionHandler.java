package core.web.app.logic.controller.advice;

import core.web.common.logic.exception.CustomHttpExceptions;
import core.web.common.logic.exception.CustomHttpExceptions.WithAbstractUrlBasedViewException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import util.ForwardingView;
import util.RedirectView;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import util.SpringMessageVars;

import java.util.Locale;

/**
 * Created by alexandremasanes on 22/07/2017.
 */
//@ControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    protected MessageSource messageSource;


    @RequestMapping(method = RequestMethod.GET)
    @ExceptionHandler(Exception.class)
    protected Object handle(Exception exception) {
        System.out.println("handle exc");
        AbstractUrlBasedView view;
        ModelAndView        modelAndView;
        HttpStatus httpStatus;
        int        statusCode;

        if(exception instanceof WithAbstractUrlBasedViewException) {
            view  = ((WithAbstractUrlBasedViewException) exception).getView();
            if(view != null)
                return view;
        }

        if(exception instanceof CustomHttpExceptions.HttpException)
            httpStatus = ((CustomHttpExceptions.HttpException)exception).getHttpStatus();
        else if(exception instanceof NoHandlerFoundException)
            httpStatus = HttpStatus.NOT_FOUND;
        else
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        statusCode = httpStatus.value();
        System.out.println(statusCode);
        if(statusCode < 400
                || messageSource.getMessage("error.message" + statusCode, null, Locale.FRENCH) == null)
            throw new CustomHttpExceptions.ResourceNotFoundException();

        modelAndView = new ModelAndView("error");
        modelAndView.addObject("messageVars", new SpringMessageVars(statusCode));
        modelAndView.addObject("headTitleCode", "error");
        modelAndView.setStatus(httpStatus);
        System.out.println(httpStatus);
        return modelAndView;
    }
}