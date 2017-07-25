package core.web.api.logic.controller;

import core.business.logic.ApiService;
import core.business.logic.TokenService;
import core.web.common.logic.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Created by alexandremasanes on 25/03/2017.
 */

public abstract class ApiController extends BaseController {

    @Autowired
    protected ApiService   apiService;

    @Autowired
    protected TokenService tokenService;
}
