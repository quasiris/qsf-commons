package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;

public class CharReplaceTransformerFilter implements TransformerFilter {


    private String charsToReplace;

    private String[] replacements;

    public CharReplaceTransformerFilter(String charsToReplace, String[] replacements) {
        if(charsToReplace.length() != replacements.length) {
            throw new IllegalArgumentException("The lenght must be equal.");
        }
        this.charsToReplace = charsToReplace;
        this.replacements = replacements;
    }


    @Override
    public String transform(String text) {
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
