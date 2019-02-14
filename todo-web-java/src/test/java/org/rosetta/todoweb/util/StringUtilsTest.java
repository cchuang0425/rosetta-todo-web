package org.rosetta.todoweb.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void test$isEmpty() {
        String a = null;
        String b = "";
        String c = "  ";
        String d = "FOO";

        Assert.assertTrue(StringUtils.isEmpty(a));
        Assert.assertTrue(StringUtils.isEmpty(b));
        Assert.assertTrue(StringUtils.isEmpty(c));
        Assert.assertFalse(StringUtils.isEmpty(d));
    }
}
