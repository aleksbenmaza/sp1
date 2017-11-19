package org.aaa.core.web.common.business.logic;

import org.aaa.core.business.mapping.entity.Token;
import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.person.Manager;
import org.aaa.core.business.mapping.entity.person.RegisteredUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by alexandremasanes on 13/11/2017.
 */

@Service
public class ManagerService extends BaseService {

    @Transactional(readOnly = true)
    public Manager getByTokenId(long tokenId) {
        Token token;
        UserAccount userAccount;
        RegisteredUser user;

        token = dao.find(Token.class, tokenId);

        if(token != null) {
            userAccount = token.getUserAccount();

            if(userAccount != null) {
                user = userAccount.getId().getUser();

                if(user != null && user instanceof Manager)
                    return (Manager) user;
            }
        }

        return null;
    }
}
