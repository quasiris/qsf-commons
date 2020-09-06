package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.junit.Assert;
import org.junit.Test;

public class TrimNormalizeFilterTest {


    @Test
    public void normalizeNull() {
        NormalizerFilter normalizerFilter = new TrimNormalizeFilter();
        Assert.assertNull(normalizerFilter.normalize(null));
    }

    @Test
    public void normalize() {
        NormalizerFilter normalizerFilter = new TrimNormalizeFilter();
        Assert.assertEquals("foo", normalizerFilter.normalize("foo "));
    }
}