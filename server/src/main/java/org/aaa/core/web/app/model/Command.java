package org.aaa.core.web.app.model;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by alexandremasanes on 05/03/2017.
 */
public abstract class Command implements Serializable {

    private static final long serialVersionUID = 2860899146864140320L;

    private Timestamp securityTimeStamp;

    {
        securityTimeStamp = new Timestamp(System.currentTimeMillis());
    }

    public abstract boolean isEmpty();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Timestamp getSecurityTimeStamp() {
        return securityTimeStamp;
    }

    public void setSecurityTimeStamp(Timestamp securityTimeStamp) {
       if(!this.securityTimeStamp.equals(securityTimeStamp))
           throw new CustomHttpExceptions.BadRequestException();
    }
}