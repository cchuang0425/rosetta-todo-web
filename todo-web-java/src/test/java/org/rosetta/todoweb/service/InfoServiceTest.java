package org.rosetta.todoweb.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfoServiceTest {

    private static final String TEST_NEW_PWD = "1111";

    @Autowired
    private InfoService service;

    private String hashedNew;

    @Before
    public void setUp() {
        hashedNew = HashUtils.hashToSHA256(TEST_NEW_PWD);
    }

    @Test
    public void test$updatePassword() {
        Assert.assertTrue(service.updatePassword(hashedNew));
    }
}
