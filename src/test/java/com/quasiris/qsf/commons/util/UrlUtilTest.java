package com.quasiris.qsf.commons.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UrlUtilTest {

    @Test
    public void removePassword() {
        String url = UrlUtil.removePassword("https://myUser:myPassword@elastic.quasiris.de:9200/product-search");
        assertEquals("https://elastic.quasiris.de:9200/product-search", url);
    }
    @Test
    public void removePasswordFromUrlWithoutPassword() {
        String url = UrlUtil.removePassword("https://elastic.quasiris.de:9200/product-search");
        assertEquals("https://elastic.quasiris.de:9200/product-search", url);
    }


    @Test
    public void encodeMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Bär");
        values.put("number", 1);

        Map<String, Object> valuesEncoded = UrlUtil.encode(values);
        assertEquals(2, valuesEncoded.size());
        assertEquals("B%C3%A4r", valuesEncoded.get("name"));
        assertEquals(1, valuesEncoded.get("number"));
    }
    @Test
    public void encodeMapWithSuffix() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Bär");
        values.put("number", 1);

        Map<String, Object> valuesEncoded = UrlUtil.encode(values, ".encoded");
        assertEquals(4, valuesEncoded.size());
        assertEquals("Bär", valuesEncoded.get("name"));
        assertEquals("B%C3%A4r", valuesEncoded.get("name.encoded"));
        assertEquals(1, valuesEncoded.get("number"));
        assertEquals(1, valuesEncoded.get("number.encoded"));
    }
}