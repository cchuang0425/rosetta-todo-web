package org.rosetta.todoweb.web;

import org.rosetta.todoweb.domain.Login;
import org.rosetta.todoweb.domain.Response;
import org.rosetta.todoweb.domain.Session;
import org.rosetta.todoweb.exception.AuthenticationException;
import org.rosetta.todoweb.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.rosetta.todoweb.config.SystemConfig.RESOURCE_TODO;

@RestController
@RequestMapping(RESOURCE_TODO)
public class AuthAPI {

    public static final String RESOURCE_LOGIN = "/login";
    public static final String RESOURCE_LOGOUT = "/logout";
    public static final String RESOURCE_VERIFY = "/verify";

    @Autowired
    private AuthService service;

    @RequestMapping(value = RESOURCE_LOGIN,
            method = RequestMethod.POST)
    public Response auth(@RequestBody Login login) {
        try {
            var result = service.auth(login);
            return Response.getResponse(true, result);
        } catch (AuthenticationException ex) {
            return Response.getResponse(false, ex.getMessage());
        }
    }

    @RequestMapping(value = RESOURCE_LOGOUT,
            method = RequestMethod.POST)
    public Response logout(@RequestBody Session session) {
        var result = service.logout(session);
        return Response.getResponse(result);
    }

    @RequestMapping(value = RESOURCE_VERIFY,
            method = RequestMethod.POST)
    public Response verify(@RequestBody Session session) {
        var result = service.checkAuth(session);
        return Response.getResponse(result);
    }
}
