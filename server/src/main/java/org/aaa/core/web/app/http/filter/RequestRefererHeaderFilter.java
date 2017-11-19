package org.aaa.core.web.app.http.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by alexandremasanes on 04/11/2017.
 */

@WebFilter(servletNames = "app")
public class RequestRefererHeaderFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(RequestRefererHeaderFilter.class);

    private final static String HTTP_REFERER_KEY = "Referer";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug(getClass().getName() + "#" + "doFilter");

        HttpServletRequest httpServletRequest;


        httpServletRequest = (HttpServletRequest) servletRequest;

        if (httpServletRequest.getHeader(HTTP_REFERER_KEY) == null || httpServletRequest.getHeader(HTTP_REFERER_KEY).isEmpty()) {
            servletRequest = new FromSessionHttpRefererRequest(httpServletRequest);
        }

        httpServletRequest.getSession().setAttribute(HTTP_REFERER_KEY, null);

        filterChain.doFilter(servletRequest, servletResponse);

        httpServletRequest.getSession().setAttribute(HTTP_REFERER_KEY, httpServletRequest.getRequestURI());
    }

    @Override
    public void destroy() {

    }

    private static class FromSessionHttpRefererRequest extends HttpServletRequestWrapper {
        public FromSessionHttpRefererRequest(HttpServletRequest httpServletRequest) {
            super(httpServletRequest);
        }

        @Override
        public String getHeader(String name) {
            switch (name) {
                case HTTP_REFERER_KEY:
                    if (super.getHeader(name) == null || super.getHeader(name).isEmpty())
                        return getSession().getAttribute(HTTP_REFERER_KEY).toString();
                default:
                    return super.getHeader(name);

            }
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            switch (name) {
                case HTTP_REFERER_KEY:
                    if (super.getHeader(name) == null || super.getHeader(name).isEmpty()) {
                        return Collections.enumeration(
                                Arrays.asList(
                                        (String) getSession()
                                                .getAttribute(HTTP_REFERER_KEY)
                                )
                        );

                    }
                    return super.getHeaders(name);
                default:
                    return super.getHeaders(name);

            }
        }
    }
}