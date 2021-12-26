package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CharReplaceTransformerFilterTest {

    String charsToReplace = "äöüß";
    String[] replacements = {"ae", "oe", "ue", "ss"};

    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new CharReplaceTransformerFilter(charsToReplace, replacements);
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new CharReplaceTransformerFilter(charsToReplace, replacements);
        assertEquals("Faehre", transformrFilter.transform("Fähre"));
    }

}