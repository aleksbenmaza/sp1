package core.web.logic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class WithViewUnauthorizedRequestException extends WithAbstractUrlBasedViewException {
        public WithViewUnauthorizedRequestException(AbstractUrlBasedView view) {
            super(view);
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class WithViewResourceForbiddenException extends WithAbstractUrlBasedViewException {
        public WithViewResourceForbiddenException(AbstractUrlBasedView view) {
            super(view);
        }
    }

    public static abstract class WithAbstractUrlBasedViewException extends RuntimeException {

        private AbstractUrlBasedView view;

        public WithAbstractUrlBasedViewException(AbstractUrlBasedView view) {
            this.view = view;
        }

        public AbstractUrlBasedView getView() {
            return view;
        }
    }

    CustomHttpExceptions() {

    }
}
