package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;

public class TrimTransformerFilter implements TransformerFilter {

    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }
        return text.trim();
    }
}
