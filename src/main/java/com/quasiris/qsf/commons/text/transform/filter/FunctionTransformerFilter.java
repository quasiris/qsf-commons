package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;

import java.util.function.Function;

public class FunctionTransformerFilter implements TransformerFilter {


    private Function<String, String> function;

    public FunctionTransformerFilter(Function<String, String> function) {
        this.function = function;
    }

    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }
        return function.apply(text);
    }
}
