package com.quasiris.qsf.commons.util;

import java.math.BigDecimal;
public final class BigDecimalUtils {
    private BigDecimalUtils() {}

    public static BigDecimal toBigDecimal(Object v) {
        if (v == null) return null;

        if (v instanceof BigDecimal) {
            return (BigDecimal) v;
        }
        if (v instanceof java.math.BigInteger) {
            return new BigDecimal((java.math.BigInteger) v);
        }
        if (v instanceof Byte || v instanceof Short || v instanceof Integer || v instanceof Long) {
            // scale 0
            return BigDecimal.valueOf(((Number) v).longValue());
        }
        if (v instanceof Float || v instanceof Double) {
            // safe path for binary floats
            return BigDecimal.valueOf(((Number) v).doubleValue());
        }

        try {
            return new BigDecimal(v.toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
