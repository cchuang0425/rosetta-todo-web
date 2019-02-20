package org.rosetta.todoweb.service.impl;

import org.rosetta.todoweb.exception.AuthenticationException;
import org.rosetta.todoweb.persistence.InfoDAO;
import org.rosetta.todoweb.service.AuthService;
import org.rosetta.todoweb.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private AuthService authService;

    @Autowired
    private InfoDAO dao;

    @Override
    public boolean updatePassword(String newPassword) {
        return dao.updatePassword(authService.convertPassword(newPassword));
    }
}
