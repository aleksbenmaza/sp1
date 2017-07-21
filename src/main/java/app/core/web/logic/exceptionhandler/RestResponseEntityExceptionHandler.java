package app.core.web.logic.exceptionhandler;

import app.core.web.logic.exception.CustomHttpExceptions.CommandNotValidatedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by alexandremasanes on 24/03/2017.
 */
@ControllerAdvice(annotations = RestController.class)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(Exception exception) {

        HttpStatus httpStatus;

        exception.printStackTrace();

        if(exception.getClass().isAnnotationPresent(ResponseStatus.class)) {
            httpStatus = exception.getClass().getAnnotation(ResponseStatus.class).value();

            if(exception instanceof CommandNotValidatedException)
                return new ResponseEntity<>(((CommandNotValidatedException)exception).getErrors() , httpStatus);
        }
        else
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(httpStatus);
    }
}