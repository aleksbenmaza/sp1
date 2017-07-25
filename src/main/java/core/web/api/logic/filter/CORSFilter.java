package core.web.api.logic.filter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alexandremasanes on 24/07/2017.
 */
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter");
        HttpServletRequest  request;
        HttpServletResponse response;
        String host;

        if(servletResponse instanceof HttpServletResponse
        && servletRequest instanceof HttpServletRequest) {
            request  = (HttpServletRequest) servletRequest;
            if(request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString())) {

                response = (HttpServletResponse) servletResponse;
                host     = ((HttpServletRequest) servletRequest).getHeader("Host");

                response.setHeader("Access-Control-Allow-Origin", host);
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Max-age", "3000");
                response.setHeader("Access-Control-Allow-Headers", "Authorization");
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
