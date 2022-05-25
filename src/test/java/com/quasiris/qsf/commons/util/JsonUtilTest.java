package com.quasiris.qsf.commons.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest {

    @Test
    void toJson() {
        // given
        Map<String, Object> doc = createDoc();

        // when
        String json = JsonUtil.toJson(doc);

        // then
        assertEquals("{\"price\":123.95,\"onStock\":true,\"title\":\"The Title\"}", json);
    }

    @Test
    void toPrettyJson() {
        // given
        Map<String, Object> doc = createDoc();

        // when
        String json = JsonUtil.toPrettyJson(doc);

        // then
        assertEquals("{\n" +
                "  \"price\" : 123.95,\n" +
                "  \"onStock\" : true,\n" +
                "  \"title\" : \"The Title\"\n" +
                "}", json);
    }

    @Test
    void fromJson() {
        // given
        String json = "{\"price\":123.95,\"onStock\":true,\"title\":\"The Title\"}";
        String prettyJson = "{\n" +
                "  \"price\" : 123.95,\n" +
                "  \"onStock\" : true,\n" +
                "  \"title\" : \"The Title\"\n" +
                "}";
        Map<String, Object> expectedDoc = createDoc();

        // when
        Map<String, Object> objFromJson = JsonUtil.fromJson(json, Map.class);
        Object objFromPrettyJson = JsonUtil.fromJson(prettyJson, Object.class);

        // then
        assertEquals(expectedDoc, objFromJson);
        assertEquals(expectedDoc, objFromPrettyJson);
    }

    private static Map<String, Object> createDoc() {
        Map<String, Object> doc = new HashMap<>();
        doc.put("title", "The Title");
        doc.put("price", 123.95);
        doc.put("onStock", true);

        return doc;
    }
}