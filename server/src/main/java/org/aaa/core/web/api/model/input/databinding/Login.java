package org.aaa.core.web.api.model.input.databinding;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by alexandremasanes on 11/11/2017.
 */
public class Login implements Serializable {

    private static final long serialVersionUID = -1605460491273022481L;

    @SerializedName("email_address")
    private String emailAddress;

    private String password;

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
