package com.quasiris.qsf.commons.text.transform;

import java.util.List;

public class TextTransformer {


    private List<TransformerFilter> filters;

    public TextTransformer(List<TransformerFilter> filters) {
        this.filters = filters;
    }

    public TextTransformer() {
    }

    public String normalize(String text) {

        for(TransformerFilter filter : filters) {
            text = filter.transform(text);
        }
        return text;
    }

    /**
     * Getter for property 'filters'.
     *
     * @return Value for property 'filters'.
     */
    public List<TransformerFilter> getFilters() {
        return filters;
    }

    /**
     * Setter for property 'filters'.
     *
     * @param filters Value to set for property 'filters'.
     */
    public void setFilters(List<TransformerFilter> filters) {
        this.filters = filters;
    }


}
