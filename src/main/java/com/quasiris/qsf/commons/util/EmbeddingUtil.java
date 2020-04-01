package com.quasiris.qsf.commons.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmbeddingUtil {
    public static Boolean isZeroVector(Double[] vector) {
        return vector == null || isZeroVector(Arrays.asList(vector));
    }

    public static Boolean isZeroVector(List<Double> vector) {
        boolean result = true;
        if(vector != null) {
            Set<Double> set = new HashSet<Double>(vector);
            result = set.size() == 1 && set.contains(0.0);
        }
        return result;
    }
}
