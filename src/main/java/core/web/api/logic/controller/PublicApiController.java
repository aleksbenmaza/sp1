package core.web.api.logic.controller;


import static core.web.api.model.ouput.publik.InsuranceDTO.fromCollection;

import core.web.common.logic.exception.CustomHttpExceptions.*;
import core.web.common.logic.exception.CustomHttpExceptions.ResourceForbiddenException;
import core.web.api.model.ouput.publik.InsuranceDTO;
import core.business.model.mapping.Token;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@RestController
@RequestMapping("/public")
public class PublicApiController extends ApiController {

    @ModelAttribute
    public void preHandle(@RequestHeader("Authorization") String token) {

        if(!tokenService.isValid(token))
            throw new UnauthorizedRequestException();
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