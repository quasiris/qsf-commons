package com.quasiris.qsf.commons.util;

import java.util.regex.Pattern;

/**
 * Created by mki on 7.7.18.
 */
public class ElasticUtil {

    // https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html
    public static final String[] ELASTIC_RESERVED_CHARS = {"\\","+","-","=","&&","||",">","<","!","(",")","{","}","[","]","^","\"","~","*","?",":","/","OR","AND"};

    public static String escape(String value) {
        return escape(value, ELASTIC_RESERVED_CHARS);
    }

    public static String escape(String value, String[] metaCharacters) {
        if(value != null) {
            for (int i = 0; i < metaCharacters.length; i++) {
                if (value.contains(metaCharacters[i])) {
                    value = value.replace(metaCharacters[i], "\\" + metaCharacters[i]);
                }
            }
        }
        return value;
    }


    public static String value2FieldName(String value) {
        value = value.toLowerCase();
        value = value.replaceAll(Pattern.quote("ü"), "ue");
        value = value.replaceAll(Pattern.quote("ä"), "ae");
        value = value.replaceAll(Pattern.quote("ö"), "oe");
        value = value.replaceAll(Pattern.quote("ß"), "ss");
        value = value.replaceAll(Pattern.quote("€"), "eur");
        value = value.replaceAll(Pattern.quote("§"), "paragraph");
        value = value.replaceAll(Pattern.quote("$"), "dollar");
        value = value.replaceAll(Pattern.quote("@"), "at");
        value = value.replaceAll(Pattern.quote("#"), "hash");
        value = value.replaceAll(Pattern.quote("*"), "star");
        value = value.replaceAll("[^a-z0-9]", "_");
        return value;
    }
}
