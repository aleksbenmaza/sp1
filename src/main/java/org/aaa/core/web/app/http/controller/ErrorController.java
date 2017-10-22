package org.aaa.core.web.app.http.controller;


import static org.springframework.web.bind.annotation.RequestMethod.*;

import static org.aaa.core.web.app.http.controller.ErrorController.VIEW_NAME;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions.ResourceNotFoundException;

import org.aaa.util.SpringMessageVars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@Controller
@RequestMapping("/" + VIEW_NAME)
public class ErrorController extends BaseController implements org.aaa.core.web.common.http.controller.ErrorController {

    public final static String VIEW_NAME = "error";

    @Autowired
    protected MessageSource messageSource;

    @RequestMapping(method = GET)
    public ModelAndView getErrorPage(HttpServletResponse response) {
        int statusCode = response.getStatus();
        if(messageSource.getMessage("error.message" + statusCode, null, Locale.FRENCH) == null)
            throw new ResourceNotFoundException();
        ModelAndView view = render();
        view.addObject("messageVars", new SpringMessageVars(statusCode));
        view.addObject("code", statusCode);
        return view;
    }

    @RequestMapping(method = {POST, PUT, DELETE, HEAD, OPTIONS})
    public void doNothing() {
        System.out.println("ErrorController.doNothing()");
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}
