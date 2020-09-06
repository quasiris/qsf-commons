package com.quasiris.qsf.commons.text.transform;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class TextTransformerTest {

    @Test
    public void transform() {

        TextTransformer transform = TextTransformerFactory.create("lowerCase|trim|removeSpecialChars|removeNumbers");
        String actual = transform.normalize(" fOo.§ba12%r.");
        Assert.assertEquals("foobar", actual);
    }


    @Test
    public void normalizeWithRegisteredFunction() {
        TextTransformerFactory.register("myFunction", myFunction);

        TextTransformer transform = TextTransformerFactory.create("lowerCase|trim|removeSpecialChars|removeNumbers|myFunction");
        String actual = transform.normalize(" fOo.§ba12%r.");
        Assert.assertEquals("foobarfoobar", actual);
    }

    Function<String, String> myFunction =
            parameter -> parameter.toLowerCase() + parameter;
}