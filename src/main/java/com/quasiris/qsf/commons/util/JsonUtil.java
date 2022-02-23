package com.quasiris.qsf.commons.util;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;


public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            String pretty = mapper.writeValueAsString(object);
            return pretty;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPrettyJson(Object object) {
        try {
            String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            return pretty;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String jsonString) {
        try {
            return mapper.readValue(jsonString, new TypeReference<T>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated // use toPrettyJson
    public static String toPrettyString(Object object) {
        return toPrettyJson(object);
    }

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
