package com.quasiris.qsf.commons.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.stream.Collectors;


public class JsonUtil {
    private static final ObjectMapper mapper;
    static {{
        mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }}

    public static ObjectMapper defaultMapper() {
        return mapper;
    }

    public static String toJson(Object object) {
        try {
            String pretty = mapper.writeValueAsString(object);
            return pretty;
        } catch (Exception e) {
            throw new RuntimeException("Error during Json serialization!", e);
        }
    }

    public static String toPrettyJson(Object object) {
        try {
            String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            return pretty;
        } catch (Exception e) {
            throw new RuntimeException("Error during Json serialization!", e);
        }
    }

    @Deprecated // remove this
    public static <T> T fromJson(String jsonString) {
        try {
            return mapper.readValue(jsonString, new TypeReference<T>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error during Json de-serialization!", e);
        }
    }

    public static <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error during Json de-serialization!", e);
        }
    }

    public static <T> T fromJson(String jsonString, Class<T> cls) {
        try {
            return mapper.readValue(jsonString, cls);
        } catch (Exception e) {
            throw new RuntimeException("Error during Json de-serialization!", e);
        }
    }

    @Deprecated // use toPrettyJson
    public static String toPrettyString(Object object) {
        return toPrettyJson(object);
    }

    public static JsonNode parseJson(String jsonString) {
        try {
            return mapper.readTree(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated // use parseJson
    public static Object toJson(String jsonString) {
        try {
            return mapper.readTree(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> encode(List<String> values) {
        JsonStringEncoder jsonStringEncoder = JsonStringEncoder.getInstance();

        return values.stream().
                map(v -> new String(jsonStringEncoder.quoteAsString(v))).
                collect(Collectors.toList());

    }

    public static String encode(String value) {
        String result = null;
        if(value != null) {
            JsonStringEncoder jsonStringEncoder = JsonStringEncoder.getInstance();
            result = new String(jsonStringEncoder.quoteAsString(value));
        }
        return result;
    }
}
