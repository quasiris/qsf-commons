package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.junit.Assert;
import org.junit.Test;

public class GermanUmlautNormalizeFilterTest {


    @Test
    public void normalizeNull() {
        NormalizerFilter normalizerFilter = new GermanUmlautNormalizeFilter();
        Assert.assertNull(normalizerFilter.normalize(null));
    }

    @Test
    public void normalize() {
        NormalizerFilter normalizerFilter = new GermanUmlautNormalizeFilter();
        Assert.assertEquals("Baer", normalizerFilter.normalize("Bär"));
    }


    @Test
    public void normalizeFirstLetter() {
        NormalizerFilter normalizerFilter = new GermanUmlautNormalizeFilter();
        Assert.assertEquals("Aerzte", normalizerFilter.normalize("Ärzte"));
    }
}