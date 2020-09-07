package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;

import java.util.ArrayList;
import java.util.List;

public class RemoveHtmlTransformerFilter extends LuceneTransformerFilter implements TransformerFilter {

    public RemoveHtmlTransformerFilter() {
        super();
        List<String> charFilters = new ArrayList<>();
        charFilters.add("HTMLStrip");
        init(charFilters, new ArrayList<>());
    }
}
