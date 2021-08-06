package com.quasiris.qsf.commons.text;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;
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

    public static String limit(String text, int maxLength) {
        return limit(text, maxLength, "..");
    }

    /**
     * Limit text after word
     * @param text
     * @param maxLength
     * @param punctuation
     * @return
     */
    public static String limit(String text, int maxLength, String punctuation) {
        if(StringUtils.isNotEmpty(text)) {
            if(StringUtils.isNotEmpty(punctuation)) {
                maxLength -= punctuation.length();
            }

            text = StringUtils.substring(text, 0, maxLength);
            text = removeLastToken(text);

            if(StringUtils.isNotEmpty(punctuation)) {
                text += punctuation;
            }
        }

        return text;
    }

    protected static String removeLastToken(String text) {
        if(StringUtils.isNotEmpty(text)) {
            while (text.length() > 0 && !text.endsWith(" ")) {
                text = text.substring(0, text.length()-1);
            }
            text = text.trim();
        }

        return text;
    }

    public static String replace(String value, Map<String, Object> valueMap) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(valueMap);
        return stringSubstitutor.replace(value);
    }
}
