package com.quasiris.qsf.commons.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TextRelevancyVector extends TextVector {
    private Double relevancy;
    private Integer index;

    public TextRelevancyVector() {
    }

    public TextRelevancyVector(String text, String normalized, Double[] vector) {
        super(text, normalized, vector);
    }

    public TextRelevancyVector(String text, String normalized, Double[] vector, Double relevancy) {
        super(text, normalized, vector);
        this.setRelevancy(relevancy);
    }

    public TextRelevancyVector(TextVector copy) {
        super(copy.getText(), copy.getNormalized(), copy.getVector());
    }

    public TextRelevancyVector(TextVector copy, Double relevancy, Integer index) {
        super(copy.getText(), copy.getNormalized(), copy.getVector());
        this.setRelevancy(relevancy);
        this.setIndex(index);
    }

    public Double getRelevancy() {
        return relevancy;
    }

    public void setRelevancy(Double relevancy) {
        this.relevancy = relevancy;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @JsonIgnore
    @Override
    public Double[] getVector() {
        return super.getVector();
    }
}
