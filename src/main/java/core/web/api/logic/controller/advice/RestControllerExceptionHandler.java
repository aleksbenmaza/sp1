package core.web.api.logic.controller.advice;

import core.web.common.logic.exception.CustomHttpExceptions.CommandNotValidatedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by alexandremasanes on 24/03/2017.
 */
@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(Exception exception, HttpServletResponse response) {
        System.out.println("should be called");
        HttpStatus httpStatus;

        exception.printStackTrace();

        if(exception.getClass().isAnnotationPresent(ResponseStatus.class)) {
            httpStatus = exception.getClass().getAnnotation(ResponseStatus.class).value();

            if(exception instanceof CommandNotValidatedException)
                return new ResponseEntity<>(((CommandNotValidatedException)exception).getErrors() , httpStatus);
        }
        else if(exception instanceof NoHandlerFoundException)
            httpStatus = HttpStatus.NOT_FOUND;
        else
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        response.setContentLength(0);

        return new ResponseEntity<>(httpStatus);
    }
}