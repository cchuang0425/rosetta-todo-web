package org.rosetta.todoweb.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.domain.Login;
import org.rosetta.todoweb.domain.Session;
import org.rosetta.todoweb.exception.AuthenticationException;
import org.rosetta.todoweb.util.HashUtils;
import org.rosetta.todoweb.util.StringUtils;
import org.rosetta.todoweb.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {

    private static final String TEST_PASSWORD = "0000";

    @Autowired
    private AuthService service;

    private String hashedPwd;
    private String hashedCid;

    @Before
    public void setUp() {
        hashedPwd = HashUtils.hashToSHA256(TEST_PASSWORD);
        hashedCid = TokenUtils.genToken();
    }

    @After
    public void tearDown() { }

    @Test
    public void test$auth() {
        try {
            String token = service.auth(new Login(hashedPwd, hashedCid));

            Assert.assertFalse(StringUtils.isEmpty(token));
            Assert.assertNotEquals(hashedCid, token);
        } catch (AuthenticationException ex) {
            Assert.assertNull(ex);
        }
    }

    @Test
    public void test$checkAuth() throws AuthenticationException {
        var token = service.auth(new Login(hashedPwd, hashedCid));
        var session = new Session(token, hashedCid);
        Assert.assertTrue(service.checkAuth(session));
    }

    @Test
    public void test$logout() throws AuthenticationException {
        var token = service.auth(new Login(hashedPwd, hashedCid));
        Assert.assertTrue(service.logout(new Session(token, hashedCid)));
    }
}
