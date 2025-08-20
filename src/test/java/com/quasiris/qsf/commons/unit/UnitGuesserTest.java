package com.quasiris.qsf.commons.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UnitGuesserTest {

    @Test
    @DisplayName("Length defaults (base = m)")
    void length_defaults() {
        assertEquals("km", UnitGuesser.guessUnit("mm", new BigDecimal("12345000")));
        assertEquals("m",  UnitGuesser.guessUnit("mm", new BigDecimal("12000")));
        assertEquals("cm", UnitGuesser.guessUnit("mm", new BigDecimal("55.0")));
        assertEquals("mm", UnitGuesser.guessUnit("mm", new BigDecimal("8.00")));
        assertEquals("mm", UnitGuesser.guessUnit("mm", BigDecimal.ZERO));
        assertEquals("llm", UnitGuesser.guessUnit("llm", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Not defined")
    void not_defined() {
        assertEquals("tbl", UnitGuesser.guessUnit("tbl", new BigDecimal("12345")));
    }

    @Test
    @DisplayName("Weight defaults (base = g)")
    void weight_defaults() {
        assertEquals("t",  UnitGuesser.guessUnit("g", new BigDecimal("2000000")));
        assertEquals("kg", UnitGuesser.guessUnit("g", new BigDecimal("1000")));
        assertEquals("g",  UnitGuesser.guessUnit("g", new BigDecimal("1")));
        assertEquals("mg", UnitGuesser.guessUnit("g", new BigDecimal("0.5")));
        assertEquals("mg", UnitGuesser.guessUnit("g", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Data defaults (base = B, binary multiples)")
    void data_defaults() {
        assertEquals("TB", UnitGuesser.guessUnit("B", new BigDecimal("1099511627776"))); // 1024^4
        assertEquals("GB", UnitGuesser.guessUnit("B", new BigDecimal("1073741824")));    // 1024^3
        assertEquals("MB", UnitGuesser.guessUnit("B", new BigDecimal("1048576")));       // 1024^2
        assertEquals("KB", UnitGuesser.guessUnit("B", new BigDecimal("1024")));
        assertEquals("B",  UnitGuesser.guessUnit("B", new BigDecimal("1")));
        assertEquals("B",  UnitGuesser.guessUnit("B", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Time defaults (base = s)")
    void time_defaults() {
        assertEquals("d",   UnitGuesser.guessUnit("s", new BigDecimal("86400")));
        assertEquals("h",   UnitGuesser.guessUnit("s", new BigDecimal("3600")));
        assertEquals("min", UnitGuesser.guessUnit("s", new BigDecimal("60")));
        assertEquals("s",   UnitGuesser.guessUnit("s", new BigDecimal("30")));
        assertEquals("s",   UnitGuesser.guessUnit("s", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Volume defaults (base = L)")
    void volume_defaults() {
        assertEquals("L",  UnitGuesser.guessUnit("L", new BigDecimal("2")));
        assertEquals("ml", UnitGuesser.guessUnit("L", new BigDecimal("0.25")));
        assertEquals("ml", UnitGuesser.guessUnit("L", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Exact thresholds choose the larger unit (>= rule)")
    void thresholds_exact() {
        // length
        assertEquals("km", UnitGuesser.guessUnit("mm", new BigDecimal("1000000")));  // 1000 m = 1 km
        assertEquals("m",  UnitGuesser.guessUnit("mm", new BigDecimal("1000")));     // 1 m
        assertEquals("cm", UnitGuesser.guessUnit("mm", new BigDecimal("100.00")));  // 1 cm
        // data
        assertEquals("KB", UnitGuesser.guessUnit("B", new BigDecimal("1024")));      // 1 KB
        assertEquals("MB", UnitGuesser.guessUnit("B", new BigDecimal("1048576")));   // 1 MB
        assertEquals("GB", UnitGuesser.guessUnit("B", new BigDecimal("1073741824"))); // 1 GB
    }

    @Test
    @DisplayName("Negatives use absolute value for unit selection")
    void negatives_use_abs() {
        assertEquals("kg", UnitGuesser.guessUnit("g", new BigDecimal("-1500")));
        assertEquals("KB", UnitGuesser.guessUnit("B", new BigDecimal("-2048")));
        assertEquals("h",  UnitGuesser.guessUnit("s", new BigDecimal("-7200")));
        assertEquals("cm", UnitGuesser.guessUnit("mm", new BigDecimal("-20.00")));
    }

    @Test
    @DisplayName("registerRules: custom base (Hz) with descending thresholds")
    void registerRules_customBase() {
        UnitGuesser.registerRules("Hz", List.of(
            UnitGuesser.Rule.of("GHz", "1000000000"),
            UnitGuesser.Rule.of("MHz", "1000000"),
            UnitGuesser.Rule.of("kHz", "1000"),
            UnitGuesser.Rule.of("Hz",  "0")
        ));

        assertEquals("GHz", UnitGuesser.guessUnit("Hz", new BigDecimal("1500000000")));
        assertEquals("MHz", UnitGuesser.guessUnit("Hz", new BigDecimal("2000000")));
        assertEquals("kHz", UnitGuesser.guessUnit("Hz", new BigDecimal("5000")));
        assertEquals("Hz",  UnitGuesser.guessUnit("Hz", new BigDecimal("500")));
        // case-insensitive base key
        assertEquals("kHz", UnitGuesser.guessUnit("hz", new BigDecimal("5000")));
    }

    @Test
    @DisplayName("addRule (base=mm): insert a mid-range unit without breaking others (restore afterwards)")
    void addRule_insertBetween_then_restore_mm() throws Exception {
        // Snapshot original "mm" rules via reflection so we can restore them
        @SuppressWarnings("unchecked")
        Map<String, List<UnitGuesser.Rule>> rulesMap =
                (Map<String, List<UnitGuesser.Rule>>) getStaticField(UnitGuesser.class, "RULES");
        List<UnitGuesser.Rule> originalMm = new ArrayList<>(rulesMap.get("mm"));

        try {
            // Insert decimeters (dm) between m (>=1000) and cm (>=10) in base=mm
            UnitGuesser.addRule("mm", new UnitGuesser.Rule("dm", new BigDecimal("100")));

            // Now 200 mm should select "dm"
            assertEquals("dm", UnitGuesser.guessUnit("mm", new BigDecimal("200")));

            // Still respects existing thresholds above/below
            assertEquals("m",  UnitGuesser.guessUnit("mm", new BigDecimal("1000"))); // 1 m
            assertEquals("cm", UnitGuesser.guessUnit("mm", new BigDecimal("50")));   // 5 cm
        } finally {
            // Restore original rules for "mm" to avoid cross-test interference
            UnitGuesser.registerRules("mm", originalMm);
        }
    }


    // -------- helper (reflection) --------
    private static Object getStaticField(Class<?> clazz, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(null);
    }
}
