package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import com.quasiris.qsf.commons.text.SpecialChars;

public class RemoveCharTransformerFilter implements TransformerFilter {

    private String charsToRemove = SpecialChars.ALL;

    public RemoveCharTransformerFilter() {
    }

    public RemoveCharTransformerFilter(String charsToRemove) {
        this.charsToRemove = charsToRemove;
    }

    @Override
    public String transform(String text) {
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
