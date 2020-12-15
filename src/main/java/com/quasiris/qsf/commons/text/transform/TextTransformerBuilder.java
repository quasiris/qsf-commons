package com.quasiris.qsf.commons.text.transform;

import com.quasiris.qsf.commons.text.transform.filter.FunctionTransformerFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class TextTransformerBuilder {
    private List<TransformerFilter> filters = new ArrayList<>();

    private TextTransformerBuilder() {
    }

    public static TextTransformerBuilder create() {
        return new TextTransformerBuilder();
    }

    public TextTransformerBuilder lowerCase() {
        addFilter("lowerCase");
        return this;
    }

    public TextTransformerBuilder upperCase() {
        addFilter("upperCase");
        return this;
    }


    public TextTransformerBuilder replaceGermanUmlauts() {
        addFilter("replaceGermanUmlauts");
        return this;
    }

    public TextTransformerBuilder removeSpecialChars() {
        addFilter("removeSpecialChars");
        return this;
    }


    public TextTransformerBuilder removeNumbers() {
        addFilter("removeNumbers");
        return this;
    }


    public TextTransformerBuilder trim() {
        addFilter("trim");
        return this;
    }


    public TextTransformerBuilder trimAll() {
        addFilter("trimAll");
        return this;
    }

    public TextTransformerBuilder removeMultipleWhitspaces() {
        addFilter("removeMultipleWhitspaces");
        return this;
    }

    public TextTransformerBuilder removeWhitespace() {
        addFilter("removeWhitespace");
        return this;
    }


    public TextTransformerBuilder urlEncode() {
        addFilter("urlEncode");
        return this;
    }


    public TextTransformerBuilder urlDecode() {
        addFilter("urlDecode");
        return this;
    }



    public TextTransformerBuilder md5() {
        addFilter("md5");
        return this;
    }


    public TextTransformerBuilder stemming() {
        addFilter("stemming");
        return this;
    }


    public TextTransformerBuilder germanLightStem() {
        addFilter("germanLightStem");
        return this;
    }

    public TextTransformerBuilder function(Function<String, String> function) {
        FunctionTransformerFilter functionTransformerFilter = new FunctionTransformerFilter(function);
        this.filters.add(functionTransformerFilter);
        return this;
    }

    public TextTransformerBuilder addFilter(String filterName) {
        TransformerFilter transformerFilter = TextTransformerFactory.createFilter(filterName);
        filters.add(transformerFilter);
        return this;
    }

    public TextTransformerBuilder addFilter(TransformerFilter transformerFilter) {
        filters.add(transformerFilter);
        return this;
    }

    public TextTransformer build() {
        TextTransformer textTransformer = new TextTransformer();
        textTransformer.setFilters(filters);
        return textTransformer;
    }
}
