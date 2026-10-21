package com.company.framework.utils;

import com.company.framework.exceptions.FrameworkException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading JSON test data files and converting them into Java data structures.
 */
public final class JsonUtils {

    private JsonUtils() {
        // Prevent instantiation
    }

    /**
     * Reads a JSON file from the classpath and deserializes it into a List of Map objects.
     *
     * @param jsonFilePath path to the JSON file relative to classpath resources (e.g. "testdata/login.json")
     * @return List of Map containing key-value pairs per data entry
     */
    public static List<Map<String, String>> getJsonData(String jsonFilePath) {
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(jsonFilePath)) {
            if (is == null) {
                throw new FrameworkException("JSON test data file not found in classpath: " + jsonFilePath);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, new TypeReference<List<Map<String, String>>>() {});
        } catch (Exception e) {
            throw new FrameworkException("Failed to read JSON data from: " + jsonFilePath, e);
        }
    }
}
