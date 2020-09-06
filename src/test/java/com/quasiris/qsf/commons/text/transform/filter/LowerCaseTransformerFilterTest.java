package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class LowerCaseTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new LowerCaseTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new LowerCaseTransformerFilter();
        Assert.assertEquals("foo", transformrFilter.transform("Foo"));
    }
}