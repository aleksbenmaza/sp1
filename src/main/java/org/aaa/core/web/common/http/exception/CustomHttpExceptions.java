package org.aaa.core.web.common.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.aaa.util.ForwardingView;
import org.aaa.util.RedirectView;

/**
 * Created by alexandremasanes on 31/05/2017.
 */
public final class CustomHttpExceptions {

    public static abstract class HttpException extends RuntimeException {

        public abstract HttpStatus getHttpStatus();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public static class MethodNotAllowedException extends RuntimeException {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestException extends RuntimeException {}

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class ResourceForbiddenException extends WithAbstractUrlBasedViewException {

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.FORBIDDEN;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {}

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

        private ModelAndView modelAndView;

        public CommandNotValidatedException() {}

        public CommandNotValidatedException(Object errors) {
            this.errors = errors;
        }

        public CommandNotValidatedException withView(String viewName) {
            modelAndView = new ModelAndView(viewName);
            return this;
        }

        public Object getErrors() {
            return errors;
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.BAD_REQUEST;
        }

        public ModelAndView getModelAndView() {
            return modelAndView;
        }
    }


    public static abstract class WithAbstractUrlBasedViewException extends HttpException {

        private AbstractUrlBasedView view;

        public WithAbstractUrlBasedViewException withForwarding(String uri) {
            view = new ForwardingView(uri);
            ((ForwardingView)view).setHttpStatus(getHttpStatus());
            return this;
        }

        public WithAbstractUrlBasedViewException withRedirect(String uri) {
            view = new RedirectView(uri);
            ((RedirectView)view).setStatusCode(getHttpStatus());
            return this;
        }

        public AbstractUrlBasedView getView() {
            return view;
        }
    }

    CustomHttpExceptions() {

    }
}
