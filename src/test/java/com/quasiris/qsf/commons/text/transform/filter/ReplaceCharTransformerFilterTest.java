package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReplaceCharTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new ReplaceCharTransformerFilter();
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new ReplaceCharTransformerFilter();
        assertEquals(" f oo ", transformrFilter.transform("_f_oo_"));
    }

}