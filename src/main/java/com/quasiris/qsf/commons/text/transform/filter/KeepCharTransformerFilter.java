package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.SpecialChars;
import com.quasiris.qsf.commons.text.transform.TransformerFilter;

public class KeepCharTransformerFilter implements TransformerFilter {

    private String charsToKeep = SpecialChars.NUMBERS;

    public KeepCharTransformerFilter() {
    }

    public KeepCharTransformerFilter(String charsToKeep) {
        this.charsToKeep = charsToKeep;
    }

    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {

            if(charsToKeep.indexOf(c) != -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Getter for property 'charsToKeep'.
     *
     * @return Value for property 'charsToKeep'.
     */
    public String getCharsToKeep() {
        return charsToKeep;
    }

    /**
     * Setter for property 'charsToKeep'.
     *
     * @param charsToKeep Value to set for property 'charsToKeep'.
     */
    public void setCharsToKeep(String charsToKeep) {
        this.charsToKeep = charsToKeep;
    }
}
