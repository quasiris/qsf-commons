package com.quasiris.qsf.commons.text.normalize.filter;

import com.quasiris.qsf.commons.text.SpecialChars;
import com.quasiris.qsf.commons.text.normalize.NormalizerFilter;

public class RemoveNumberNormalizeFilter extends  RemoveCharNormalizeFilter implements NormalizerFilter {


    public RemoveNumberNormalizeFilter() {
        super(SpecialChars.NUMBERS);
    }

}
