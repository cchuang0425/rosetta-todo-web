package org.rosetta.todoweb.util;

import java.util.UUID;

public class TokenUtils {
    public static String genToken() {
        String uuid = UUID.randomUUID().toString();
        return HashUtils.hashToSHA256(uuid);
    }
}
