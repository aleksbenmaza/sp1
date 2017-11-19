package org.aaa.core.web.api.http.controller;

import org.aaa.core.business.mapping.entity.person.Manager;
import org.aaa.core.business.mapping.entity.person.User;
import org.aaa.core.web.api.model.ouput.admin.ManagerDTO;
import org.aaa.core.web.common.business.logic.ManagerService;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandremasanes on 13/11/2017.
 */
@RestController
@RequestMapping("/manager/managers")
public class ManagerApiController extends BaseController {

    @Autowired
    private ManagerService managerService;

    @ModelAttribute
    public Manager manager(
            @RequestHeader("Authorization") String encryptedToken
    ) {
        User user;

        user = tokenService.getGrantedUser(encryptedToken);

        if(user == null)
            throw new CustomHttpExceptions.UnauthorizedRequestException();
        if(!(user instanceof Manager))
            throw new CustomHttpExceptions.ResourceForbiddenException();

        return (Manager)user;
    }

    @ModelAttribute
    public void checkResourceAccess(
            Manager manager,
            @PathVariable(required = false) Long managerId
    ) {
        if(managerId != null && manager.getId() != managerId)
            throw new CustomHttpExceptions.ResourceForbiddenException();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ManagerDTO> getTokenManagers(
            @RequestParam("token_id") long tokenId
    ) {
        List<ManagerDTO> managerDTOs;

        managerDTOs = new ArrayList<>();

        managerDTOs.add(new ManagerDTO(managerService.getByTokenId(tokenId)));

        return managerDTOs;
    }
}
