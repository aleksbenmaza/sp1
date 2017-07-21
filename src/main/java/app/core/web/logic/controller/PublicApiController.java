package app.core.web.logic.controller;


import static app.core.web.model.domaintransfer.publik.InsuranceDTO.fromCollection;

import app.core.web.logic.exception.CustomHttpExceptions.*;
import app.core.web.logic.exception.CustomHttpExceptions.ResourceForbiddenException;
import app.core.web.model.databinding.command.Login;
import app.core.web.model.domaintransfer.publik.InsuranceDTO;
import app.core.business.model.mapping.Token;
import app.core.web.logic.controller.annotation.PreHandler;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@RestController
@RequestMapping("/public")
public class PublicApiController extends ApiController {

    @PreHandler
    public void preHandle(@RequestHeader("Authorization") String token) {
        if(!tokenService.isValid(token))
            throw new UnauthorizedRequestException();
    }

    @PreHandler.Ignored
    @RequestMapping(method = RequestMethod.HEAD)
    public void renewTokenIfExpired(
            @RequestHeader("Authorization") String              tokenValue,
                                            HttpServletResponse response
    ) {
        Token token;

        token = tokenService.replaceIfExpired(tokenValue);
        if(token == null)
            throw new BadRequestException();
        if(token.getOldValue() == null  || !token.getOldValue().equals(tokenValue))
            throw new ResourceForbiddenException();
        response.setHeader("Authorization", token.getValue());
    }

    @RequestMapping(value = "/insurances", method = RequestMethod.GET)
    public List<InsuranceDTO> getInsurances() {
        return fromCollection(apiService.getAllInsurances(), InsuranceDTO::new);
    }

    @RequestMapping(value = "/insurances/{id}/deductible/{amount}", method = RequestMethod.GET)
    public Float getDeductible(@PathVariable long id, @PathVariable float amount) {
        float deductibleValue;

        deductibleValue = apiService.getDeductibleValue(id, amount);

        return deductibleValue;
    }
}