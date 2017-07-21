package app.core.web.model.databinding.command;

/**
 * Created by alexandremasanes on 22/06/2017.
 */
public class PasswordReinitialization implements Command {

    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean isEmpty() {
        return emailAddress == null || emailAddress.isEmpty();
    }
}
