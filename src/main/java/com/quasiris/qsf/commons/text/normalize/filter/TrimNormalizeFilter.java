package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

public class TrimNormalizeFilter implements NormalizerFilter {

    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }
        return text.trim();
    }
}
