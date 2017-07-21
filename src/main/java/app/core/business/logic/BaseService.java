package app.core.business.logic;


import app.core.business.model.dao.DAO;
import app.core.web.logic.helper.MessageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import javax.annotation.PostConstruct;


/**
 * Created by alexandremasanes on 26/02/2017.
 */
public abstract class BaseService {

    public static abstract class Result {

        private String messageCode;

        public String getMessageCode() {
            return messageCode;
        }

        public void setMessageCode(String messageCode) {
            this.messageCode = messageCode;
        }
    }

    @Qualifier("dao")
    @Autowired
    protected DAO dao;

    @Autowired
    private MessageHelper messageHelper;

    private static String hashSalt;

    @PostConstruct
    protected void afterPropertiesSet() {
        if(hashSalt == null)
            hashSalt = dao.getHashSalt();
    }

    protected String getHashSalt() {
        return hashSalt;
    }

    protected String getMessage(String code, Object... vars) {
        return messageHelper.getMessage(code, vars);
    }

}
