package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class KeepCharTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new KeepCharTransformerFilter();
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transformEmpty() {
        TransformerFilter transformrFilter = new KeepCharTransformerFilter();
        assertEquals("", transformrFilter.transform(""));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new KeepCharTransformerFilter();
        assertEquals("1", transformrFilter.transform("foo    Bar 1 test"));
    }

}