package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import com.quasiris.qsf.commons.text.SpecialChars;
import org.apache.commons.lang3.StringUtils;

public class TrimAllNormalizeFilter implements NormalizerFilter {


    private String trimChars;

    public TrimAllNormalizeFilter() {
        trimChars = " " + SpecialChars.ALL;
    }

    public TrimAllNormalizeFilter(String trimChars) {
        this.trimChars = trimChars;
    }

    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
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
