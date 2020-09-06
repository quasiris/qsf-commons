package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class RemoveNumberTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new RemoveNumberTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new RemoveNumberTransformerFilter();
        Assert.assertEquals("foo.", transformrFilter.transform("44foo33.00"));
    }

}