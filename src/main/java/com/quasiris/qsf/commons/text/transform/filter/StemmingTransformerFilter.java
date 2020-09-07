package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;

import java.util.Collections;

public class StemmingTransformerFilter extends LuceneTransformerFilter implements TransformerFilter {

    public StemmingTransformerFilter() {
        super();


        init(Collections.emptyList(), Collections.singletonList("germanLightStem"));
    }
}
