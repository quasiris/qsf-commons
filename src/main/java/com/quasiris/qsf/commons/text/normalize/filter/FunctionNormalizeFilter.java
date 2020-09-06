package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

import java.util.function.Function;

public class FunctionNormalizeFilter implements NormalizerFilter {


    private Function<String, String> function;

    public FunctionNormalizeFilter(Function<String, String> function) {
        this.function = function;
    }

    @Override
    public String normalize(String text) {
        if(text == null) {
            return null;
        }
        return function.apply(text);
    }
}
