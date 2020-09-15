package com.quasiris.qsf.commons.text;

import java.util.regex.Pattern;

/**
 * Created by mki on 17.12.17.
 */
public class TextUtils {


    public static Pattern NUMBER_PATTERN = Pattern.compile( "[0-9]" );

    public static String replaceGermanUmlaut(String input) {

        //replace all lower Umlauts
        String output =
                input
                        .replaceAll("ü", "ue")
                        .replaceAll("ö", "oe")
                        .replaceAll("ä", "ae")
                        .replaceAll("ß", "ss");

        //first replace all capital umlaute in a non-capitalized context (e.g. Übung)
        output =
                output
                        .replaceAll("Ü(?=[a-zäöüß ])", "Ue")
                        .replaceAll("Ö(?=[a-zäöüß ])", "Oe")
                        .replaceAll("Ä(?=[a-zäöüß ])", "Ae");

        //now replace all the other capital umlaute
        output =
                output
                        .replaceAll("Ü", "UE")
                        .replaceAll("Ö", "OE")
                        .replaceAll("Ä", "AE");

        return output;
    }

    public static boolean isGermanPostalCode(String token) {
        return token.matches("\\d{5}");
    }

    public static boolean containsNumber( String value ) {
        return NUMBER_PATTERN.matcher(value).find();
    }


    public static boolean containsLetter(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if(Character.isLetter(c)) {
                return true;
            }

        }
        return false;

    }

    public static String trimAll(String str) {
        return strip(str);
    }

    public static String strip(String str) {
        if (isEmpty(str)) {
            return str;
        } else {
            str = stripStart(str);
            return stripEnd(str);
        }
    }

    public static String stripStart(String str) {
        if(str == null) {
            return null;
        }
        int start = 0;
        int strLen = str.length();
        while(start != strLen && !Character.isLetterOrDigit(str.charAt(start))) {
            ++start;
        }
        return str.substring(start);
    }

    public static String stripEnd(String str) {
        if(str == null) {
            return null;
        }
        int end = str.length();
        while(end != 0 && !Character.isLetterOrDigit(str.charAt(end - 1))) {
            --end;
        }
        return str.substring(0, end);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
