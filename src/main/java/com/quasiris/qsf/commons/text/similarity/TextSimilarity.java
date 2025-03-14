package com.quasiris.qsf.commons.text.similarity;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.*;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class TextSimilarity {

    public double compare(String textLeft, String textRight) throws IOException {
        // Compute TF-IDF vectors for both texts
        Map<String, Double> leftVector = computeTFIDF(textLeft);
        Map<String, Double> rightVector = computeTFIDF(textRight);

        // Compute cosine similarity between the vectors
        return computeCosineSimilarity(leftVector, rightVector);
    }

    public Map<String, Double> computeTFIDF(String text) throws IOException {
        RAMDirectory ramDirectory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());
        IndexWriter writer = new IndexWriter(ramDirectory, config);

        // Index the single document
        Document luceneDoc = new Document();
        luceneDoc.add(new TextField("content", text, Field.Store.YES));
        writer.addDocument(luceneDoc);
        writer.close();

        // Compute TF-IDF for this document
        IndexReader reader = DirectoryReader.open(ramDirectory);
        Map<String, Integer> termFreq = tokenize(text);
        Map<String, Double> tfidf = new HashMap<>();

        for (String term : termFreq.keySet()) {
            int docFreq = reader.docFreq(new Term("content", term));
            double idf = Math.log((double) reader.numDocs() / (docFreq + 1)) + 1; // IDF formula
            double tfidfScore = termFreq.get(term) * idf;
            tfidf.put(term, tfidfScore);
        }
        reader.close();
        return tfidf;
    }

    private Map<String, Integer> tokenize(String text) throws IOException {
        Analyzer analyzer = new SimpleAnalyzer();
        Map<String, Integer> termFreq = new HashMap<>();

        try (var tokenStream = analyzer.tokenStream("content", new StringReader(text))) {
            CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = attr.toString();
                termFreq.put(term, termFreq.getOrDefault(term, 0) + 1);
            }
            tokenStream.end();
        }
        return termFreq;
    }

    private double computeCosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        Set<String> words = new HashSet<>();
        words.addAll(vec1.keySet());
        words.addAll(vec2.keySet());

        double dotProduct = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (String word : words) {
            double val1 = vec1.getOrDefault(word, 0.0);
            double val2 = vec2.getOrDefault(word, 0.0);
            dotProduct += val1 * val2;
            norm1 += val1 * val1;
            norm2 += val2 * val2;
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
