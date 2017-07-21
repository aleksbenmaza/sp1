package app.core.web.logic.exception;

import app.core.web.model.databinding.validation.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alexandremasanes on 31/05/2017.
 */
public final class CustomHttpExceptions {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestException extends RuntimeException {}

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class ResourceForbiddenException extends RuntimeException {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {}

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedRequestException extends RuntimeException {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class CommandNotValidatedException extends RuntimeException {

        private Object errors;

        public CommandNotValidatedException(Object errors) {
            this.errors = errors;
        }

        public Object getErrors() {
            return errors;
        }
    }

    CustomHttpExceptions() {

    }
}
