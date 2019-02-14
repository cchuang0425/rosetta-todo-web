package org.rosetta.todoweb.util;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class SQLUtilsTest {

    private static final String TEST_FILE = "test";
    private static final String TEST_ID = "test";
    private static final String TEST_SQL = "select now();";

    @Test
    public void test$loadSQL() throws IOException {
        String sql = SQLUtils.loadSQL(TEST_FILE, TEST_ID);
        Assert.assertTrue(sql.contains(TEST_SQL));
    }
}
