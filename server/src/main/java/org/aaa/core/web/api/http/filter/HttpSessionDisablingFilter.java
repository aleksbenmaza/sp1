package org.aaa.core.web.api.http.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by alexandremasanes on 04/11/2017.
 */
@WebFilter(servletNames = "api")
public class HttpSessionDisablingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(HttpSessionDisablingFilter.class);

    private static class SessionlessRequest extends HttpServletRequestWrapper {

        private SessionlessRequest(HttpServletRequest request) {
            super(request);
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug(getClass().getName() + "#" + "doFilter");
        filterChain.doFilter(
                new SessionlessRequest(
                        (HttpServletRequest) servletRequest
                ),
                servletResponse
        );
    }

    @Override
    public void destroy() {}
}
