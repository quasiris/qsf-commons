package com.quasiris.qsf.commons.util;


import com.quasiris.qsf.dto.common.MultiMap;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    void extractUsernamePassword() throws MalformedURLException {
        String url = "https://admin:B%C3%A4r@example.com/search";
        String usernamePassword = UrlUtil.extractUsernamePassword(url);
        System.out.println(usernamePassword.split(":"));
        assertEquals("admin:Bär", usernamePassword);
    }

    @Test
    void extractUsernamePasswordEmpty() throws MalformedURLException {
        String url = "https://example.com/search";
        String usernamePassword = UrlUtil.extractUsernamePassword(url);
        assertNull(usernamePassword);
    }

    @Test
    void decode() {
        assertEquals("Bär", UrlUtil.decode("B%C3%A4r"));
    }

    @Test
    void buildQuerystring() {
        // given
        MultiMap<String, Object> params = new MultiMap<>();
        params.put("q", "bär");
        params.put("filter", "price:[10,100]");
        params.put("filter", "color:blue");
        params.put("filter", "onStock:true");
        params.put("sort", "price:desc");
        params.put("gerät", "smartphone");

        // when
        String querystring = UrlUtil.buildQuerystring(params);

        // then
        assertEquals("filter=price%3A%5B10%2C100%5D&filter=color%3Ablue&filter=onStock%3Atrue&q=b%C3%A4r&sort=price%3Adesc&ger%C3%A4t=smartphone", querystring);
    }

    @Test
    void appendQuerystring() {
        String querystring = "filter=price%3A%5B10%2C100%5D&ger%C3%A4t=smartphone";
        assertEquals("https://example.com/search?filter=price%3A%5B10%2C100%5D&ger%C3%A4t=smartphone", UrlUtil.appendQuerystring("https://example.com/search", querystring));
        assertEquals("https://example.com/search?filter=price%3A%5B10%2C100%5D&ger%C3%A4t=smartphone", UrlUtil.appendQuerystring("https://example.com/search?", querystring));
        assertEquals("https://example.com/search?page=1&filter=price%3A%5B10%2C100%5D&ger%C3%A4t=smartphone", UrlUtil.appendQuerystring("https://example.com/search?page=1", querystring));
        assertEquals("https://example.com/search?page=1&filter=price%3A%5B10%2C100%5D&ger%C3%A4t=smartphone", UrlUtil.appendQuerystring("https://example.com/search?page=1&", querystring));
    }
}