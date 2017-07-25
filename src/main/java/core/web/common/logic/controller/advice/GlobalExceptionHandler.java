package core.web.common.logic.controller.advice;

import core.web.common.logic.exception.CustomHttpExceptions;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
