package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class CharReplaceTransformerFilterTest {

    String charsToReplace = "äöüß";
    String[] replacements = {"ae", "oe", "ue", "ss"};

    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new CharReplaceTransformerFilter(charsToReplace, replacements);
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new CharReplaceTransformerFilter(charsToReplace, replacements);
        Assert.assertEquals("Faehre", transformrFilter.transform("Fähre"));
    }

}