package org.rosetta.todoweb.web;

import org.rosetta.todoweb.domain.NewPassword;
import org.rosetta.todoweb.domain.Response;
import org.rosetta.todoweb.service.AuthService;
import org.rosetta.todoweb.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.rosetta.todoweb.config.SystemConfig.RESOURCE_TODO;

@RestController
@RequestMapping(RESOURCE_TODO)
public class InfoAPI {
    public static final String RESOURCE_INFO = "/info";

    @Autowired
    private AuthService authService;

    @Autowired
    private InfoService infoService;

    @RequestMapping(value = RESOURCE_INFO,
            method = RequestMethod.POST)
    public Response setPassword(@RequestBody NewPassword newPwd) {
        var result = authService.checkAuth(newPwd.getSession())
                && infoService.updatePassword(newPwd.getPassword());

        return Response.getResponse(result);
    }
}
