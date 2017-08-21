package org.aaa.core.web.api.http.controller;


import static org.aaa.core.web.api.model.ouput.publik.InsuranceDTO.fromCollection;

import org.aaa.core.web.common.http.exception.CustomHttpExceptions.*;
import org.aaa.core.web.api.model.ouput.publik.InsuranceDTO;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@RestController
@RequestMapping("/public")
public class PublicApiController extends ApiController {

    @ModelAttribute
    public void assertTokenIsValid(@RequestHeader("Authorization") String token) {

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