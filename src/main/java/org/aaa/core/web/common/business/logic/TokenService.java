package org.aaa.core.web.common.business.logic;

import static java.util.UUID.randomUUID;

import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.Token;

import org.aaa.core.web.app.http.session.Guest;
import org.aaa.core.business.mapping.entity.User;
import org.aaa.core.web.common.helper.Host;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by alexandremasanes on 29/04/2017.
 */

@Service
public class TokenService extends BaseService {

    private HashMap<String, TokenEncryptionComponents> cache;

    @Autowired
    private StandardPBEStringEncryptor jasypt;

    {
        cache  = new HashMap<>();
    }

    @Value("#{@dao.tokenLifetime}")
    private int tokenLifetime;

    @Autowired
    private Host host;

    @Transactional(readOnly = true)
    public boolean valid(String encryptedString) {
        return valid(cache.computeIfAbsent(encryptedString, this::fromEncryptedString));
    }

    @Transactional
    public String replaceIfExpired(String encryptedString) {
        Token token;
        UUID uuid;
        TokenEncryptionComponents tokenEncryptionComponents;

        tokenEncryptionComponents = cache.computeIfAbsent(
                encryptedString,
                this::fromEncryptedString
        );

        if(tokenEncryptionComponents == null)
            return null;

        token = get(tokenEncryptionComponents.uuid);

        if(token == null)
            return null;

        if(expired(token)) {
            token.setOldValue(tokenEncryptionComponents.uuid);
            uuid = randomUUID();
            token.setValue(uuid);
            System.out.println("dao.save -> SO ?");
            dao.save(token);
            tokenEncryptionComponents.uuid = uuid;
            tokenEncryptionComponents.timestamp = token.getUpdateTime();
            cache.remove(encryptedString);
            encryptedString = toEncryptedString(tokenEncryptionComponents);
            cache.put(encryptedString, tokenEncryptionComponents);
        }

        return encryptedString;
    }

    @Transactional
    public String createToken() {
        return createToken(null);
    }

    @Transactional
    public String createToken(UserAccount userAccount) {
        Token token;
        UUID uuid;
        TokenEncryptionComponents tokenEncryptionComponents;
        String encryptedToken;

        tokenEncryptionComponents = new TokenEncryptionComponents();

        tokenEncryptionComponents.uuid = uuid = randomUUID();

        if(userAccount != null) {
            token = userAccount.getToken();
            tokenEncryptionComponents.emailAddress = userAccount.getEmailAddress();
            if(token != null) {
                token.setOldValue(token.getValue());
                token.setValue(uuid);
            } else {
                token = new Token();
                token.setValue(uuid);
                userAccount.setToken(token);
                dao.save(token);
            }
        } else {
            token = new Token();
            token.setValue(uuid);
            dao.save(token);
        }

        tokenEncryptionComponents.timestamp = token.getUpdateTime();

        encryptedToken = toEncryptedString(tokenEncryptionComponents);

        cache.put(encryptedToken, tokenEncryptionComponents);

        return encryptedToken;
    }

    @Transactional(readOnly = true)
    public User getGrantedUser(String encryptedString) {
        UserAccount userAccount;
        TokenEncryptionComponents tokenEncryptionComponents;
        tokenEncryptionComponents = cache.computeIfAbsent(encryptedString, this::fromEncryptedString);
        if(!valid(tokenEncryptionComponents))
            return null;

        userAccount = get(tokenEncryptionComponents.uuid).getUserAccount();
        return userAccount == null ? new Guest() : userAccount.getId().getUser();
    }

    private boolean expired(TokenEncryptionComponents tokenEncryptionComponents) {
        return new Date().compareTo(tokenEncryptionComponents.timestamp) >= tokenLifetime;
    }

    private boolean expired(Token token) {
        return new Date().compareTo(token.getUpdateTime()) >= tokenLifetime;
    }

    @Transactional(readOnly = true)
    private Token get(UUID tokenValue) {
        return dao.findToken(tokenValue);
    }

    private String toEncryptedString(TokenEncryptionComponents tokenEncryptionComponents) {
        String key;
        key = StringUtils.join(
                new Object[] {
                        tokenEncryptionComponents.uuid,
                        tokenEncryptionComponents.emailAddress,
                        tokenEncryptionComponents.timestamp
                },
                '#'
        );

        // this is the authentication token user will send in order to use the web service
        return jasypt.encrypt(key);
    }

    private TokenEncryptionComponents fromEncryptedString(String encryptedString) {
        String[]             decryptedStrings;
        TokenEncryptionComponents tokenEncryptionComponents;

        decryptedStrings = jasypt.decrypt(encryptedString).split("#");

        tokenEncryptionComponents = new TokenEncryptionComponents();
        tokenEncryptionComponents.emailAddress = decryptedStrings[1];
        try {
            tokenEncryptionComponents.uuid = UUID.fromString(decryptedStrings[0]);
            tokenEncryptionComponents.timestamp = Timestamp.valueOf(decryptedStrings[2]);
        } catch (IllegalArgumentException e) {
            tokenEncryptionComponents = null;
        }

        return tokenEncryptionComponents;
    }

    @Transactional(readOnly = true)
    private boolean valid(TokenEncryptionComponents tokenEncryptionComponents) {
        Token token;

        token = get(tokenEncryptionComponents.uuid);

        System.out.println("token is " + token);
        return token != null && !expired(token);
    }

    private class TokenEncryptionComponents {
        UUID      uuid;
        String    emailAddress;
        Timestamp timestamp;
    }
}
