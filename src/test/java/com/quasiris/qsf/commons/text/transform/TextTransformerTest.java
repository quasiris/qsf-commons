package com.quasiris.qsf.commons.text.transform;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextTransformerTest {

    @Test
    public void transform() {

        TextTransformer transform = TextTransformerFactory.create("lowerCase|trim|removeSpecialChars|removeNumbers");
        String actual = transform.normalize(" fOo.§ba12%r.");
        assertEquals("foobar", actual);
    }


    @Test
    public void transformWithRegisteredFunction() {
        TextTransformerFactory.register("myFunction", myFunction);

        TextTransformer transform = TextTransformerFactory.create("lowerCase|trim|removeSpecialChars|removeNumbers|myFunction");
        String actual = transform.normalize(" fOo.§ba12%r.");
        assertEquals("foobarfoobar", actual);
    }

    Function<String, String> myFunction =
            parameter -> parameter.toLowerCase() + parameter;


    @Test
    public void transformWithBuilder() {
        TextTransformerFactory.register("myFunction", myFunction);

        TextTransformer transform = TextTransformerBuilder.create().
                lowerCase().
                trim().
                removeSpecialChars().
                removeNumbers().
                function(myFunction).
                build();

        String actual = transform.normalize(" fOo.§ba12%r.");
        assertEquals("foobarfoobar", actual);
    }

}