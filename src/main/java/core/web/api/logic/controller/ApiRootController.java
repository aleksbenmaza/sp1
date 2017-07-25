package core.web.api.logic.controller;

import core.business.model.mapping.Token;
import core.web.common.logic.exception.CustomHttpExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by alexandremasanes on 23/07/2017.
 */
@RestController
@ResponseStatus(HttpStatus.NO_CONTENT)
public class ApiRootController extends ApiController {


    @RequestMapping(value = "/", method = RequestMethod.HEAD)
    public void renewTokenIfExpired(
            @RequestHeader("Authorization") String              tokenValue,
                                            HttpServletResponse response
    ) {
        Token token;

        token = tokenService.replaceIfExpired(tokenValue);
        if(token == null)
            throw new CustomHttpExceptions.BadRequestException();
        if(token.getOldValue() == null  || !token.getOldValue().equals(tokenValue))
            throw new CustomHttpExceptions.ResourceForbiddenException();
        response.setHeader("Authorization", token.getValue());
    }
}