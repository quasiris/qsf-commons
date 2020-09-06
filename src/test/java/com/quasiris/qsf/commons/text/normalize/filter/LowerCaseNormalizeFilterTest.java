package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LowerCaseNormalizeFilterTest {


    @Test
    public void normalizeNull() {
        NormalizerFilter normalizerFilter = new LowerCaseNormalizeFilter();
        Assert.assertNull(normalizerFilter.normalize(null));
    }

    @Test
    public void normalize() {
        NormalizerFilter normalizerFilter = new LowerCaseNormalizeFilter();
        Assert.assertEquals("foo", normalizerFilter.normalize("Foo"));
    }
}