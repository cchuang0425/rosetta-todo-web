package org.rosetta.todoweb.util;

import java.util.Objects;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return Objects.isNull(str) || str.isBlank();
    }
}
