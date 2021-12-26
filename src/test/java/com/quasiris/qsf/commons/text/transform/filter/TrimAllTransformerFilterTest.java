package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TrimAllTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new TrimAllTransformerFilter();
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new TrimAllTransformerFilter();
        assertEquals("f_oo", transformrFilter.transform("_f_oo "));
    }
}