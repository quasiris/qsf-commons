package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlEncodeNormalizeFilter implements NormalizerFilter {

    private String encoding = "UTF-8";

    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }

        try {
            return URLEncoder.encode(text, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
