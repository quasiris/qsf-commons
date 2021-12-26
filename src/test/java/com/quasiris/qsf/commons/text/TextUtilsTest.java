package com.quasiris.qsf.commons.text;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class TextUtilsTest {

    @Test
    public void testLimitEmpty() {
        String actual = TextUtils.limit("", 100, " ...");
        assertEquals("", actual);
    }

    @Test
    public void testLimitNull() {
        String actual = TextUtils.limit(null, 100, " ...");
        assertNull(actual);
    }

    @Test
    public void testLimitOneWorde() {
        String actual = TextUtils.limit("quasiris", 100, " ...");
        assertEquals("quasiris", actual);
    }

    @Test
    @Disabled // Fix this
    public void testLimit() {
        String actual = TextUtils.limit("quasiris is the best company", 16, " ...");
        assertEquals("quasiris is ...", actual);
    }

    @Test
    @Disabled // Fix this
    public void testLimitExactly() {
        String actual = TextUtils.limit("quasiris is the best company", 15, " ...");
        assertEquals("quasiris is ...", actual);
    }

    @Test
    public void testTrimAll() {
        String actual = TextUtils.trimAll(" foo123 ");
        assertEquals("foo123", actual);
    }

    @Test
    public void testStrip() {
        String actual = TextUtils.strip(" foo ");
        assertEquals("foo", actual);
    }

    @Test
    public void testStripDashPrefix() {
        String actual = TextUtils.strip(" -foo - ");
        assertEquals("foo", actual);
    }

    @Test
    public void testStripDashOnly() {
        String actual = TextUtils.strip("-");
        assertEquals("", actual);
    }



    @Test
    public void testStripStartEmpty() {
        String actual = TextUtils.stripStart("");
        assertEquals("", actual);
    }

    @Test
    public void testStripStartNull() {
        String actual = TextUtils.stripStart(null);
        assertNull(actual);
    }

    @Test
    public void testStripStartDashOnly() {
        String actual = TextUtils.stripStart("-");
        assertEquals("", actual);
    }



    @Test
    public void testStripEnd() {
        String actual = TextUtils.stripEnd(" -foo ");
        assertEquals(" -foo", actual);
    }

    @Test
    public void testStripDashSuffix() {
        String actual = TextUtils.stripEnd(" -foo- ");
        assertEquals(" -foo", actual);
    }

    @Test
    public void testStripEndDashOnly() {
        String actual = TextUtils.stripEnd("-");
        assertEquals("", actual);
    }



    @Test
    public void testStripEndEmpty() {
        String actual = TextUtils.stripEnd("");
        assertEquals("", actual);
    }

    @Test
    public void testStripEndNull() {
        String actual = TextUtils.stripEnd(null);
        assertNull(actual);
    }

    @Test
    public void testStripEndDashSuffix() {
        String actual = TextUtils.stripEnd(" -foo - ");
        assertEquals(" -foo", actual);
    }

    @Test
    public void testReplaceString() {
        String url = "https://${hostname}/";
        Map<String, Object> paremters = new HashMap<>();
        paremters.put("hostname", "www.quasiris.de");
        String actual = TextUtils.replace(url, paremters);
        assertEquals("https://www.quasiris.de/", actual);
    }

    @Test
    public void testReplaceLong() {
        String number = "${number}";
        Map<String, Object> paremters = new HashMap<>();
        paremters.put("number", 42L);
        String actual = TextUtils.replace(number, paremters);
        assertEquals("42", actual);
    }
}
