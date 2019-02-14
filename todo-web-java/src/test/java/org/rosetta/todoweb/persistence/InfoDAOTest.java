package org.rosetta.todoweb.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfoDAOTest {

    private static final String TEST_PASSWORD = "password";

    @Autowired
    private InfoDAO dao;

    @Test
    public void test$updatePassword() {
        Assert.assertTrue(dao.updatePassword(TEST_PASSWORD));
    }

    @Test
    public void test$checkPassword() {
        Assert.assertFalse(dao.checkPassword(TEST_PASSWORD));
        dao.updatePassword(TEST_PASSWORD);
        Assert.assertTrue(dao.checkPassword(TEST_PASSWORD));
    }
}
