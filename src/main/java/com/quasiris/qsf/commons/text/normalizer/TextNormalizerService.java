package com.quasiris.qsf.commons.text.normalizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.charfilter.MappingCharFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilterFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Normalize text and prepare for embedding
 */
public class TextNormalizerService {
    private Analyzer analyzer;

    public TextNormalizerService(NormalizerConfig config) {
        String enabledCharsRegex = config.isKeepPunctuation() ? "[^a-zA-Z0-9ÄÖÜäöüß.,!?:-]+" : "[^a-zA-Z0-9ÄÖÜäöüß-]+";
        try {
            CustomAnalyzer.Builder builder = CustomAnalyzer.builder(Paths.get("."))
                    .withTokenizer("whitespace")
                    .addCharFilter("patternReplace", "pattern", "\\\\n|\\\\r\\\\n|\\n|\\r\\n", "replacement", " ") // remove escaped newlines
                    .addCharFilter("patternReplace", "pattern", "(\\w+)[.|!]{1}(\\w+)", "replacement", "$1-$2") // replace Fritz!Box or Fritz.Box to Fritz-Box
                    .addCharFilter(HTMLStripCharFilterFactory.class)
                    .addCharFilter("patternReplace", "pattern", "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "replacement", " ") // remove urls
                    .addCharFilter("patternReplace", "pattern", enabledCharsRegex, "replacement", " ")
                    .addCharFilter("patternReplace", "pattern", "(?<=[\\s])-(?=[\\s])|(?<=[\\S])-(?=[\\s])|(?<=[\\s])-(?=[\\S])", "replacement", " ") // remove hyphen between whitespaces
                    .addCharFilter("patternReplace", "pattern", "\\b((?:[\\w|ÄÖÜäöüß]{1}[-!._]{1})+[\\w|ÄÖÜäöüß]{1})\\b", "replacement", " "); // remove abbreviations
            if(config.isNormalizeUmlaut()) {
                builder.addCharFilter(MappingCharFilterFactory.class, "mapping", "normalizer/umlaute-mapping.txt");
            }
            builder.addTokenFilter("lowercase");
            if(StringUtils.isNotEmpty(config.getStopwordFilepath())) {
                builder.addTokenFilter("stop", "ignoreCase", "false", "words", config.getStopwordFilepath());
            }
            if(config.isStem()) {
                builder.addTokenFilter("germanLightStem");
            }
            if(StringUtils.isNotEmpty(config.getSynonymsFilepath())) {
                builder.addTokenFilter(SynonymGraphFilterFactory.class, "synonyms", config.getSynonymsFilepath());
            }
            if(config.isRemoveDuplicates()) {
                builder.addTokenFilter("removeDuplicates");
            }
            builder.addTokenFilter("trim");
            analyzer = builder.build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String normalize(String text) {
        String result = text;
        if(StringUtils.isNotEmpty(result)) {
            try {
                List<String> tokens = analyze(text, analyzer);
                if(tokens != null) {
                    result = StringUtils.join(tokens, " ");
                }
            } catch (IOException ignored) {
            }
        }

        return result;
    }

    public static String normalizeWhitespace(String text) {
        String result = text;
        if(StringUtils.isNotEmpty(result)) {
            result = result.trim().replaceAll(" +", " ");
        }
        return result;
    }

    public static List<String> analyze(String text, Analyzer analyzer) throws IOException {
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
