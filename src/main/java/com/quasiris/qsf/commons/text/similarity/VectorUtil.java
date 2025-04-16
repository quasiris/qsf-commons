package com.quasiris.qsf.commons.text.similarity;

import java.util.List;

public class VectorUtil {

    public static Double[] computeAverageVector(List<Double[]> vectors) {
        if (vectors.isEmpty()) {
            return null; // Return null if there are no vectors
        }

        int vectorSize = vectors.get(0).length;
        Double[] averageVector = new Double[vectorSize];

        // Initialize the averageVector with zeros
        for (int i = 0; i < vectorSize; i++) {
            averageVector[i] = 0.0;
        }

        // Sum up all vectors
        for (Double[] vector : vectors) {
            if (vector.length != vectorSize) {
                throw new IllegalArgumentException("All vectors must have the same length");
            }
            for (int i = 0; i < vectorSize; i++) {
                averageVector[i] += vector[i];
            }
        }

        // Divide by the number of vectors to compute the mean
        int numVectors = vectors.size();
        for (int i = 0; i < vectorSize; i++) {
            averageVector[i] /= numVectors;
        }

        return averageVector;
    }
}
