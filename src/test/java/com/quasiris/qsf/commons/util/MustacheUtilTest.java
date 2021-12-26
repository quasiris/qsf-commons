package com.quasiris.qsf.commons.util;


import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MustacheUtilTest {
    @Test
    public void compileMustache() {
        // given
        String template = "foo: {{data.foo}}";
        Map<String, String> data = new HashMap<>();
        data.put("data.foo", "bar");

        // when
        String result = MustacheUtil.compileMustache(template, data);

        // then
        assertEquals("foo: bar", result);
    }
}