package com.quasiris.qsf.commons.unit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class UnitTranslatorGoldenDynamicTests {

    // Add any number of files here
    private static final String[] RESOURCES = new String[] {
            "/unit/unit-cases_en-US.json",
            "/unit/unit-cases_de-DE.json",
            //"/unit/single_test_en-US.json",
    };

    private static final int PAD = 14;
    private static final int DECIMALS = 1;

    private static final Pattern LOCALE_FROM_FILENAME = Pattern.compile("(.*)_(.+)\\.json$");

    static class Case {
        public String given;                            // e.g. "55.5 cm"
        public String toSortableStringWithOriginalUnit; // expected sortable
        public String formatted;                        // expected human display
        public String autoFormatted;                     // expected human display - auto format the unit
        public String displayName;                      // OPTIONAL custom name for this test
        @Override public String toString() { return "Case{" + given + "}"; }
    }

    @TestFactory
    @DisplayName("Golden cases (each JSON item is its own IntelliJ test)")
    Stream<DynamicTest> goldenCases() throws Exception {
        List<DynamicTest> tests = new ArrayList<>();
        for (String resource : RESOURCES) {
            Locale locale = inferLocaleFromResourceName(resource);
            List<Case> cases = loadCases(resource);
            assertFalse(cases.isEmpty(), "No cases in " + resource);

            for (Case c : cases) {
                final String defaultName = locale + " | " + c.given + " -> " + c.formatted;
                final String name = (c.displayName != null && !c.displayName.isBlank())
                        ? c.displayName
                        : defaultName;

                tests.add(dynamicTest(name, () -> {
                    Unit u = UnitExtractor.extractUnit(c.given);

                    assertNotNull(u.getNumber(), "No numeric part in " + c.given);
                    assertNotNull(u.getUnit(), "No unit part in " + c.given);

                    BigDecimal value = new BigDecimal(u.getNumber());
                    String unit = u.getUnit();

                    UnitTranslator t = new UnitTranslator(unit, value);

                    String actualSortable = t.toSortableString(PAD, DECIMALS, u.getUnit());
                    assertEquals(c.toSortableStringWithOriginalUnit, actualSortable,
                            () -> "toSortable mismatch in " + resource + " for " + c.given);


                    UnitTranslator tSorted = UnitTranslator.fromSortableStringWithOriginalUnit(actualSortable);

                    String actualFormatted = UnitDisplayFormatter.format(tSorted.getValueForUnit(unit), unit, locale);
                    assertEquals(c.formatted, actualFormatted,
                            () -> "formatted mismatch in " + resource + " for " + c.given);


                    String guessedUnit = UnitGuesser.guessUnit(t.getBaseUnit(), t.getBaseValue());
                    String guessedActualFormatted = UnitDisplayFormatter.format(t.getValueForUnit(guessedUnit), guessedUnit, locale);
                    if(c.autoFormatted == null) {
                        c.autoFormatted = c.formatted;
                    }
                    assertEquals(c.autoFormatted, guessedActualFormatted,
                            () -> "formatted mismatch in " + resource + " for " + c.given);

                }));
            }
        }
        return tests.stream();
    }

    // ------------ helpers ------------

    private static List<Case> loadCases(String resource) throws Exception {
        try (InputStream in = UnitTranslatorGoldenDynamicTests.class.getResourceAsStream(resource)) {
            assertNotNull(in, "Missing resource: " + resource);
            return new ObjectMapper().readValue(in, new TypeReference<List<Case>>() {});
        }
    }

    private static Locale inferLocaleFromResourceName(String resource) {
        String name = resource.startsWith("/") ? resource.substring(1) : resource;
        Matcher m = LOCALE_FROM_FILENAME.matcher(name);
        assertTrue(m.find(), "Filename must look like unit-cases_<locale>.json: " + resource);
        return UnitDisplayFormatter.parseLocale(m.group(2));
    }
}
