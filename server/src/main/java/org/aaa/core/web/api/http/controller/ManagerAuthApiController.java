package org.aaa.core.web.api.http.controller;

import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.person.Manager;
import org.aaa.core.business.mapping.entity.person.User;
import org.aaa.core.web.api.model.input.databinding.Login;
import org.aaa.core.web.common.business.logic.TokenService;
import org.aaa.core.web.common.business.logic.UserService;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alexandremasanes on 11/11/2017.
 */
@RestController
@RequestMapping("/manager/tokens")
public class ManagerAuthApiController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ManagerAuthApiController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void post(
            @RequestBody Login               login,
                         HttpServletResponse response
    ) throws NoSuchAlgorithmException {
        logger.debug(login.getEmailAddress() + " " + login.getPassword());
        UserAccount userAccount;

        userAccount = userService.getUserAccount(login.getEmailAddress(), login.getPassword());

        if(userAccount == null || !(userAccount.getId().getUser() instanceof Manager))
            throw new CustomHttpExceptions.BadRequestException();

        setAuthorizationResponseHeader(response, tokenService.createEncrypted(userAccount));
        setLocationResponseHeader(response, userAccount.getToken().getId());
    }
}
