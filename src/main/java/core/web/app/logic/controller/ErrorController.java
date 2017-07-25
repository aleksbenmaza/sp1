package core.web.app.logic.controller;


import static org.springframework.web.bind.annotation.RequestMethod.*;

import core.web.common.logic.controller.BaseController;
import core.web.common.logic.exception.CustomHttpExceptions.ResourceNotFoundException;

import util.SpringMessageVars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends BaseController {

    @Autowired
    protected MessageSource messageSource;

    @RequestMapping(method = GET)
    public ModelAndView getErrorPage(
            HttpServletRequest  request,
            HttpServletResponse response
    ) {
        System.out.println(request.getServletPath());
        if(request.getServletPath().contains("api"))
            return new ModelAndView("blank");
        int statusCode = response.getStatus();
        if(statusCode < 400
        || messageSource.getMessage("error.message" + statusCode, null, Locale.FRENCH) == null)
            throw new ResourceNotFoundException();
        ModelAndView view = new ModelAndView("error");
        view.addObject("messageVars", new SpringMessageVars(statusCode));
        view.addObject("headTitleCode", "error");
        return view;
    }

    @RequestMapping(method = {POST, PUT, DELETE, HEAD, OPTIONS})
    public void doNothing(HttpServletResponse response) {
        response.setContentLength(0);
    }
}
