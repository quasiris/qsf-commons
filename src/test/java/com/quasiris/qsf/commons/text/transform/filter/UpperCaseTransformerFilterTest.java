package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class UpperCaseTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new UpperCaseTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new UpperCaseTransformerFilter();
        Assert.assertEquals("FOO", transformrFilter.transform("Foo"));
    }

}