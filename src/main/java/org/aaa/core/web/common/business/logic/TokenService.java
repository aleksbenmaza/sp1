package org.aaa.core.web.common.business.logic;

import static java.util.UUID.randomUUID;

import org.aaa.core.business.mapping.UserAccount;
import org.aaa.core.business.mapping.Token;

import org.aaa.core.web.common.Server;
import org.aaa.core.web.app.http.session.Guest;
import org.aaa.core.business.mapping.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by alexandremasanes on 29/04/2017.
 */

@Service
public class TokenService extends BaseService {

    private HashSet<Token> tokens;

    {
        tokens = new HashSet<>();
    }

    @Autowired
    private Server server;

    @Value("#{@dao.tokenLifetime}")
    private int tokenLifetime;

    @Transactional
    public boolean isValid(String value) {
        Token token;

        token = null;

        for(Token t : tokens)
            if(t != null && t.getValue().equals(value))
                token = t;

        if(token == null) {
            token = dao.findToken(value);
        }
        if(token != null)
            tokens.add(token);
        System.out.println("token is " + token);
        return token != null && !hasExpired(token);
    }

    @Transactional
    public Token replaceIfExpired(String value) {
        Token token;
        UserAccount userAccount;

        token = get(value);

        if(token == null)
            return null;

        if(hasExpired(token)) {
            token.setOldValue(value);

            token.setValue(generateValue());
            System.out.println("dao.save -> SO ?");
            dao.save(token);
        }

        return token;
    }

    @Transactional
    public Token createToken(UserAccount userAccount) {
        Token token;

        token = new Token();

        token.setValue(generateValue());

        token.setApiServer(server.HOST);

        if(userAccount != null) {
            userAccount.setToken(token);
            dao.save(userAccount);
        } else
            dao.save(token);

        tokens.add(token);

        return token;
    }


    public int getTokenLifetime() {
        return tokenLifetime;
    }

    public User getGrantedUser(String tokenValue) {
        UserAccount userAccount;

        if(!isValid(tokenValue))
            return null;

        userAccount = get(tokenValue).getUserAccount();
        System.out.println("toto");
        System.out.println(userAccount);
        return userAccount == null ? new Guest() : userAccount.getUser();
    }

    private boolean hasExpired(Token token) {
        return new Date().getTime() - token.updatedAt().getTime() >= tokenLifetime;
    }

    private Token get(String value) {
        Token token;
        token = tokens.stream()
                      .filter(t -> t.getValue().equals(value))
                      .findFirst()
                      .orElse(null);

        if(token == null) {
            token = dao.findToken(value);

            if (token != null)
                tokens.add(token);
        }
        return token;
    }

    private static String generateValue() {
        return randomUUID().toString().replace("-", "");
    }

    public static void main(String... args) {
        System.out.println(generateValue());
    }
}
