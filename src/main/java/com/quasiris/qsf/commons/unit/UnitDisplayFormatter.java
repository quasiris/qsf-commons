package com.quasiris.qsf.commons.unit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * UnitDisplayFormatter
 *
 * Best-practice, locale-aware number formatting for common technical units.
 * - Grouping and min/max decimals are controlled purely by DecimalFormat PATTERNS.
 * - You can register/override patterns at runtime.
 * - Fallback pattern is "#,##0.###".
 *
 * Example:
 *   UnitDisplayFormatter.format(new BigDecimal("55"), "cm", Locale.GERMANY);   // "55 cm"
 *   UnitDisplayFormatter.format(new BigDecimal("55.5"), "cm", Locale.GERMANY); // "55,5 cm"
 *   UnitDisplayFormatter.format(new BigDecimal("1024"), "MB", Locale.US);      // "1,024 MB"
 */
public final class UnitDisplayFormatter {

    private UnitDisplayFormatter() {}

    /** Default rounding for display (HALF_UP is most natural for humans). */
    public static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;

    /** Fallback pattern if unit is unknown. */
    public static final String DEFAULT_PATTERN = "#,##0.###";

    /** Registry of best-practice patterns per (lowercased) unit key. */
    private static final Map<String, String> UNIT_PATTERNS = new HashMap<>();

    static {
        // -------- LENGTH --------
        // integers or up to a couple of decimals, grouping when large
        put("mm", "#,##0");        // 2500 -> "2,500"
        put("cm", "#,##0.#");      // 55.5 -> "55.5"
        put("m",  "#,##0.##");     // 1.25 -> "1.25"
        put("dm", "#,##0.#");
        put("km", "#,##0.###");
        put("zoll", "#,##0.##");   // inch (German "Zoll")

        // -------- WEIGHT --------
        put("mg", "#,##0");
        put("g",  "#,##0");
        put("kg", "#,##0.###");
        put("t",  "#,##0.###");
        put("kilo", "#,##0.###");
        put("gramm", "#,##0");

        // -------- VOLUME --------
        put("ml", "#,##0");
        put("l",  "#,##0.##");
        put("liter", "#,##0.##");

        // -------- POWER --------
        put("w",  "#,##0");
        put("watt", "#,##0");
        put("kw", "#,##0.##");

        // -------- DATA SIZE --------
        // Usually integers; allow up to 2 decimals if you truly use fractional units.
        put("b",  "#,##0");        // bytes
        put("kb", "#,##0.##");
        put("mb", "#,##0.##");
        put("gb", "#,##0.##");
        put("tb", "#,##0.##");
        put("byte", "#,##0");
        put("bit", "#,##0");       // counts of bits (not rate)
        // Data rate
        put("bit/s",  "#,##0.##");
        put("kbit/s", "#,##0.##");
        put("mbit/s", "#,##0.##");
        put("gbit/s", "#,##0.##");
        put("tbit/s", "#,##0.##");

        // -------- TIME --------
        put("s",   "#,##0");       // seconds as integer
        put("sec", "#,##0");
        put("sek", "#,##0");
        put("min", "#,##0.#");     // allow half minutes if needed
        put("h",   "#,##0.##");    // hours can be fractional
        put("d",   "#,##0");
        put("tag", "#,##0");
        put("tage", "#,##0");
        put("woche", "#,##0");
        put("wochen", "#,##0");
        put("monat", "#,##0");
        put("monate", "#,##0");
        put("jahr", "#,##0.#");
        put("jahre", "#,##0.#");

        // -------- FREQUENCY --------
        put("hz",  "#,##0");
        put("khz", "#,##0.##");
        put("mhz", "#,##0.###");
        put("ghz", "#,##0.####");

        // -------- TEMPERATURE --------
        put("°c", "0.#");          // "0.#" => show decimal only if present
        put("grad", "0.#");

        // -------- IMAGE / VIDEO --------
        put("pixel", "#,##0");
        put("megapixel", "#,##0.##");
        put("fps", "#,##0.##");
        put("bilder/s", "#,##0.##");

        // -------- ELECTRICAL / MISC --------
        put("ah",  "0.###");
        put("mah", "#,##0");
        put("dpi", "#,##0");
        put("dbi", "#,##0.#");

        // -------- COUNTS --------
        put("stk",   "#,##0");
        put("stk.",  "#,##0");
        put("stück", "#,##0");
        put("fach",  "#,##0");
        put("seite", "#,##0");
        put("seiten", "#,##0");

        // -------- CURRENCY (simple numeric; prefer NumberFormat.getCurrencyInstance for full i18n) --------
        put("€",  "#,##0.00");
        put("eur","#,##0.00");
        put("$",  "#,##0.00");
    }

    private static void put(String unitLower, String pattern) {
        UNIT_PATTERNS.put(unitLower, pattern);
    }

    /**
     * Format a value with best-practice pattern for the given unit and locale.
     * Keeps the unit's original casing in the output.
     *
     * @param value numeric value in the ORIGINAL unit (not base/sortable unit)
     * @param unit the unit label (e.g. "cm", "GB", "kg") – case preserved in output
     * @param locale target locale (e.g. Locale.GERMANY)
     * @return e.g. "55 cm", "55,5 cm", "1,024 MB"
     */
    public static String format(BigDecimal value, String unit, Locale locale) {
        return format(value, unit, locale, DEFAULT_ROUNDING);
    }

    public static String format(BigDecimal value, String unit, Locale locale, RoundingMode rounding) {
        if (value == null || unit == null) {
            throw new IllegalArgumentException("value and unit must not be null");
        }
        String pattern = UNIT_PATTERNS.getOrDefault(unit.toLowerCase(Locale.ROOT), DEFAULT_PATTERN);
        DecimalFormat df = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
        df.setRoundingMode(rounding);
        return df.format(value) + " " + unit; // preserve given casing
    }

    /**
     * Obtain the DecimalFormat configured for a unit & locale (in case you want to use it directly).
     */
    public static DecimalFormat formatter(Locale locale, String unit, RoundingMode rounding) {
        String pattern = UNIT_PATTERNS.getOrDefault(unit.toLowerCase(Locale.ROOT), DEFAULT_PATTERN);
        DecimalFormat df = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
        df.setRoundingMode(rounding == null ? DEFAULT_ROUNDING : rounding);
        return df;
    }

    /**
     * Override or add a pattern for a specific unit (case-insensitive key).
     * Example: registerPattern("cm", "0.##") to remove grouping for centimeters.
     */
    public static void registerPattern(String unit, String pattern) {
        if (unit == null || pattern == null) {
            throw new IllegalArgumentException("unit and pattern must not be null");
        }
        UNIT_PATTERNS.put(unit.toLowerCase(Locale.ROOT), pattern);
    }

    /**
     * Parse strings like "de-DE", "en_US" into Locale.
     */
    public static Locale parseLocale(String s) {
        if (s == null || s.isBlank()) return Locale.getDefault();
        String[] p = s.replace('-', '_').split("_", 3);
        if (p.length == 1) return new Locale(p[0]);
        if (p.length == 2) return new Locale(p[0], p[1]);
        return new Locale(p[0], p[1], p[2]);
    }
}
