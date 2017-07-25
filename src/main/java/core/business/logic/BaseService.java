package core.business.logic;


import static util.CommonUtils.replaceIfNull;

import core.business.model.dao.DAO;
import core.web.common.logic.helper.MessageHelper;

import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    protected DAO dao;

    @Autowired
    private MessageHelper messageHelper;

    private static String hashSalt;

    @PostConstruct
    protected void afterPropertiesSet() {
        hashSalt = replaceIfNull(hashSalt, dao::getHashSalt);
    }

    protected String getHashSalt() {
        return hashSalt;
    }

    protected String getMessage(String code, Object... vars) {
        return messageHelper.getMessage(code, vars);
    }

}
