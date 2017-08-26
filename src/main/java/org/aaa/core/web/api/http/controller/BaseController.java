package org.aaa.core.web.api.http.controller;

import org.aaa.core.web.common.business.logic.TokenService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alexandremasanes on 25/03/2017.
 */

public abstract class BaseController {

    @Autowired
    protected TokenService tokenService;
}
