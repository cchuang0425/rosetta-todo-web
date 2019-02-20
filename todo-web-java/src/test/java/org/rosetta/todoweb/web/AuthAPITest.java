package org.rosetta.todoweb.web;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.domain.Login;
import org.rosetta.todoweb.domain.Session;
import org.rosetta.todoweb.service.AuthService;
import org.rosetta.todoweb.util.HashUtils;
import org.rosetta.todoweb.util.JSONUtils;
import org.rosetta.todoweb.util.StringUtils;
import org.rosetta.todoweb.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.rosetta.todoweb.config.SystemConfig.RESOURCE_TODO;
import static org.rosetta.todoweb.web.AuthAPI.RESOURCE_LOGIN;
import static org.rosetta.todoweb.web.AuthAPI.RESOURCE_LOGOUT;
import static org.rosetta.todoweb.web.AuthAPI.RESOURCE_VERIFY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthAPITest {

    private static final String TEST_PASSWORD = "0000";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService service;

    private String hashedPwd;
    private String hashedCid;
    private String token;

    @Before
    public void setUp() {
        hashedPwd = HashUtils.hashToSHA256(TEST_PASSWORD);
        hashedCid = TokenUtils.genToken();
    }

    @After
    public void tearDown() {
        if (!StringUtils.isEmpty(token)) {
            service.logout(new Session(token, hashedCid));
        }
    }

    @Test
    public void test$auth() throws Exception {
        var auth = new Login(hashedPwd, hashedCid);
        var result = mockMvc.perform(
                post(RESOURCE_TODO + RESOURCE_LOGIN)
                        .content(JSONUtils.getJSONFromObject(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                            .andReturn();

        Assert.assertTrue(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                             .getContentAsString())
                                   .get("succeed")
                                   .asBoolean());

        this.token = JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                        .getContentAsString())
                              .get("message")
                              .asText();
    }

    @Test
    public void test$logout() throws Exception {
        var auth = new Login(hashedPwd, hashedCid);
        var token = login(auth);
        var session = new Session(token, auth.getClientId());

        mockMvc.perform(
                post(RESOURCE_TODO + RESOURCE_LOGOUT)
                        .content(JSONUtils.getJSONFromObject(session))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
               .andDo(result ->
                       Assert.assertTrue(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                                            .getContentAsString())
                                                  .get("succeed")
                                                  .asBoolean()));
    }

    private String login(Login login) throws Exception {
        var response = mockMvc.perform(
                post(RESOURCE_TODO + RESOURCE_LOGIN)
                        .content(JSONUtils.getJSONFromObject(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                              .andReturn()
                              .getResponse()
                              .getContentAsString();

        return JSONUtils.getJSONTreeFromRaw(response)
                        .get("message")
                        .asText();
    }

    @Test
    public void test$verify() throws Exception {
        var randomCid = HashUtils.hashToSHA256(UUID.randomUUID().toString());
        var randomToken = HashUtils.hashToSHA256(UUID.randomUUID().toString());
        var badSession = new Session(randomToken, randomCid);

        mockMvc.perform(
                post(RESOURCE_TODO + RESOURCE_VERIFY)
                        .content(JSONUtils.getJSONFromObject(badSession))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
               .andDo(result ->
                       Assert.assertFalse(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                                             .getContentAsString())
                                                   .get("succeed")
                                                   .asBoolean()));

        var login = new Login(hashedPwd, hashedCid);
        var token = login(login);
        var vldSession = new Session(token, hashedCid);

        mockMvc.perform(
                post(RESOURCE_TODO + RESOURCE_VERIFY)
                        .content(JSONUtils.getJSONFromObject(vldSession))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
               .andDo(result ->
                       Assert.assertTrue(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                                            .getContentAsString())
                                                  .get("succeed")
                                                  .asBoolean()));
    }
}
