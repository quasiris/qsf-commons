package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.junit.Assert;
import org.junit.Test;

public class CharReplaceNormalizeFilterTest {

    String charsToReplace = "äöüß";
    String[] replacements = {"ae", "oe", "ue", "ss"};

    @Test
    public void normalizeNull() {
        NormalizerFilter normalizerFilter = new CharReplaceNormalizeFilter(charsToReplace, replacements);
        Assert.assertNull(normalizerFilter.normalize(null));
    }

    @Test
    public void normalize() {
        NormalizerFilter normalizerFilter = new CharReplaceNormalizeFilter(charsToReplace, replacements);
        Assert.assertEquals("Faehre", normalizerFilter.normalize("Fähre"));
    }

}