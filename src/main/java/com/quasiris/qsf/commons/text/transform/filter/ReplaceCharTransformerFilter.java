package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.SpecialChars;
import com.quasiris.qsf.commons.text.transform.TransformerFilter;

public class ReplaceCharTransformerFilter implements TransformerFilter {

    private String charsToRemove = SpecialChars.ALL;
    private String replaceChar = " ";

    public ReplaceCharTransformerFilter() {
    }

    public ReplaceCharTransformerFilter(String charsToRemove) {
        this.charsToRemove = charsToRemove;
    }


    public ReplaceCharTransformerFilter(String charsToRemove, String replaceChar) {
        this.charsToRemove = charsToRemove;
        this.replaceChar = replaceChar;
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
            } else {
                sb.append(replaceChar);
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

    public String getReplaceChar() {
        return replaceChar;
    }

    public void setReplaceChar(String replaceChar) {
        this.replaceChar = replaceChar;
    }
}
