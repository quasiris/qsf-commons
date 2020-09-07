package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class StemmingTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new StemmingTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new StemmingTransformerFilter();
        Assert.assertEquals("Fahr", transformrFilter.transform("Fahren"));
    }

}