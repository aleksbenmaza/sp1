package org.aaa.core.web.common.business.logic;

import static java.util.UUID.randomUUID;

import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.Token;

import org.aaa.core.web.app.http.session.Guest;
import org.aaa.core.business.mapping.entity.person.User;
import org.aaa.core.web.common.helper.Host;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.sql.Timestamp;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexandremasanes on 29/04/2017.
 */

@Service
public class TokenService extends BaseService {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(TokenService.class.getName());

    private HashMap<String, TokenEncryptionComponents> cache;
    
    {
        cache  = new HashMap<>();
    }

    @Autowired
    private StandardPBEStringEncryptor jasypt;

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
        Token newToken;
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
            uuid = randomUUID();
            newToken = new Token(uuid);
            System.out.println("dao.save -> SO ?");
            dao.remove(token);
            dao.save(newToken);
            tokenEncryptionComponents.uuid = uuid;
            tokenEncryptionComponents.timestamp = token.getUpdateTime();
            cache.remove(encryptedString);
            encryptedString = toEncryptedString(tokenEncryptionComponents);
            cache.put(encryptedString, tokenEncryptionComponents);
        }

        return encryptedString;
    }

    @Transactional
    public String createEncrypted() {
        return createEncrypted(null);
    }

    @Transactional
    public String createEncrypted(UserAccount userAccount) {
        Token                      token;
        Token                      newToken;
        UUID                       uuid;
        TokenEncryptionComponents  tokenEncryptionComponents;
        String                     encryptedToken;

        tokenEncryptionComponents = new TokenEncryptionComponents();

        if(userAccount != null) {
            if((token = userAccount.getToken()) != null && !expired(token)) {
                tokenEncryptionComponents = toTokenEncryptionComponents(token);
                for (Map.Entry<String, TokenEncryptionComponents> entry : cache.entrySet())
                    if (entry.getValue().equals(tokenEncryptionComponents))
                        return entry.getKey();
                encryptedToken = toEncryptedString(tokenEncryptionComponents);
                cache.put(encryptedToken, tokenEncryptionComponents);
                return encryptedToken;
            } else {
                tokenEncryptionComponents.uuid = uuid = randomUUID();
                tokenEncryptionComponents.emailAddress = userAccount.getEmailAddress();
                newToken = new Token(uuid, userAccount);
                dao.save(userAccount);
            }
        } else {
            tokenEncryptionComponents.uuid = uuid = randomUUID();
            newToken = new Token(uuid);
            dao.save(newToken);
        }

        tokenEncryptionComponents.timestamp = newToken.getUpdateTime();

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
        logger.debug("tokenEncryptionComponents not valid !");
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

    private TokenEncryptionComponents toTokenEncryptionComponents(Token token) {
        TokenEncryptionComponents components;

        components = new TokenEncryptionComponents();

        components.uuid = token.getValue();
        components.timestamp = token.getUpdateTime();
        components.emailAddress = token.getUserAccount().getEmailAddress();

        return components;
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
        String[]                  decryptedStrings;
        TokenEncryptionComponents tokenEncryptionComponents;

        decryptedStrings = jasypt.decrypt(encryptedString).split("#");

        tokenEncryptionComponents = new TokenEncryptionComponents();
        try {
            tokenEncryptionComponents.emailAddress = decryptedStrings[1];
            tokenEncryptionComponents.uuid = UUID.fromString(decryptedStrings[0]);
            tokenEncryptionComponents.timestamp = Timestamp.valueOf(decryptedStrings[2]);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
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

    @Scheduled(cron = "0 0 * * * *")
    protected void clearCache() {
        cache.values().removeIf(
                tokenEncryptionComponents -> new Date().compareTo(
                        tokenEncryptionComponents.timestamp) > tokenLifetime
        );
    }

    public boolean deleteToken(long id) {
        return dao.remove(Token.class, id);
    }

    private class TokenEncryptionComponents {
        UUID      uuid;
        String    emailAddress;
        Timestamp timestamp;

        @Override
        public boolean equals(Object o) {
            return o != null
               && (this == o || o instanceof TokenEncryptionComponents && uuid.equals(((TokenEncryptionComponents) o).uuid) && emailAddress.equals(((TokenEncryptionComponents) o).emailAddress) && timestamp.equals(((TokenEncryptionComponents) o).timestamp));
        }
    }
}