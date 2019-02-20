package org.rosetta.todoweb.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    /**
     * from <a href="https://dotblogs.com.tw/kevinsrx/2014/03/14/144382">https://dotblogs.com.tw/kevinsrx/2014/03/14/144382</a>
     */
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String hashToSHA256(String txt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(txt.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
    }
}
