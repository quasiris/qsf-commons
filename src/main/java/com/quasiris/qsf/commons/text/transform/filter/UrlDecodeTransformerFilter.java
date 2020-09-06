package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class UrlDecodeTransformerFilter implements TransformerFilter {

    private String encoding = "UTF-8";

    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }

        try {
            return URLDecoder.decode(text, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
