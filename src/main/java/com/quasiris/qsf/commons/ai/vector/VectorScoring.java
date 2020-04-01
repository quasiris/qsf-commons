package com.quasiris.qsf.commons.ai.vector;

public interface VectorScoring {
    Double score(Double[] vectorA, Double[] vectorB);
}
