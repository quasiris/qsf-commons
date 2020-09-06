package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class TrimAllTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new TrimAllTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new TrimAllTransformerFilter();
        Assert.assertEquals("f_oo", transformrFilter.transform("_f_oo "));
    }
}