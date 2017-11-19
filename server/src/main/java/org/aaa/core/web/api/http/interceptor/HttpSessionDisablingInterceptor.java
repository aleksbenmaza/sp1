package org.aaa.core.web.api.http.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by alexandremasanes on 10/10/2017.
 */

@Component
public class HttpSessionDisablingInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(new SessionlessRequest(request), response, handler);
    }

    private static class SessionlessRequest extends HttpServletRequestWrapper {

        private SessionlessRequest(HttpServletRequest request) {
            super(request);
            if(request.getSession() != null)
                request.getSession().invalidate();
        }

        @Override
        public HttpSession getSession() {
            return null;
        }

        @Override
        public HttpSession getSession(boolean create) {
            return null;
        }
    }
}