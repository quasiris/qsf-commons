package com.quasiris.qsf.commons.util;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
public class ElasticUtilTest {

    @Test
    public void value2FieldNameSlashUmlaut() {

        String fieldName = ElasticUtil.value2FieldName("Farbe / Ausführung");
        assertEquals("farbe___ausfuehrung", fieldName);
    }

    @Test
    public void value2FieldNameEUR() {

        String fieldName = ElasticUtil.value2FieldName("Preis in €");
        assertEquals("preis_in_eur", fieldName);
    }

    @Test
    public void value2FieldNameHiphone() {

        String fieldName = ElasticUtil.value2FieldName("Display-Beleuchtung");
        assertEquals("display_beleuchtung", fieldName);
    }

    @Test
    public void escapeCORONA() throws Exception {
        String value = ElasticUtil.escape("CORONA");
        assertEquals("CORONA", value);
    }


    @Test
    public void escapeCARD() throws Exception {
        String value = ElasticUtil.escape("CANDIDATE");
        assertEquals("CANDIDATE", value);
    }


    @Test
    public void escapeSingleAnd() throws Exception {
        String value = ElasticUtil.escape("foo & bar");
        assertEquals("foo & bar", value);
    }

    @Test
    public void escape() throws Exception {
        String value = ElasticUtil.escape("foo && bar");
        assertEquals("foo \\&& bar", value);
    }

    @Test
    public void escapeORAtTheEnd() throws Exception {
        String value = ElasticUtil.escape("16515 OR");
        assertEquals("16515 OR", value);
    }

    @Test
    public void escapeOR() throws Exception {
        String value = ElasticUtil.escape("16515 OR 0815");
        assertEquals("16515 \\OR 0815", value);
    }

    @Test
    public void parseIndexFromUrl() throws URISyntaxException {
        String expected = "foo";

        String actual = ElasticUtil.parseIndexFromUrl("http://localhost/foo");
        assertEquals(expected, actual);

        actual = ElasticUtil.parseIndexFromUrl("http://localhost/foo/");
        assertEquals(expected, actual);

        actual = ElasticUtil.parseIndexFromUrl("http://localhost/foo/bar");
        assertEquals(expected, actual);

        actual = ElasticUtil.parseIndexFromUrl("http://localhost/foo/bar/");
        assertEquals(expected, actual);

        actual = ElasticUtil.parseIndexFromUrl("http://localhost/foo/bar/?");
        assertEquals(expected, actual);

        actual = ElasticUtil.parseIndexFromUrl("http://localhost/foo,foo2/bar/?");
        assertEquals("foo,foo2", actual);

        actual = ElasticUtil.parseIndexFromUrl("http://localhost/foo,foo2*/bar/?");
        assertEquals("foo,foo2*", actual);
    }
}