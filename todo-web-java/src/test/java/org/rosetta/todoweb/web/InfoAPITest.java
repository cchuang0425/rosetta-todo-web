package org.rosetta.todoweb.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.domain.Login;
import org.rosetta.todoweb.domain.NewPassword;
import org.rosetta.todoweb.domain.Session;
import org.rosetta.todoweb.service.InfoService;
import org.rosetta.todoweb.util.HashUtils;
import org.rosetta.todoweb.util.JSONUtils;
import org.rosetta.todoweb.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.rosetta.todoweb.config.SystemConfig.RESOURCE_TODO;
import static org.rosetta.todoweb.web.AuthAPI.RESOURCE_LOGIN;
import static org.rosetta.todoweb.web.InfoAPI.RESOURCE_INFO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InfoAPITest {
    private static final String TEST_PASSWORD = "0000";
    private static final String TEST_NEW_PASSWORD = "1111";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InfoService service;

    private String hashedPwd;
    private String hashedCid;
    private Session session;

    @Before
    public void setUp() {
        hashedPwd = HashUtils.hashToSHA256(TEST_PASSWORD);
        hashedCid = TokenUtils.genToken();
    }

    @After
    public void tearDown() {
        service.updatePassword(TEST_PASSWORD);
    }

    @Test
    public void test$setPassword() throws Exception {
        this.session = login(TEST_PASSWORD);

        var hashedNewPwd = HashUtils.hashToSHA256(TEST_NEW_PASSWORD);
        var newPwd = new NewPassword(session, hashedNewPwd);
        mockMvc.perform(
                post(RESOURCE_TODO + RESOURCE_INFO).content(JSONUtils.getJSONFromObject(newPwd))
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .accept(MediaType.APPLICATION_JSON))
               .andDo(result -> Assert.assertTrue(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                                                     .getContentAsString())
                                                           .get("succeed")
                                                           .asBoolean()));
    }

    private Session login(String password) throws Exception {
        var auth = new Login(hashedPwd, hashedCid);
        var raw = mockMvc.perform(
                post(RESOURCE_TODO + RESOURCE_LOGIN).content(JSONUtils.getJSONFromObject(auth))
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .accept(MediaType.APPLICATION_JSON))
                         .andReturn()
                         .getResponse()
                         .getContentAsString();
        var json = JSONUtils.getJSONTreeFromRaw(raw);
        return new Session(json.get("message").asText(), hashedCid);
    }
}
