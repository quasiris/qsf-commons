package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionNormalizeFilterTest {


    @Test
    public void normalizeNull() {
        NormalizerFilter normalizerFilter = new FunctionNormalizeFilter(myFunction);
        Assert.assertNull(normalizerFilter.normalize(null));
    }

    @Test
    public void normalize() {
        NormalizerFilter normalizerFilter = new FunctionNormalizeFilter(myFunction);
        Assert.assertEquals("fooFoo", normalizerFilter.normalize("Foo"));
    }

    Function<String, String> myFunction =
            parameter -> parameter.toLowerCase() + parameter;

}