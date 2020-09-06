package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlDecodeNormalizeFilter implements NormalizerFilter {

    private String encoding = "UTF-8";

    @Override
    public String normalize(String text) {
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
