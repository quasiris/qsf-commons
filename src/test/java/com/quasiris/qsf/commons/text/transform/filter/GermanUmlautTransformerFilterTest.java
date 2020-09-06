package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class GermanUmlautTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new GermanUmlautTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new GermanUmlautTransformerFilter();
        Assert.assertEquals("Baer", transformrFilter.transform("Bär"));
    }


    @Test
    public void transformFirstLetter() {
        TransformerFilter transformrFilter = new GermanUmlautTransformerFilter();
        Assert.assertEquals("Aerzte", transformrFilter.transform("Ärzte"));
    }
}