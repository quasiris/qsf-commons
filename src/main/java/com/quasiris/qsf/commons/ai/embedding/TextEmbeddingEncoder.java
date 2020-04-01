package com.quasiris.qsf.commons.ai.embedding;

import com.quasiris.qsf.commons.ai.dto.TextVector;
import com.quasiris.qsf.commons.exception.NormalizerNotSupportedException;
import com.quasiris.qsf.commons.text.normalizer.TextNormalizerService;

import java.util.List;

/**
 * Transform Query to Vector with AI model
 */
public interface TextEmbeddingEncoder {

    /**
     * Embed text into vector
     * @param text
     * @param normalizer profile. No normalizatin will be performed if empty
     * @param autosplit split text if true
     * @return list or empty list
     */
    List<TextVector> embed(String text, TextNormalizerService normalizer, boolean autosplit) throws NormalizerNotSupportedException;
}
