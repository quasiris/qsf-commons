package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.SpecialChars;
import com.quasiris.qsf.commons.text.transform.TransformerFilter;

public class RemoveNumberTransformerFilter extends RemoveCharTransformerFilter implements TransformerFilter {


    public RemoveNumberTransformerFilter() {
        super(SpecialChars.NUMBERS);
    }

}
