package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;
import org.apache.commons.codec.digest.DigestUtils;

public class Md5NormalizeFilter implements NormalizerFilter {


    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }
        return DigestUtils.md5Hex(text);
    }
}
