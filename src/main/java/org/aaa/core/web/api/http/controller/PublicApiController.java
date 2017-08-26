package org.aaa.core.web.api.http.controller;


import static org.aaa.core.web.api.model.ouput.publik.InsuranceDTO.fromCollection;

import org.aaa.core.web.common.business.logic.InsuranceService;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions.*;
import org.aaa.core.web.api.model.ouput.publik.InsuranceDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
@RestController
@RequestMapping("/public")
public class PublicApiController extends BaseController {

    @Autowired
    private InsuranceService insuranceService;

    @ModelAttribute
    public void assertTokenIsValid(@RequestHeader("Authorization") String token) {

        if(!tokenService.isValid(token))
            throw new UnauthorizedRequestException();
    }

    @RequestMapping(value = "/insurances", method = RequestMethod.GET)
    public List<InsuranceDTO> getInsurances() {
        return fromCollection(insuranceService.getInsurances(), InsuranceDTO::new);
    }

    @RequestMapping(value = "/insurances/{id}/deductible/{amount}", method = RequestMethod.GET)
    public Float getDeductible(@PathVariable long id, @PathVariable float amount) {
        float deductibleValue;

        deductibleValue = insuranceService.getDeductibleValue(id, amount);

        return deductibleValue;
    }
}