package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LuceneTransformerFilter implements TransformerFilter {

    private String tokenizer = "whitespace";
    private List<String> charFilters;
    private List<String> tokenFilters;

    private Analyzer analyzer;

    public LuceneTransformerFilter() {
    }

    public LuceneTransformerFilter(List<String> charFilters,  List<String> tokenFilters) {
        init(charFilters, tokenFilters);
    }

    protected void init(List<String> charFilters,  List<String> tokenFilters) {
        this.charFilters = charFilters;
        this.tokenFilters = tokenFilters;
        try {
            CustomAnalyzer.Builder builder = CustomAnalyzer.
                    builder(Paths.get(".")).
                    withTokenizer(tokenizer);

            for(String charFilter : charFilters) {
                builder = builder.addCharFilter(charFilter);
            }

            for(String tokenFilter : tokenFilters) {
                builder = builder.addTokenFilter(tokenFilter);
            }
            analyzer = builder.build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }
        try {
            return String.join(" ", analyze(text));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> analyze(String text) throws IOException {
        List<String> result = new ArrayList<>();
        try(TokenStream tokenStream = analyzer.tokenStream("", text)) {
            CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                result.add(attr.toString());
            }
        }
        return result;
    }

}
