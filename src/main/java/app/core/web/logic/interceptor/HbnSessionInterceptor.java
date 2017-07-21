package app.core.web.logic.interceptor;

import app.core.business.model.dao.DAO;
import app.util.Hash;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by alexandremasanes on 01/03/2017.
 */

@Component
public class HbnSessionInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //sessionFactory.openSession();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        //sessionFactory.getCurrentSession().close();
    }
}
