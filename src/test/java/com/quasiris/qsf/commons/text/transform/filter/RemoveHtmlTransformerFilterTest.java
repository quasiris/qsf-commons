package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class RemoveHtmlTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new RemoveHtmlTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new RemoveHtmlTransformerFilter();
        Assert.assertEquals("Fahren", transformrFilter.transform("<p>Fahren</p>"));
    }

}