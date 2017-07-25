package core.web.common.logic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import util.ForwardingView;
import util.RedirectView;

/**
 * Created by alexandremasanes on 31/05/2017.
 */
public final class CustomHttpExceptions {

    public static abstract class HttpException extends RuntimeException {

        public abstract HttpStatus getHttpStatus();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public static class MethodNotAllowedException extends HttpException {

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.METHOD_NOT_ALLOWED;
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestException extends HttpException {

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class ResourceForbiddenException extends WithAbstractUrlBasedViewException {

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.FORBIDDEN;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends HttpException {

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.NOT_FOUND;
        }
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedRequestException extends WithAbstractUrlBasedViewException {

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class CommandNotValidatedException extends HttpException {

        private Object errors;

        public CommandNotValidatedException(Object errors) {
            this.errors = errors;
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.BAD_REQUEST;
        }

        public Object getErrors() {
            return errors;
        }
    }


    public static abstract class WithAbstractUrlBasedViewException extends HttpException {

        private AbstractUrlBasedView view;

        public WithAbstractUrlBasedViewException withForwarding(String uri) {
            view = new ForwardingView(uri, getHttpStatus());
            return this;
        }

        public WithAbstractUrlBasedViewException withRedirect(String uri) {
            view = new RedirectView(uri, getHttpStatus());
            return this;
        }

        public AbstractUrlBasedView getView() {
            return view;
        }
    }

    CustomHttpExceptions() {

    }
}
