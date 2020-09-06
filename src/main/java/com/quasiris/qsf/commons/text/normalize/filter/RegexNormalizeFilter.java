package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

import java.util.regex.Pattern;

public class RegexNormalizeFilter implements NormalizerFilter {

    private Pattern pattern;

    private String replacement = "";


    public RegexNormalizeFilter(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }
        return pattern.matcher(text).replaceAll(replacement);
    }

    /**
     * Getter for property 'pattern'.
     *
     * @return Value for property 'pattern'.
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Setter for property 'pattern'.
     *
     * @param pattern Value to set for property 'pattern'.
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Getter for property 'replacement'.
     *
     * @return Value for property 'replacement'.
     */
    public String getReplacement() {
        return replacement;
    }

    /**
     * Setter for property 'replacement'.
     *
     * @param replacement Value to set for property 'replacement'.
     */
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
}
