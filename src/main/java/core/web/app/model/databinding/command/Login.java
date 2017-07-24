package core.web.model.databinding.command;

/**
 * Created by alexandremasanes on 26/02/2017.
 */

public final class Login implements Command {

    protected String emailAddress;

    protected String password;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEmpty() {
        return (emailAddress == null || emailAddress.isEmpty())
                &&
               (password     == null || password.isEmpty());
    }

    @Override
    public String toString() {
        return "Login{" +
                "emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
