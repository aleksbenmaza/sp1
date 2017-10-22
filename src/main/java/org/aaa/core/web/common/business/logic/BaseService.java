package org.aaa.core.web.common.business.logic;


import static org.aaa.util.ObjectUtils.ifNull;

import org.aaa.core.business.repository.DAO;
import org.aaa.core.web.common.helper.MessageGetter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


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
    private MessageGetter messageHelper;

    @Value("#{@dao.hashSalt}")
    private String hashSalt;


    protected String getHashSalt() {
        return hashSalt;
    }

    protected String getMessage(String code, Object... vars) {
        return messageHelper.get(code, vars);
    }

}
