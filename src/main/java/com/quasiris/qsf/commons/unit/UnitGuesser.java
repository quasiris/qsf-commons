package com.quasiris.qsf.commons.unit;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Picks a human-friendly unit purely from a value expressed in a base unit.
 * Returns ONLY the chosen unit (no number formatting).
 *
 * Examples:
 *   UnitGuesser.guessUnit("m", new BigDecimal("0.055"))  -> "cm"
 *   UnitGuesser.guessUnit("g", new BigDecimal("1500"))   -> "kg"
 *   UnitGuesser.guessUnit("B", new BigDecimal("1536"))   -> "KB"   // binary (1024)
 *   UnitGuesser.guessUnit("s", new BigDecimal("7200"))   -> "h"
 *   UnitGuesser.guessUnit("L", new BigDecimal("0.25"))   -> "ml"
 */
public final class UnitGuesser {

    private UnitGuesser() {}

    /** Threshold rule: if |baseValue| >= thresholdBase, use 'unit'. */
    public static final class Rule {
        public final String unit;
        public final BigDecimal thresholdBase; // in the base unit

        public Rule(String unit, BigDecimal thresholdBase) {
            this.unit = Objects.requireNonNull(unit, "unit");
            this.thresholdBase = Objects.requireNonNull(thresholdBase, "thresholdBase");
        }

        public static Rule of(String unit, String thresholdBase) {
            return new Rule(unit, new BigDecimal(thresholdBase));
        }
    }

    /** baseKey (normalized lower-case) -> rules sorted by descending threshold */
    private static final Map<String, List<Rule>> RULES = new HashMap<>();

    private static String norm(String s) {
        return s == null ? null : s.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Register or replace rules for a base unit. Rules are sorted by descending threshold.
     * Ensure at least one rule has a 0 threshold (fallback).
     */
    public static void registerRules(String baseUnit, List<Rule> rules) {
        if (baseUnit == null || rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("baseUnit and rules must be non-empty");
        }
        List<Rule> sorted = new ArrayList<>(rules);
        sorted.sort((a, b) -> b.thresholdBase.compareTo(a.thresholdBase)); // desc
        boolean hasZero = sorted.stream().anyMatch(r -> r.thresholdBase.signum() == 0);
        if (!hasZero) {
            // add a fallback using the last unit as default with threshold 0
            sorted.add(Rule.of(sorted.get(sorted.size() - 1).unit, "0"));
        }
        RULES.put(norm(baseUnit), Collections.unmodifiableList(sorted));
    }

    /** Add or override a single rule for a base unit; keeps list sorted by threshold desc. */
    public static void addRule(String baseUnit, Rule rule) {
        String k = norm(baseUnit);
        RULES.compute(k, (key, list) -> {
            List<Rule> l = (list == null) ? new ArrayList<>() : new ArrayList<>(list);
            // replace if same unit exists
            l = l.stream().filter(r -> !r.unit.equalsIgnoreCase(rule.unit)).collect(Collectors.toList());
            l.add(rule);
            l.sort((a, b) -> b.thresholdBase.compareTo(a.thresholdBase));
            // ensure fallback 0
            boolean hasZero = l.stream().anyMatch(r -> r.thresholdBase.signum() == 0);
            if (!hasZero) {
                l.add(Rule.of(l.get(l.size() - 1).unit, "0"));
            }
            return Collections.unmodifiableList(l);
        });
    }

    /** Guess only the unit string from base unit + value. */
    public static String guessUnit(String baseUnit, BigDecimal baseValue) {
        if (baseUnit == null || baseValue == null) {
            throw new IllegalArgumentException("baseUnit and baseValue must not be null");
        }
        List<Rule> rules = RULES.get(norm(baseUnit));
        if (rules == null || rules.isEmpty()) {
            // no rules registered -> return the base itself
            return baseUnit;
        }
        BigDecimal abs = baseValue.abs();
        for (Rule r : rules) {
            if (abs.compareTo(r.thresholdBase) >= 0) {
                return r.unit;
            }
        }
        // Fallback (shouldn't happen if there's a 0-threshold rule)
        return rules.get(rules.size() - 1).unit;
    }

    /** Convenience overload. */
    public static String guessUnit(String baseUnit, double baseValue) {
        return guessUnit(baseUnit, BigDecimal.valueOf(baseValue));
    }

    // -------- Default rule sets --------
    static {
        // Length, base "m"
        registerRules("m", List.of(
            Rule.of("km", "1000"),
            Rule.of("m",  "1"),
            Rule.of("cm", "0.01"),
            Rule.of("mm", "0")
        ));

        // Weight, base "g"
        registerRules("g", List.of(
            Rule.of("t",  "1000000"),
            Rule.of("kg", "1000"),
            Rule.of("g",  "1"),
            Rule.of("mg", "0")
        ));

        // Data size, base "B" (binary multiples)
        registerRules("B", List.of(
            Rule.of("TB", "1099511627776"), // 1024^4
            Rule.of("GB", "1073741824"),    // 1024^3
            Rule.of("MB", "1048576"),       // 1024^2
            Rule.of("KB", "1024"),
            Rule.of("B",  "0")
        ));

        // Time, base "s"
        registerRules("s", List.of(
            Rule.of("d",   "86400"),
            Rule.of("h",   "3600"),
            Rule.of("min", "60"),
            Rule.of("s",   "0")
        ));

        // Volume, base "L"
        registerRules("L", List.of(
            Rule.of("L",  "1"),
            Rule.of("ml", "0")
        ));
    }
}
