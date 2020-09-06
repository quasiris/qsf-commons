package com.quasiris.qsf.commons.text.normalize;

import com.quasiris.qsf.commons.text.normalize.filter.FunctionNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.GermanUmlautNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.LowerCaseNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.RemoveCharNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.RemoveNumberNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.TrimAllNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.TrimNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.UrlDecodeNormalizeFilter;
import com.quasiris.qsf.commons.text.normalize.filter.UrlEncodeNormalizeFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class NormalizerFactory {

    private static final Map<String, NormalizerFilter> factoryMap = new HashMap<>();
    static {
        factoryMap.put("lowerCase", new LowerCaseNormalizeFilter());
        factoryMap.put("upperCase", new LowerCaseNormalizeFilter());
        factoryMap.put("replaceGermanUmlauts", new GermanUmlautNormalizeFilter());
        factoryMap.put("removeSpecialChars", new RemoveCharNormalizeFilter());
        factoryMap.put("removeNumbers", new RemoveNumberNormalizeFilter());
        factoryMap.put("trim", new TrimNormalizeFilter());
        factoryMap.put("trimAll", new TrimAllNormalizeFilter());
        factoryMap.put("removeWhitespace", new RemoveCharNormalizeFilter(" "));
        factoryMap.put("urlEncode", new UrlEncodeNormalizeFilter());
        factoryMap.put("urlDecode", new UrlDecodeNormalizeFilter());
        factoryMap.put("md5", new UrlDecodeNormalizeFilter());
    }

    public static void register(String filterName, NormalizerFilter normalizerFilter) {
        factoryMap.put(filterName, normalizerFilter);
    }

    public static void register(String filterName, Function<String, String> function) {
        FunctionNormalizeFilter functionNormalizeFilter = new FunctionNormalizeFilter(function);
        factoryMap.put(filterName, functionNormalizeFilter);
    }


    public static NormalizerFilter createFilter(String filterName) {
        NormalizerFilter normalizerFilter = factoryMap.get(filterName);
        if(normalizerFilter != null) {
            return normalizerFilter;
        }
        throw new IllegalArgumentException("The filterName " + filterName + " does not exists.");
    }


    public static Normalizer create(List<String> filterNames) {

        List<NormalizerFilter> filters = new ArrayList<>();
        for(String filterName : filterNames) {
            NormalizerFilter normalizerFilter = createFilter(filterName);
            filters.add(normalizerFilter);
        }
        Normalizer normalizer = new Normalizer(filters);
        return normalizer;
    }

    public static Normalizer create(String filterNames) {
        List<String> filterNameList = Arrays.asList(filterNames.split(Pattern.quote("|")));
        return create(filterNameList);
    }
}
