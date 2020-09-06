package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class RegexTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new RegexTransformerFilter("");
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new RegexTransformerFilter("\\d+");
        Assert.assertEquals("foo", transformrFilter.transform("foo123"));
    }
}