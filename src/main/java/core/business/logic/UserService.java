package core.business.logic;

import static util.Hash.*;
import static java.util.UUID.randomUUID;

import core.business.model.mapping.*;
import core.business.model.mapping.person.Person;
import core.business.model.mapping.person.RegisteredUser;
import core.business.model.mapping.person.insuree.Customer;
import core.web.app.model.databinding.Login;
import core.web.app.model.databinding.Registration;
import core.web.app.model.persistence.User;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * Created by alexandremasanes on 01/03/2017.
 */
@Service
public class UserService extends BaseService {

    public static class LoginResult extends Result {

        private RegisteredUser registeredUser;

        public RegisteredUser getRegisteredUser() {
            return registeredUser;
        }

        public void setRegisteredUser(RegisteredUser registeredUser) {
            this.registeredUser = registeredUser;
        }
    }

    public User getUser(long id) {
        return (User) dao.find(Person.class, id);
    }

    public boolean userIsGranted(RegisteredUser user) {

        boolean bool;

        return user != null
                && (!(user instanceof Customer)
                || ((Customer)user).getStatus() == ToBeChecked.Status.VALID);
    }

    public LoginResult proceedLogin(Login login) throws NoSuchAlgorithmException {
        User        user;
        LoginResult loginResult;
        UserAccount userAccount;
        String      messageCode = null;
        String      toBeHashed;
        String      emailAddress;
        String      password;

        emailAddress = login.getEmailAddress();

        password     = login.getPassword();

        toBeHashed   = emailAddress + getHashSalt() + password;

        userAccount  = dao.findUserAccount(emailAddress, encrypt(toBeHashed, SHA_1));
        System.out.println(userAccount);
        loginResult  = new LoginResult();

        if(userAccount == null) {
            loginResult.setMessageCode("notification.badCredentials");
            return loginResult;
        }


        user = (RegisteredUser)userAccount.getUser();
        ToBeChecked.Status status;

        if(user instanceof Customer
                && (status = ((Customer) user).getStatus()) != null) {

            switch (status) {
                case VALID:
                    loginResult.setRegisteredUser((RegisteredUser) user);
                    return loginResult;

                case AWAITING:
                    if(((Customer)user).getSepa() == null )
                        messageCode = "notification.notYetCheckedAccount";
                    break;

                case INVALID:
                    messageCode = "notification.invalidAccount";
                    break;
            }
            user = null;
        }

        loginResult.setMessageCode(messageCode);
        loginResult.setRegisteredUser((RegisteredUser) user);
        return loginResult;
    }

    public UserAccount getUserAccount(String emailAddress) {
        return dao.findUserAccount(emailAddress);
    }

    public void resetPassword(UserAccount userAccount, String password) throws NoSuchAlgorithmException{
        String hash;

        hash = encrypt(userAccount.getEmail() +
                        getHashSalt() +
                        password,
                SHA_1);

        userAccount.setHash(hash);

        dao.save(userAccount);
    }

    public boolean emailAddressExists(String emailAddress) {
        return dao.emailExists(emailAddress);
    }

    public void createUserAccount(Registration registration, Customer customer) throws NoSuchAlgorithmException {
        String hash;

        hash = encrypt(registration.getEmailAddress() +
                        getHashSalt() +
                        registration.getPassword(),
                        SHA_1);

        createUserAccount(customer, registration.getEmailAddress(), hash);
    }

    private <T extends Person & RegisteredUser> void createUserAccount(T registredUser,
                                                                       String emailAddress,
                                                                       String hash) {
        UserAccount userAccount;
        userAccount = new UserAccount(registredUser);
        userAccount.setApiKey(randomUUID().toString());
        userAccount.setEmail(emailAddress);
        userAccount.setHash(hash);
        dao.save(userAccount);
    }
}