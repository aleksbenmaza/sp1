package org.aaa.core.web.app.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by alexandremasanes on 04/11/2017.
 */
@Controller
@RequestMapping("/manager")
public class ManagerPanelController extends BaseController {

    private final static String VIEW_NAME = "managerpanel";

    @ModelAttribute
    public void beforeHanle(@RequestHeader("Host") String host) {

    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex() {
        return render();
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}