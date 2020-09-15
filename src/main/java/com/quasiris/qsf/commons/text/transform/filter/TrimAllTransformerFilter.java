package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.TextUtils;
import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.apache.commons.lang3.StringUtils;

public class TrimAllTransformerFilter implements TransformerFilter {


    private String trimChars;

    public TrimAllTransformerFilter() {
    }

    public TrimAllTransformerFilter(String trimChars) {
        this.trimChars = trimChars;
    }

    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }

        if(trimChars == null) {
            return TextUtils.strip(text);
        }
        return StringUtils.strip(text, trimChars);
    }

    /**
     * Getter for property 'trimChars'.
     *
     * @return Value for property 'trimChars'.
     */
    public String getTrimChars() {
        return trimChars;
    }

    /**
     * Setter for property 'trimChars'.
     *
     * @param trimChars Value to set for property 'trimChars'.
     */
    public void setTrimChars(String trimChars) {
        this.trimChars = trimChars;
    }
}
