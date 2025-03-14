package com.quasiris.qsf.commons.text.similarity;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextSimilarityTest {

    private final TextSimilarity textSimilarity = new TextSimilarity();

    @Test
    void testSimilarTexts() throws IOException {
        String text1 = "Artificial Intelligence is transforming the world.";
        String text2 = "Machine Learning is a subset of Artificial Intelligence.";

        double similarity = textSimilarity.compare(text1, text2);
        assertTrue(similarity > 0.2, "Expected similarity to be greater than 0.2");
    }

    @Test
    void testIdenticalTexts() throws IOException {
        String text1 = "This is an example sentence.";
        String text2 = "This is an example sentence.";

        double similarity = textSimilarity.compare(text1, text2);
        assertEquals(1.0, similarity, 0.01, "Expected similarity to be close to 1.0");
    }

    @Test
    void testDifferentTexts() throws IOException {
        String text1 = "The sun is shining brightly today.";
        String text2 = "Deep learning models require a lot of data.";

        double similarity = textSimilarity.compare(text1, text2);
        assertTrue(similarity < 0.2, "Expected similarity to be less than 0.2");
    }
}
