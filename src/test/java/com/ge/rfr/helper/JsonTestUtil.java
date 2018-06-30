package com.ge.rfr.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.testng.Assert;

import java.io.IOException;
import java.net.URL;

/**
 * This class allows asserting that a given Java object is equal to a reference JSON document
 * which may be stored in the test classpath.
 */
public class JsonTestUtil {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new GuavaModule())
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true);

    public static void assertEquals(Object obj, URL refJson) throws IOException {

        // Deserialize the reference JSON using the given obj class
        Object refObj = mapper.readValue(refJson, obj.getClass());
        // Write it back out (formatted)
        String refString = mapper.writeValueAsString(refObj);

        // Write out the actual obj as string
        String actString = mapper.writeValueAsString(obj);

        Assert.assertEquals(actString, refString);

    }

    public static void assertEquals(Object obj, Object refObj) throws IOException {

        // Write it back out (formatted)
        String refString = mapper.writeValueAsString(refObj);

        // Write out the actual obj as string
        String actString = mapper.writeValueAsString(obj);

        Assert.assertEquals(actString, refString);
    }

    public static <T> String toJson(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String normalizeJson(String json) {
        try {
            return mapper.writeValueAsString(mapper.readTree(json));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(URL refUrl, Class<T> clazz) throws IOException {
        return mapper.readValue(refUrl, clazz);
    }
}
