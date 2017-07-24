package core.web.logic.controller;

import core.business.logic.ApiService;
import core.business.logic.TokenService;
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
