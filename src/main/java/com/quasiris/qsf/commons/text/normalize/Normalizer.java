package com.quasiris.qsf.commons.text.normalize;

import java.util.List;

public class Normalizer {


    private List<NormalizerFilter> filters;

    public Normalizer(List<NormalizerFilter> filters) {
        this.filters = filters;
    }

    public Normalizer() {
    }

    public String normalize(String text) {

        for(NormalizerFilter filter : filters) {
            text = filter.normalize(text);
        }
        return text;
    }

    /**
     * Getter for property 'filters'.
     *
     * @return Value for property 'filters'.
     */
    public List<NormalizerFilter> getFilters() {
        return filters;
    }

    /**
     * Setter for property 'filters'.
     *
     * @param filters Value to set for property 'filters'.
     */
    public void setFilters(List<NormalizerFilter> filters) {
        this.filters = filters;
    }
}
