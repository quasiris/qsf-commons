package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.junit.Assert;
import org.junit.Test;

public class RemoveCharNormalizeFilterTest {


    @Test
    public void normalizeNull() {
        NormalizerFilter normalizerFilter = new RemoveCharNormalizeFilter();
        Assert.assertNull(normalizerFilter.normalize(null));
    }

    @Test
    public void normalize() {
        NormalizerFilter normalizerFilter = new RemoveCharNormalizeFilter();
        Assert.assertEquals("foo", normalizerFilter.normalize("_f_oo_"));
    }

}