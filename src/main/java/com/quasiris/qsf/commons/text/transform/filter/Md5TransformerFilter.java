package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.apache.commons.codec.digest.DigestUtils;

public class Md5TransformerFilter implements TransformerFilter {


    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }
        return DigestUtils.md5Hex(text);
    }
}
