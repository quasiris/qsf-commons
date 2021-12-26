package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RegexTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new RegexTransformerFilter("");
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new RegexTransformerFilter("\\d+");
        assertEquals("foo", transformrFilter.transform("foo123"));
    }
}