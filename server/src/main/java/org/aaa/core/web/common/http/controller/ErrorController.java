package org.aaa.core.web.common.http.controller;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by alexandremasanes on 13/10/2017.
 */
public interface ErrorController {

    @ModelAttribute
    default void throw404IfNotError(HttpServletResponse response) {
        if(HttpStatus.Series.valueOf(response.getStatus()) == HttpStatus.Series.SUCCESSFUL)
            throw new CustomHttpExceptions.ResourceNotFoundException();
    }
}
