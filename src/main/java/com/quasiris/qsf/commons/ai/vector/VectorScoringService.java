package com.quasiris.qsf.commons.ai.vector;

import com.quasiris.qsf.commons.ai.dto.TextRelevancyVector;
import com.quasiris.qsf.commons.ai.dto.TextVector;

import java.util.ArrayList;
import java.util.List;

public class VectorScoringService {
    VectorScoring vectorScoring;

    public VectorScoringService() {
        this.vectorScoring = new CosineSimilarityScoring();
    }

    public VectorScoringService(VectorScoring vectorScoring) {
        this.vectorScoring = vectorScoring;
    }

    public List<TextRelevancyVector> score(TextVector queryVector, List<TextVector> textVectors) {
        List<TextRelevancyVector> textRelevancyVectors = new ArrayList<>();

        for (int i = 0; i < textVectors.size(); i++) {
            TextVector textVector = textVectors.get(i);
            Double score = 0.0;
            if(textVector.getVector() != null) {
                score = vectorScoring.score(queryVector.getVector(), textVector.getVector());
                textRelevancyVectors.add(new TextRelevancyVector(textVector, score, i));
            }
        }

        return textRelevancyVectors;
    }
}
