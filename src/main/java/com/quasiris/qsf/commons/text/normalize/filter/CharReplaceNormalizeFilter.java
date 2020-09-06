package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

public class CharReplaceNormalizeFilter implements NormalizerFilter {


    private String charsToReplace;

    private String[] replacements;

    public CharReplaceNormalizeFilter(String charsToReplace, String[] replacements) {
        if(charsToReplace.length() != replacements.length) {
            throw new IllegalArgumentException("The lenght must be equal.");
        }
        this.charsToReplace = charsToReplace;
        this.replacements = replacements;
    }


    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        for (char c : text.toCharArray()) {
            if(charsToReplace.indexOf(c) != -1) {
                int i = charsToReplace.indexOf(c);
                ret.append(replacements[i]);
            } else {
                ret.append(c);
            }
        }
        return ret.toString();
    }
}
