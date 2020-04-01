package com.quasiris.qsf.commons.ai.vector;

public class CosineSimilarityScoring implements VectorScoring {
    @Override
    public Double score(Double[] vectorA, Double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        Double score = 0.0;
        if(vectorA != null && vectorB != null) {
            for (int i = 0; i < vectorA.length; i++) {
                dotProduct += vectorA[i] * vectorB[i];
                normA += Math.pow(vectorA[i], 2);
                normB += Math.pow(vectorB[i], 2);
            }
            score = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }
        return score;
    }
}
