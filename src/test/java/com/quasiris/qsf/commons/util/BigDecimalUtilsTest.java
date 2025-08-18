package com.quasiris.qsf.commons.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalUtilsTest {

    @Test
    void toBigDecimal_nullInput_returnsNull() {
        assertNull(BigDecimalUtils.toBigDecimal(null));
    }

    @Test
    void toBigDecimal_bigDecimal_passthrough() {
        BigDecimal input = new BigDecimal("123.4500");
        BigDecimal result = BigDecimalUtils.toBigDecimal(input);
        assertSame(input, result, "Should return the same BigDecimal instance");
    }

    @Test
    void toBigDecimal_integer_converts() {
        BigDecimal result = BigDecimalUtils.toBigDecimal(Integer.valueOf(42));
        assertNotNull(result);
        assertEquals(new BigDecimal("42"), result);
    }

    @Test
    void toBigDecimal_long_converts() {
        BigDecimal result = BigDecimalUtils.toBigDecimal(Long.valueOf(9000000000L));
        assertNotNull(result);
        assertEquals(new BigDecimal("9000000000"), result);
    }

    @Test
    void toBigDecimal_double_convertsUsingValueOf() {
        // Note: BigDecimal.valueOf(0.1) yields 0.1 exactly (via string path), not the binary float.
        BigDecimal result = BigDecimalUtils.toBigDecimal(Double.valueOf(0.1));
        assertNotNull(result);
        assertEquals(new BigDecimal("0.1"), result);
    }

    @Test
    void toBigDecimal_stringNumeric_converts() {
        BigDecimal result = BigDecimalUtils.toBigDecimal("  -123.456  ");
        assertNotNull(result);
        assertEquals(new BigDecimal("-123.456"), result);
    }

    @Test
    void toBigDecimal_largeStringNumeric_converts() {
        String big = "123456789012345678901234567890.123456789";
        BigDecimal result = BigDecimalUtils.toBigDecimal(big);
        assertNotNull(result);
        assertEquals(new BigDecimal(big), result);
    }

    @Test
    void toBigDecimal_invalidString_returnsNull() {
        assertNull(BigDecimalUtils.toBigDecimal("abc"));
        assertNull(BigDecimalUtils.toBigDecimal("12.3.4"));
        assertNull(BigDecimalUtils.toBigDecimal(""));
        assertNull(BigDecimalUtils.toBigDecimal("   "));
    }
}
