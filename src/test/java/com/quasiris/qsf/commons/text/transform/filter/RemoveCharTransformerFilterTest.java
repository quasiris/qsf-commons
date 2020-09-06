package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class RemoveCharTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new RemoveCharTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new RemoveCharTransformerFilter();
        Assert.assertEquals("foo", transformrFilter.transform("_f_oo_"));
    }

}