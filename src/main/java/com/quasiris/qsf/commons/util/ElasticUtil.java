package com.quasiris.qsf.commons.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * Created by mki on 7.7.18.
 */
public class ElasticUtil {

    // https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html
    public static final String[] ELASTIC_RESERVED_CHARS = {"\\","+","-","=","&&","||",">","<","!","(",")","{","}","[","]","^","\"","~","*","?",":","/"," OR "," AND "};
    public static final String[] ELASTIC_ESCAPED_CHARS = {"\\\\","\\+","\\-","\\=","\\&&","\\||","\\>","\\<","\\!","\\(","\\)","\\{","\\}","\\[","\\]","\\^","\\\"","\\~","\\*","\\?","\\:","\\/"," \\OR "," \\AND "};

    // https://www.elastic.co/guide/en/elasticsearch/reference/current/regexp-syntax.html
    // . ? + * | { } [ ] ( ) " \
    // # @ & < >  ~
    public static final String[] ELASTIC_REGEX_RESERVED_CHARS = {"\\",".","?","+","*","|","{","}","[","]","(",")","\"","#","@","&","<",">","~"};
    public static final String[] ELASTIC_REGEX_ESCAPED_CHARS = {"\\\\","\\.","\\?","\\+","\\*","\\|","\\{","\\}","\\[","\\]","\\(","\\)","\\\"","\\#","\\@","\\&","\\<","\\>","\\~"};

    public static String escape(String value) {
        return escape(value, ELASTIC_RESERVED_CHARS, ELASTIC_ESCAPED_CHARS);
    }

    public static String escapeRegex(String value) {
        return escape(value, ELASTIC_REGEX_RESERVED_CHARS, ELASTIC_REGEX_ESCAPED_CHARS);
    }


    private static String escape(String value, String[] metaCharacters, String[] metaCharactersEscaped) {
        if(value != null) {
            for (int i = 0; i < metaCharacters.length; i++) {
                if (value.contains(metaCharacters[i])) {
                    value = value.replace(metaCharacters[i], metaCharactersEscaped[i]);
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

    public static String parseIndexFromUrl(String url) throws URISyntaxException {
        if(url==null) {
            throw new URISyntaxException("", "The url is null.");
        }
        URI uri = new URI(url);
        String[] parts = uri.getPath().split(Pattern.quote("/"));
        if(parts.length < 2) {
            throw new URISyntaxException(url, "The url " + url + " must contain at least one path part.");
        }
        String index = parts[1];
        return index;
    }
}
