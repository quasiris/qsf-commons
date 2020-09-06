package com.quasiris.qsf.commons.text.normalize;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class NormalizerTest {

    @Test
    public void normalize() {

        Normalizer normalizer = NormalizerFactory.create("lowerCase|trim|removeSpecialChars|removeNumbers");
        String actual = normalizer.normalize(" fOo.§ba12%r.");
        Assert.assertEquals("foobar", actual);
    }


    @Test
    public void normalizeWithRegisteredFunction() {
        NormalizerFactory.register("myFunction", myFunction);

        Normalizer normalizer = NormalizerFactory.create("lowerCase|trim|removeSpecialChars|removeNumbers|myFunction");
        String actual = normalizer.normalize(" fOo.§ba12%r.");
        Assert.assertEquals("foobarfoobar", actual);
    }

    Function<String, String> myFunction =
            parameter -> parameter.toLowerCase() + parameter;
}