package com.quasiris.qsf.commons.text.transform;

import com.quasiris.qsf.commons.text.transform.filter.FunctionTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.GermanUmlautTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.LowerCaseTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.RemoveCharTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.RemoveNumberTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.StemmingTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.TrimAllTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.TrimTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.UrlDecodeTransformerFilter;
import com.quasiris.qsf.commons.text.transform.filter.UrlEncodeTransformerFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class TextTransformerFactory {

    private static final Map<String, TransformerFilter> factoryMap = new HashMap<>();
    static {
        factoryMap.put("lowerCase", new LowerCaseTransformerFilter());
        factoryMap.put("upperCase", new LowerCaseTransformerFilter());
        factoryMap.put("replaceGermanUmlauts", new GermanUmlautTransformerFilter());
        factoryMap.put("removeSpecialChars", new RemoveCharTransformerFilter());
        factoryMap.put("removeNumbers", new RemoveNumberTransformerFilter());
        factoryMap.put("trim", new TrimTransformerFilter());
        factoryMap.put("trimAll", new TrimAllTransformerFilter());
        factoryMap.put("removeWhitespace", new RemoveCharTransformerFilter(" "));
        factoryMap.put("urlEncode", new UrlEncodeTransformerFilter());
        factoryMap.put("urlDecode", new UrlDecodeTransformerFilter());
        factoryMap.put("md5", new UrlDecodeTransformerFilter());
        factoryMap.put("stemming", new StemmingTransformerFilter());
        factoryMap.put("germanLightStem", new StemmingTransformerFilter());
    }

    public static void register(String filterName, TransformerFilter normalizerFilter) {
        factoryMap.put(filterName, normalizerFilter);
    }

    public static void register(String filterName, Function<String, String> function) {
        FunctionTransformerFilter functionNormalizeFilter = new FunctionTransformerFilter(function);
        factoryMap.put(filterName, functionNormalizeFilter);
    }


    public static TransformerFilter createFilter(String filterName) {
        TransformerFilter normalizerFilter = factoryMap.get(filterName);
        if(normalizerFilter != null) {
            return normalizerFilter;
        }
        throw new IllegalArgumentException("The filterName " + filterName + " does not exists.");
    }


    public static TextTransformer create(List<String> filterNames) {

        List<TransformerFilter> filters = new ArrayList<>();
        for(String filterName : filterNames) {
            TransformerFilter normalizerFilter = createFilter(filterName);
            filters.add(normalizerFilter);
        }
        TextTransformer normalizer = new TextTransformer(filters);
        return normalizer;
    }

    public static TextTransformer create(String filterNames) {
        List<String> filterNameList = Arrays.asList(filterNames.split(Pattern.quote("|")));
        return create(filterNameList);
    }
}