package core.web.app.model.persistence;

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
