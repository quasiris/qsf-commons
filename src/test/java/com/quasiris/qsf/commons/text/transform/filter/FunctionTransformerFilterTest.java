package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class FunctionTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new FunctionTransformerFilter(myFunction);
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new FunctionTransformerFilter(myFunction);
        Assert.assertEquals("fooFoo", transformrFilter.transform("Foo"));
    }

    Function<String, String> myFunction =
            parameter -> parameter.toLowerCase() + parameter;

}