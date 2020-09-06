package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import com.quasiris.qsf.commons.text.SpecialChars;

public class RemoveCharNormalizeFilter implements NormalizerFilter {

    private String charsToRemove = SpecialChars.ALL;

    public RemoveCharNormalizeFilter() {
    }

    public RemoveCharNormalizeFilter(String charsToRemove) {
        this.charsToRemove = charsToRemove;
    }

    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {

            if(charsToRemove.indexOf(c) == -1) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Getter for property 'charsToRemove'.
     *
     * @return Value for property 'charsToRemove'.
     */
    public String getCharsToRemove() {
        return charsToRemove;
    }

    /**
     * Setter for property 'charsToRemove'.
     *
     * @param charsToRemove Value to set for property 'charsToRemove'.
     */
    public void setCharsToRemove(String charsToRemove) {
        this.charsToRemove = charsToRemove;
    }
}
