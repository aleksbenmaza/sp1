package core.web.logic.controller;


import static org.springframework.web.bind.annotation.RequestMethod.*;

import core.web.logic.exception.CustomHttpExceptions.ResourceNotFoundException;

import util.SpringMessageVars;

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
public class ErrorController extends AppController {

    public final static String VIEW_NAME = "error";

    @Autowired
    protected MessageSource messageSource;

    @RequestMapping(value = "/error", method = GET)
    public ModelAndView getErrorPage(HttpServletResponse response) throws ResourceNotFoundException {
        int statusCode = response.getStatus();
        if(statusCode < 400
        || messageSource.getMessage("error.message" + statusCode, null, Locale.FRENCH) == null)
            throw new ResourceNotFoundException();
        ModelAndView view = render();
        view.addObject("messageVars", new SpringMessageVars(statusCode));
        view.addObject("code", statusCode);
        return view;
    }

    @RequestMapping(value = "/error", method = {POST, PUT, DELETE, HEAD, OPTIONS})
    public void doNothing() {

    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}
