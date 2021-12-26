package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FunctionTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new FunctionTransformerFilter(myFunction);
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new FunctionTransformerFilter(myFunction);
        assertEquals("fooFoo", transformrFilter.transform("Foo"));
    }

    Function<String, String> myFunction =
            parameter -> parameter.toLowerCase() + parameter;

}