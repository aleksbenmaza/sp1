package org.aaa.core.web.app.http.session;

import org.aaa.core.business.mapping.User;

/**
 * Created by alexandremasanes on 26/02/2017.
 */

public class Guest implements User {

    private String tokenValue;

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
