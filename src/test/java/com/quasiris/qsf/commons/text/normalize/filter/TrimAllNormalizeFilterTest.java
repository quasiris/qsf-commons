package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.junit.Assert;
import org.junit.Test;

public class TrimAllNormalizeFilterTest {


    @Test
    public void normalizeNull() {
        NormalizerFilter normalizerFilter = new TrimAllNormalizeFilter();
        Assert.assertNull(normalizerFilter.normalize(null));
    }

    @Test
    public void normalize() {
        NormalizerFilter normalizerFilter = new TrimAllNormalizeFilter();
        Assert.assertEquals("f_oo", normalizerFilter.normalize("_f_oo "));
    }
}