package org.rosetta.todoweb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
    public static String readTextInClasspath(String fileName) throws IOException {
        try (InputStream is = IOUtils.class.getResourceAsStream(fileName);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(String.format("%s%n", line));
            }

            return sb.toString();
        }
    }
}
