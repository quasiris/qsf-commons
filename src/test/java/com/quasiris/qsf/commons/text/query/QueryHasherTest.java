package com.quasiris.qsf.commons.text.query;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class QueryHasherTest {

    @Test
    void testHashing() {
        // Arrange
        Set<String> stopwords = Set.of("der", "die", "das", "ihr", "wir", "und", "oder", "von");
        QueryHasher hasher = new QueryHasher(10, stopwords);

        String expectedHash = "42916f1694b17dbe3b6b3ec1bfdb52be";
        assertEquals(expectedHash, hasher.normalize("wago klemme"));
        assertEquals(expectedHash, hasher.normalize("Wago klemme"));
        assertEquals(expectedHash, hasher.normalize("die klemme von Wago"));
    }
    @Test
    void testProcessText_withStopwordsAndMaxLength10() {
        // Arrange
        Set<String> stopwords = Set.of("the", "is", "a", "this", "to", "and");
        QueryHasher hasher = new QueryHasher(10, stopwords);
        String input = "Hello, world! This is a sample text to demonstrate the 123 TokenProcessor.";

        // Act
        String result = hasher.normalize(input);

        // Assert
        String expectedHash = "f6caa8be09ab3afb5260ca3b83f14a75"; // Precomputed hash
        assertEquals(expectedHash, result);
    }

    @Test
    void testProcessText_withDifferentMaxLength() {
        // Arrange
        Set<String> stopwords = Set.of("the", "is", "a", "this", "to", "and");
        QueryHasher hasher = new QueryHasher(10, stopwords);
        String input = "Hello, world! This is a sample text to demonstrate the 123 TokenProcessor.";

        // Act
        String result = hasher.normalize(input);

        // Assert
        String expectedHash = "f6caa8be09ab3afb5260ca3b83f14a75"; // Precomputed hash
        assertEquals(expectedHash, result);
    }

    @Test
    void testProcessText_withEmptyInput() {
        // Arrange
        Set<String> stopwords = Set.of("the", "is", "a", "this", "to", "and");
        QueryHasher hasher = new QueryHasher(10, stopwords);
        String input = "";

        // Act
        String result = hasher.normalize(input);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testProcessText_withNullInput() {
        // Arrange
        Set<String> stopwords = Set.of("the", "is", "a", "this", "to", "and");
        QueryHasher hasher = new QueryHasher(10, stopwords);

        // Act
        String result = hasher.normalize(null);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testProcessText_withoutStopwords() {
        // Arrange
        Set<String> stopwords = Set.of();
        QueryHasher hasher = new QueryHasher(10, stopwords);
        String input = "Hello, world! This is a test.";

        // Act
        String result = hasher.normalize(input);

        // Assert
        String expectedHash = "6cdd590040e6cacb23f327bec7e3305e"; // Precomputed hash
        assertEquals(expectedHash, result);
    }

    @Test
    void testProcessText_withOnlyStopwords() {
        // Arrange
        Set<String> stopwords = Set.of("the", "is", "a", "this", "to", "and");
        QueryHasher hasher = new QueryHasher(10, stopwords);
        String input = "The is a this to and.";

        // Act
        String result = hasher.normalize(input);

        // Assert
        assertEquals("", result);
    }
}
