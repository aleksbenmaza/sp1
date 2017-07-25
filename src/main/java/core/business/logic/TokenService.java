package core.business.logic;

import static java.util.UUID.randomUUID;

import core.business.model.mapping.UserAccount;
import core.business.model.mapping.Token;

import core.web.common.Server;
import core.web.app.model.persistence.Guest;
import core.web.app.model.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by alexandremasanes on 29/04/2017.
 */

@Service
public class TokenService extends BaseService {

    private int tokenExpirationTime;

    private HashSet<Token> tokens;

    {
        tokens = new HashSet<>();
    }

    @Autowired
    private Server server;

    @Transactional
    public boolean isValid(String value) {
        Token token;

        token = null;

        for(Token t : tokens)
            if(t != null && t.getValue().equals(value))
                token = t;

        if(token == null)
            token = dao.findToken(value);


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
            token = dao.findToken(value);

        if(token == null)
            return null;

        if(hasExpired(token)) {
            token.setOldValue(value);

            token.setValue(generateValue());

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

    public String mergeKeyAndToken(UserAccount userAccount) {
        char[] chars;
        String apiKey;
        String tokenValue;
        int i;

        apiKey     = userAccount.getApiKey();
        tokenValue = userAccount.getToken().getValue();
        chars      = new char[apiKey.length()];
        i = 0;

        for(char c : chars)
            chars[i++] = (char)(apiKey.charAt(i) & tokenValue.charAt(i));

        return new String(chars);
    }

    public int getTokenExpirationTime() {
        return tokenExpirationTime;
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

    @Override
    @PostConstruct
    protected void afterPropertiesSet() {
        super.afterPropertiesSet();
        tokenExpirationTime = dao.getTokenLifetime();
    }

    private boolean hasExpired(Token token) { System.out.println("has expired " + (new Date().getTime() - token.createdAt().getTime() >= tokenExpirationTime));
        return new Date().getTime() - token.updatedAt().getTime() >= tokenExpirationTime;
    }

    private Token get(String value) {
        Token token; System.out.println(tokens);
        token = tokens.stream()
                      .filter(t -> t.getValue().equals(value))
                      .findFirst()
                      .orElse(null);

        if(token == null)
            token = dao.findToken(value);
        if(token != null)
            tokens.add(token);
        return token;
    }

    private static String generateValue() {
        return randomUUID().toString().replace("-", "");
    }

    public static void main(String... args) {
        System.out.println(generateValue());
    }
}
