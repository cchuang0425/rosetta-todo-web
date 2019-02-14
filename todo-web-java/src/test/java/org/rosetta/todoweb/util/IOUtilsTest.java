package org.rosetta.todoweb.util;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    public static final String TEST_FILE = "test.txt";
    public static final String TEST_TEXT = "test";

    @Test
    public void test$readTextInClasspath() throws IOException {
        String testContent = IOUtils.readTextInClasspath("/" + TEST_FILE);
        Assert.assertTrue(testContent.contains(TEST_TEXT));
    }
}
