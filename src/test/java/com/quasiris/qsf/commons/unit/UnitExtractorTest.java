package com.quasiris.qsf.commons.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitExtractorTest {

    @Test
    @DisplayName("Extracts number and alphabetic unit without spaces")
    void extract_simpleAlphaUnit() {
        Unit u = UnitExtractor.extractUnit("12kg");
        assertEquals("12", u.getNumber());
        assertEquals("kg", u.getUnit());
    }

    @Test
    @DisplayName("Extracts number and alphabetic unit with spaces")
    void extract_withSpaces() {
        Unit u = UnitExtractor.extractUnit("  123   mA  ");
        assertEquals("123", u.getNumber());
        assertEquals("mA", u.getUnit());
    }

    @Test
    @DisplayName("Handles decimal point and minus sign before unit")
    void extract_decimalAndMinus() {
        Unit u = UnitExtractor.extractUnit("-40.5dB");
        assertEquals("-40.5", u.getNumber());
        assertEquals("dB", u.getUnit());
    }

    @Test
    @DisplayName("Treats currency symbol (€) as start of unit (number before symbol)")
    void extract_currencyEuro_symbolAfterNumber() {
        Unit u = UnitExtractor.extractUnit("19,99€");
        assertEquals("19,99", u.getNumber());
        assertEquals("€", u.getUnit());
    }

    @Test
    @DisplayName("Treats currency symbol ($) as start of unit (leading symbol -> no number)")
    void extract_currencyDollar_leadingSymbol() {
        Unit u = UnitExtractor.extractUnit("$9.99");
        // Current implementation: once unit starts, digits go into unit as well.
        assertNull(u.getNumber(), "Number should be null when nothing read before unit start");
        assertEquals("$9.99", u.getUnit());
    }

    @Test
    @DisplayName("Treats double quote (\") as start of unit")
    void extract_inchQuote() {
        Unit u = UnitExtractor.extractUnit("10\"x10\"");
        assertEquals("10", u.getNumber());
        assertEquals("\"x10\"", u.getUnit());
    }

    @Test
    @DisplayName("Ignores all spaces while parsing")
    void extract_ignoresSpacesCompletely() {
        Unit u = UnitExtractor.extractUnit("1 2 3   kg");
        assertEquals("123", u.getNumber());
        assertEquals("kg", u.getUnit());
    }

    @Test
    @DisplayName("Edge case: percent is NOT considered a unit start -> stays in number")
    void extract_percentGoesIntoNumber() {
        Unit u = UnitExtractor.extractUnit("50%");
        assertEquals("50%", u.getNumber(), "Current logic puts % into the number part");
        assertEquals("", u.getUnit(), "Unit remains empty because % does not trigger startUnit");
    }

    @Test
    @DisplayName("Empty string -> empty unit, no number")
    void extract_emptyString() {
        Unit u = UnitExtractor.extractUnit("");
        assertNull(u.getNumber());
        assertEquals("", u.getUnit());
    }

    @Test
    @DisplayName("Null input throws NullPointerException")
    void extract_nullInput() {
        assertThrows(NullPointerException.class, () -> UnitExtractor.extractUnit(null));
    }
}
