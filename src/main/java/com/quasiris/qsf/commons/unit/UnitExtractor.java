package com.quasiris.qsf.commons.unit;

public class UnitExtractor {

    public static Unit extractUnit(String value) {
        char[] chars = value.toCharArray();

        StringBuilder number = new StringBuilder();
        StringBuilder unitBuilder = new StringBuilder();
        boolean startUnit = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(c == ' ') {
                continue;
            }
            if(isStartUnit(c)) {
                startUnit = true;
            }

            if(!startUnit) {
                number.append(c);
            } else {
                unitBuilder.append(c);
            }
        }

        Unit unit = new Unit();
        if(number.length() > 0) {
            unit.setNumber(number.toString());
        }

        unit.setUnit(unitBuilder.toString());

        return unit;

    }

    private static boolean isStartUnit(char c) {
        if(Character.isLetter(c)) {
            return true;
        }
        if(c == '€' || c == '$' || c == '"') {
            return true;
        }
        return false;
    }
}
