package com.quasiris.qsf.commons.unit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class UnitTranslator {

    private SimpleEntry<String, BigDecimal> baseUnit;
    private BigDecimal baseValue;

    // NEW: remember the original unit as provided by the caller
    private String originalUnit;

    public UnitTranslator(String unit, BigDecimal value) {
        if (unit != null) {
            this.originalUnit = unit; // <-- store original
            baseUnit = getUnit(unit);
        }
        if (baseUnit != null && value != null) {
            this.baseValue = value.multiply(baseUnit.getValue()).round(MathContext.DECIMAL32);
        }
    }

    public UnitTranslator(String unit) {
        this.originalUnit = unit; // <-- store original
        baseUnit = unitMapping.get(unit.toLowerCase());
    }

    private SimpleEntry<String, BigDecimal> getUnit(String unit) {
        return unitMapping.get(unit.toLowerCase());
    }

    public BigDecimal getBaseValue() { return baseValue; }

    public BigDecimal getValueForUnit(String unit) {
        BigDecimal normFactor = getUnit(unit).getValue();
        return baseValue.divide(normFactor, MathContext.DECIMAL32);
    }

    public String getBaseUnit() {
        if (baseUnit == null) return null;
        return baseUnit.getKey();
    }

    public boolean isBaseUnit(String unit) {
        SimpleEntry<String, BigDecimal> baseUnit = getUnit(unit);
        if (baseUnit == null) return false;
        return baseUnit.getKey().equalsIgnoreCase(unit);
    }

    public BigDecimal getNormFactor() { return baseUnit.getValue(); }

    public boolean hasUnit() { return this.baseUnit != null; }

    // ---------------- NEW: sortable string with original unit suffix ----------------

    /**
     * Returns a character-sortable string:
     * - number is converted to a canonical sortable unit (mm, g, B, s)
     * - output keeps the ORIGINAL unit label (as provided to the constructor)
     * - format: zero-padded integer part + '.' + fixed decimals + ' ' + originalUnit
     *   e.g. 55 cm -> 550.0 in mm -> "0000550.0 cm"
     *
     * @param zeroPadInteger number of digits for the integer part (zero-padded)
     * @param decimals number of fractional digits (fixed-width)
     */
    public String toSortableStringWithOriginalUnit(int zeroPadInteger, int decimals) {
        if (!hasUnit() || baseValue == null) {
            throw new IllegalStateException("No unit/value set.");
        }

        // pick sortable numeric unit by base (ensures lexical == numeric order)
        String sortableNumericUnit = chooseSortableUnitForBase(getBaseUnit());

        // convert baseValue to that sortable numeric unit
        BigDecimal numeric = getValueForUnit(sortableNumericUnit);

        // round to fixed decimals
        BigDecimal scaled = numeric.setScale(decimals, RoundingMode.HALF_UP);

        // split into integer / fractional strings
        String str = scaled.toPlainString();
        String intPart, fracPart;
        int dot = str.indexOf('.');
        if (dot >= 0) {
            intPart = str.substring(0, dot);
            fracPart = str.substring(dot + 1);
        } else {
            intPart = str;
            fracPart = (decimals > 0) ? "0".repeat(decimals) : "";
        }

        // left-pad integer part (handle negatives)
        String sign = "";
        if (intPart.startsWith("-")) {
            sign = "-";
            intPart = intPart.substring(1);
        }
        String paddedInt = String.format("%0" + zeroPadInteger + "d", new java.math.BigInteger(intPart));

        // ensure fractional is exactly 'decimals' length
        if (decimals > 0) {
            if (fracPart.length() < decimals) {
                fracPart = (fracPart + "0".repeat(decimals)).substring(0, decimals);
            } else if (fracPart.length() > decimals) {
                fracPart = fracPart.substring(0, decimals);
            }
        }

        // IMPORTANT: append *original* unit label as requested
        String unitLabel = (originalUnit == null) ? "" : originalUnit;

        return decimals > 0
                ? sign + paddedInt + "." + fracPart + " " + unitLabel
                : sign + paddedInt + " " + unitLabel;
    }

    /**
     * Choose a numeric unit that yields integral-ish magnitudes for stable lexicographic sorting.
     * Base "m" -> "mm", "g" -> "g", "B" -> "B", "s" -> "s"
     */
    private String chooseSortableUnitForBase(String base) {
        if (base == null) return null;
        String b = base.toLowerCase(Locale.ROOT);
        switch (b) {
            case "m":  return "mm";
            case "g":  return "g";
            case "b":  return "b"; // key used in your map; label choice is separate
            case "s":  return "s";
            default:   return base;
        }
    }

    // ---------------- your existing unitMapping (unchanged) ----------------
    private static HashMap<String, SimpleEntry<String, BigDecimal>> unitMapping = new HashMap<>();
    static {
        // ... keep your entire mapping as-is ...
        unitMapping.put("cm", new SimpleEntry<>("m", new BigDecimal(0.01)));
        unitMapping.put("mm", new SimpleEntry<>("m", new BigDecimal(0.001)));
        unitMapping.put("m", new SimpleEntry<>("m", new BigDecimal(1)));
        unitMapping.put("meter", new SimpleEntry<>("m", new BigDecimal(1)));
        unitMapping.put("dm", new SimpleEntry<>("m", new BigDecimal(0.1)));
        unitMapping.put("km", new SimpleEntry<>("m", new BigDecimal(1000)));
        unitMapping.put("zoll", new SimpleEntry<>("m", new BigDecimal(0.0254)));

        unitMapping.put("ml", new SimpleEntry<>("L", new BigDecimal(0.001)));
        unitMapping.put("l", new SimpleEntry<>("L", new BigDecimal(1)));
        unitMapping.put("liter", new SimpleEntry<>("L", new BigDecimal(1)));

        unitMapping.put("lumen", new SimpleEntry<>("lumen", new BigDecimal(1)));

        unitMapping.put("w", new SimpleEntry<>("W", new BigDecimal(1)));
        unitMapping.put("kw", new SimpleEntry<>("W", new BigDecimal(1000)));
        unitMapping.put("watt", new SimpleEntry<>("W", new BigDecimal(1)));

        unitMapping.put("g", new SimpleEntry<>("g", new BigDecimal(1)));
        unitMapping.put("mg", new SimpleEntry<>("g", new BigDecimal(0.001)));
        unitMapping.put("kg", new SimpleEntry<>("g", new BigDecimal(1000)));
        unitMapping.put("kilo", new SimpleEntry<>("g", new BigDecimal(1000)));
        unitMapping.put("gramm", new SimpleEntry<>("g", new BigDecimal(1)));
        unitMapping.put("t", new SimpleEntry<>("g", new BigDecimal(1000_000)));
        unitMapping.put("tonne", new SimpleEntry<>("g", new BigDecimal(1000_000)));

        unitMapping.put("dpi", new SimpleEntry<>("dpi", new BigDecimal(1)));

        unitMapping.put("eur", new SimpleEntry<>("€", new BigDecimal(1)));
        unitMapping.put("€", new SimpleEntry<>("€", new BigDecimal(1)));
        unitMapping.put("ct", new SimpleEntry<>("€", new BigDecimal(0.001)));
        unitMapping.put("cent", new SimpleEntry<>("€", new BigDecimal(0.001)));

        unitMapping.put("$", new SimpleEntry<>("$", new BigDecimal(0.001)));

        unitMapping.put("w/kg", new SimpleEntry<>("W/kg", new BigDecimal(1)));

        unitMapping.put("stück", new SimpleEntry<>("Stk.", new BigDecimal(1)));
        unitMapping.put("stk", new SimpleEntry<>("Stk.", new BigDecimal(1)));
        unitMapping.put("stk.", new SimpleEntry<>("Stk.", new BigDecimal(1)));

        unitMapping.put("pixel", new SimpleEntry<>("pixel", new BigDecimal(1)));
        unitMapping.put("megapixel", new SimpleEntry<>("pixel", new BigDecimal(1000 * 1000)));

        unitMapping.put("std", new SimpleEntry<>("s", new BigDecimal(3600)));
        unitMapping.put("std.", new SimpleEntry<>("s", new BigDecimal(3600)));
        unitMapping.put("s", new SimpleEntry<>("s", new BigDecimal(1)));
        unitMapping.put("sec", new SimpleEntry<>("s", new BigDecimal(1)));
        unitMapping.put("sek", new SimpleEntry<>("s", new BigDecimal(1)));
        unitMapping.put("min", new SimpleEntry<>("s", new BigDecimal(60)));
        unitMapping.put("min.", new SimpleEntry<>("s", new BigDecimal(60)));
        unitMapping.put("h", new SimpleEntry<>("s", new BigDecimal(3600)));
        unitMapping.put("stunde", new SimpleEntry<>("s", new BigDecimal(3600)));
        unitMapping.put("stunden", new SimpleEntry<>("s", new BigDecimal(3600)));
        unitMapping.put("d", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("kalendertage", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("kalendertag", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("tag", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("tage", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("werktage", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("werktag", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("arbeitstag", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("arbeitstage", new SimpleEntry<>("s", new BigDecimal(24*3600)));
        unitMapping.put("woche", new SimpleEntry<>("s", new BigDecimal(7*24*3600)));
        unitMapping.put("wochen", new SimpleEntry<>("s", new BigDecimal(7*24*3600)));
        unitMapping.put("monate", new SimpleEntry<>("s", new BigDecimal(30*24*3600)));
        unitMapping.put("monat", new SimpleEntry<>("s", new BigDecimal(30*24*3600)));
        unitMapping.put("jahre", new SimpleEntry<>("s", new BigDecimal(365*24*3600)));
        unitMapping.put("jahr", new SimpleEntry<>("s", new BigDecimal(365*24*3600)));

        unitMapping.put("fach", new SimpleEntry<>("fach", new BigDecimal(1)));

        unitMapping.put("seiten", new SimpleEntry<>("seite", new BigDecimal(1)));
        unitMapping.put("seite", new SimpleEntry<>("seite", new BigDecimal(1)));

        unitMapping.put("byte", new SimpleEntry<>("B", new BigDecimal(1)));
        unitMapping.put("bit", new SimpleEntry<>("B", new BigDecimal(0.125)));
        unitMapping.put("b", new SimpleEntry<>("B", new BigDecimal(1)));
        unitMapping.put("kb", new SimpleEntry<>("B", new BigDecimal(1024)));
        unitMapping.put("mb", new SimpleEntry<>("B", new BigDecimal(1024*1024)));
        unitMapping.put("gb", new SimpleEntry<>("B", new BigDecimal(1024*1024*1024)));
        unitMapping.put("tb", new SimpleEntry<>("B", new BigDecimal(1024L*1024L*1024L*1024L)));
        unitMapping.put("mbit", new SimpleEntry<>("B", new BigDecimal(1024*0.125)));

        unitMapping.put("bit/s", new SimpleEntry<>("bit/s", new BigDecimal(1)));
        unitMapping.put("kbit/s", new SimpleEntry<>("bit/s", new BigDecimal(1024)));
        unitMapping.put("mbit/s", new SimpleEntry<>("bit/s", new BigDecimal(1024*1024)));
        unitMapping.put("gbit/s", new SimpleEntry<>("bit/s", new BigDecimal(1024*1024*1024)));
        unitMapping.put("tbit/s", new SimpleEntry<>("bit/s", new BigDecimal(1024L*1024L*1024L*1024L)));

        unitMapping.put("hz", new SimpleEntry<>("Hz", new BigDecimal(1)));
        unitMapping.put("khz", new SimpleEntry<>("Hz", new BigDecimal(1000)));
        unitMapping.put("mhz", new SimpleEntry<>("Hz", new BigDecimal(1000000)));
        unitMapping.put("ghz", new SimpleEntry<>("Hz", new BigDecimal(1000000000)));

        unitMapping.put("bilder/s", new SimpleEntry<>("fps", new BigDecimal(1)));
        unitMapping.put("fps", new SimpleEntry<>("fps", new BigDecimal(1)));

        unitMapping.put("mah", new SimpleEntry<>("Ah", new BigDecimal(0.001)));
        unitMapping.put("ah", new SimpleEntry<>("Ah", new BigDecimal(1)));

        unitMapping.put("grad", new SimpleEntry<>("°C", new BigDecimal(1)));
        unitMapping.put("°c", new SimpleEntry<>("°C", new BigDecimal(1)));
        unitMapping.put("°", new SimpleEntry<>("°C", new BigDecimal(1)));

        unitMapping.put("dbi", new SimpleEntry<>("dbi", new BigDecimal(1)));

        unitMapping.put("port", new SimpleEntry<>("Port", new BigDecimal(1)));
        unitMapping.put("ports", new SimpleEntry<>("Port", new BigDecimal(1)));

        unitMapping.put("tsd", new SimpleEntry<>("tsd", new BigDecimal(1000)));
        unitMapping.put("tsd.", new SimpleEntry<>("tsd", new BigDecimal(1000)));
        unitMapping.put("mio", new SimpleEntry<>("mio", new BigDecimal(1000000)));
        unitMapping.put("mio.", new SimpleEntry<>("mio", new BigDecimal(1000000)));

        unitMapping.put("dbm", new SimpleEntry<>("dBm", new BigDecimal(1)));
        unitMapping.put("lux", new SimpleEntry<>("Lux", new BigDecimal(1)));

        unitMapping.put("none", new SimpleEntry<>("none", new BigDecimal(1)));
    }

    // ---------------- your existing expand() (unchanged) ----------------
    public List<String> expand() {
        List<String> expansions = new ArrayList<>();
        if (!hasUnit()) { return expansions; }
        String myBaseUnit = getBaseUnit();
        for (Map.Entry<String, SimpleEntry<String, BigDecimal>> entry : unitMapping.entrySet()) {
            String candidateUnit = entry.getKey();
            SimpleEntry<String, BigDecimal> candidateMapping = entry.getValue();
            String candidateBase = candidateMapping.getKey();
            if (myBaseUnit.equalsIgnoreCase(candidateBase)) {
                BigDecimal convertedVal = baseValue.divide(candidateMapping.getValue(), MathContext.DECIMAL32);
                BigDecimal displayVal = convertedVal.stripTrailingZeros();
                expansions.add(displayVal.toPlainString() + " " + candidateUnit);
            }
        }
        return expansions;
    }
}
