package org.rosetta.todoweb.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rosetta.todoweb.exception.SystemException;

public class JSONUtils {
    public static ObjectMapper getJSONMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    public static String getJSONFromObject(Object obj) {
        var mapper = getJSONMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace(System.err);
            return "";
        }
    }

    public static <T> T getObjectFromJSON(String json, Class<T> claz) {
        var mapper = getJSONMapper();
        try {
            return mapper.readValue(json, claz);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new SystemException(ex);
        }
    }

    public static JsonNode getJSONTreeFromRaw(String raw) {
        var mapper = getJSONMapper();
        try {
            return mapper.readTree(raw);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new SystemException(ex);
        }
    }
}
