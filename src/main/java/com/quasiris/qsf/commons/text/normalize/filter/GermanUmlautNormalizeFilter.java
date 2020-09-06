package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.TextUtils;
import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

public class GermanUmlautNormalizeFilter implements NormalizerFilter {

    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }
        return TextUtils.replaceGermanUmlaut(text);
    }
}
