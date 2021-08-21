package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class KeepCharTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new KeepCharTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }
    @Test
    public void transformEmpty() {
        TransformerFilter transformrFilter = new KeepCharTransformerFilter();
        Assert.assertEquals("", transformrFilter.transform(""));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new KeepCharTransformerFilter();
        Assert.assertEquals("1", transformrFilter.transform("foo    Bar 1 test"));
    }

}