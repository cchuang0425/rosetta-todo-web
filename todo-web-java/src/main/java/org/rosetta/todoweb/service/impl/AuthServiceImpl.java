package org.rosetta.todoweb.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.rosetta.todoweb.domain.Login;
import org.rosetta.todoweb.domain.Session;
import org.rosetta.todoweb.exception.AuthenticationException;
import org.rosetta.todoweb.persistence.InfoDAO;
import org.rosetta.todoweb.service.AuthService;
import org.rosetta.todoweb.util.HashUtils;
import org.rosetta.todoweb.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private Map<String, String> serverTokens;

    public AuthServiceImpl() {
        serverTokens = Collections.synchronizedMap(new HashMap<>());
    }

    @Autowired
    private InfoDAO dao;

    @Override
    public String auth(Login login) throws AuthenticationException {
        var serverPwd = convertPassword(login.getPassword());
        var result = dao.checkPassword(serverPwd);

        if (result) {
            var token = TokenUtils.genToken();
            serverTokens.put(login.getClientId(), token);
            return token;
        } else {
            throw new AuthenticationException("密碼錯誤");
        }
    }

    @Override
    public String convertPassword(String password) {
        return HashUtils.hashToSHA256(password.trim().toUpperCase());
    }

    @Override
    public boolean checkAuth(Session session) {
        return serverTokens.containsKey(session.getClientId())
                && serverTokens.get(session.getClientId()).equals(session.getToken());
    }

    @Override
    public boolean logout(Session session) {
        return checkAuth(session) && serverTokens.remove(session.getClientId()) != null;
    }
}
