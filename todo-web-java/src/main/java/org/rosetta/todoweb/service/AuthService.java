package org.rosetta.todoweb.service;

import org.rosetta.todoweb.domain.Login;
import org.rosetta.todoweb.domain.Session;
import org.rosetta.todoweb.exception.AuthenticationException;

public interface AuthService {
    String auth(Login login) throws AuthenticationException;

    String convertPassword(String password);

    boolean checkAuth(Session session);

    boolean logout(Session session);
}
