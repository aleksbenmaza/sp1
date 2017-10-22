package org.aaa.core.web.common.business.logic;

import static org.aaa.util.Hash.*;

import org.aaa.core.business.mapping.ToBeChecked;
import org.aaa.core.business.mapping.UserAccount;
import org.aaa.core.business.mapping.person.Person;
import org.aaa.core.business.mapping.person.RegisteredUser;
import org.aaa.core.business.mapping.person.insuree.Customer;
import org.aaa.core.web.app.model.Login;
import org.aaa.core.web.app.model.Registration;
import org.aaa.core.business.mapping.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public User getUser(long id) {
        return (User) dao.find(Person.class, id);
    }

    @Transactional(readOnly = true)
    public boolean userIsGranted(RegisteredUser user) {
        return user != null
                && (!(user instanceof Customer)
                || ((Customer)user).getStatus() == ToBeChecked.Status.VALID);
    }

    @Transactional(readOnly = true)
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

        loginResult  = new LoginResult();

        if(userAccount == null) {
            loginResult.setMessageCode("notification.badCredentials");
            return loginResult;
        }

        user = userAccount.getId().getUser();
        System.out.println(user);
        ToBeChecked.Status status;

        if(user instanceof Customer
                && (status = ((Customer) user).getStatus()) != null) {

            switch (status) {
                case VALID:
                    loginResult.setRegisteredUser((RegisteredUser) user);
                    return loginResult;

                case AWAITING:
                    if(! ((Customer)user).isSepaDocumentPresent())
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

    @Transactional(readOnly = true)
    public UserAccount getUserAccount(String emailAddress) {
        return dao.findUserAccount(emailAddress);
    }

    @Transactional
    public void resetPassword(
            UserAccount userAccount,
            String password
    ) throws NoSuchAlgorithmException{
        String hash;

        hash = encrypt(userAccount.getEmailAddress() +
                        getHashSalt() +
                        password,
                SHA_1);

        userAccount.setHash(hash);

        dao.save(userAccount);
    }

    @Transactional(readOnly = true)
    public boolean emailAddressExists(String emailAddress) {
        return dao.hasUserAccount(emailAddress);
    }


    @Transactional
    public void createUserAccount(
            Registration registration,
            Customer     customer
    ) throws NoSuchAlgorithmException {
        String hash;

        hash = encrypt(registration.getEmailAddress() +
                        getHashSalt() +
                        registration.getPassword(),
                        SHA_1);

        createUserAccount(customer, registration.getEmailAddress(), hash);
    }

    private <T extends Person & RegisteredUser> void createUserAccount(
            T      registeredUser,
            String emailAddress,
            String hash
    ) {
        UserAccount userAccount;
        userAccount = new UserAccount(new UserAccount.Id(registeredUser));
        userAccount.setEmailAddress(emailAddress);
        userAccount.setHash(hash);
        dao.save(userAccount);
    }
}