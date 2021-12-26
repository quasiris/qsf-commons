package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GermanUmlautTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new GermanUmlautTransformerFilter();
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new GermanUmlautTransformerFilter();
        assertEquals("Baer", transformrFilter.transform("Bär"));
    }


    @Test
    public void transformFirstLetter() {
        TransformerFilter transformrFilter = new GermanUmlautTransformerFilter();
        assertEquals("Aerzte", transformrFilter.transform("Ärzte"));
    }
}