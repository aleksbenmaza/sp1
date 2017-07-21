package app.core.web.logic.controller;

import app.core.business.logic.ApiService;
import app.core.business.logic.TokenService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alexandremasanes on 25/03/2017.
 */
public abstract class ApiController extends BaseController {

    @Autowired
    protected ApiService   apiService;

    @Autowired
    protected TokenService tokenService;
}
