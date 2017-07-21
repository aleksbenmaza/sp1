package app.core.web.logic.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alexandremasanes on 02/03/2017.
 */
//@WebFilter(urlPatterns = {"/espace-assure/"})
public class CustomerPanelFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).sendRedirect("/");

    }

    @Override
    public void destroy() {

    }
}
