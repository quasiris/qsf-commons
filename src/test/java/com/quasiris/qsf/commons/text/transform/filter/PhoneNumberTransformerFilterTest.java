package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PhoneNumberTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new PhoneNumberTransformerFilter();
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new PhoneNumberTransformerFilter();
        assertEquals("030901820", transformrFilter.transform("0 30 901820."));
    }

    @Test
    public void transformWithGermanCountryCodePlus() {
        TransformerFilter transformrFilter = new PhoneNumberTransformerFilter();
        assertEquals("030901820", transformrFilter.transform("+49 30 901820."));
    }

    @Test
    public void transformWithGermanCountryCode00() {
        TransformerFilter transformrFilter = new PhoneNumberTransformerFilter();
        assertEquals("030901820", transformrFilter.transform("0049 30 901820."));
    }

    @Test
    public void transformWithUSACountryCodePlus() {
        TransformerFilter transformrFilter = new PhoneNumberTransformerFilter();
        assertEquals("030901820", transformrFilter.transform("+1 30 901820."));
    }

    @Test
    public void transformWithUSACountryCode00() {
        TransformerFilter transformrFilter = new PhoneNumberTransformerFilter();
        assertEquals("030901820", transformrFilter.transform("001 30 901820."));
    }
}