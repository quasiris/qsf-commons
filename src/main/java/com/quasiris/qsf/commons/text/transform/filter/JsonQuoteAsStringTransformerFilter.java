package com.quasiris.qsf.commons.text.transform.filter;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.quasiris.qsf.commons.text.transform.TransformerFilter;

public class JsonQuoteAsStringTransformerFilter implements TransformerFilter {

    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }
        return new String(JsonStringEncoder.getInstance().quoteAsString(text));
    }
}
