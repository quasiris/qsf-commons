package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class TrimTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new TrimTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new TrimTransformerFilter();
        Assert.assertEquals("foo", transformrFilter.transform("foo "));
    }
}