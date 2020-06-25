package com.quasiris.qsf.commons.ai.embedding;

import com.quasiris.qsf.commons.ai.dto.Document;
import com.quasiris.qsf.commons.ai.dto.TextVector;
import com.quasiris.qsf.commons.ai.dto.TextVectorDocument;
import com.quasiris.qsf.commons.exception.NormalizerNotSupportedException;
import com.quasiris.qsf.commons.text.normalizer.TextNormalizerService;

import java.util.List;

/**
 * Transform Query to Vector with AI model
 */
public interface TextEmbeddingEncoder {

    /**
     * Embed text into vector
     * @param text that should be embedded
     * @param normalizer profile. No normalizatin will be performed if empty
     * @param autosplit split text if true
     * @return list or empty list
     * @throws NormalizerNotSupportedException if not supported
     */
    List<TextVector> embed(String text, TextNormalizerService normalizer, boolean autosplit) throws NormalizerNotSupportedException;

    /**
     * Embed doc into vector
     * @param doc that should be embedded
     * @param normalizer profile. No normalizatin will be performed if empty
     * @param autosplit split text if true
     * @return vectorized document
     * @throws NormalizerNotSupportedException if not supported
     */
    TextVectorDocument embed(Document<String> doc, TextNormalizerService normalizer, boolean autosplit) throws NormalizerNotSupportedException;

    /**
     * Embed docs into vector
     * @param docs that should be embedded
     * @param normalizer profile. No normalizatin will be performed if empty
     * @param autosplit split text if true
     * @return vectorized documents
     * @throws NormalizerNotSupportedException if not supported
     */
    List<TextVectorDocument> embedBulk(List<Document<String>> docs, TextNormalizerService normalizer, boolean autosplit) throws NormalizerNotSupportedException;
}
