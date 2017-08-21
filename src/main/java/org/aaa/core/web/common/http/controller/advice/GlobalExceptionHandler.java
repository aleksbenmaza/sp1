package org.aaa.core.web.common.http.controller.advice;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Created by alexandremasanes on 25/07/2017.
 */
//@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public void handle() {
        throw new CustomHttpExceptions.ResourceNotFoundException();
    }
}
