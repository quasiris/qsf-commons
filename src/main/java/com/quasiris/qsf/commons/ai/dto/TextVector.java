package com.quasiris.qsf.commons.ai.dto;

import java.io.Serializable;

public class TextVector implements Serializable {
    private String text;
    private String normalized;
    private Double[] vector;

    private Integer tokenCount;

    public TextVector() {
    }

    public TextVector(String text, String normalized, Double[] vector) {
        this.text = text;
        this.normalized = normalized;
        this.vector = vector;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNormalized() {
        return normalized;
    }

    public void setNormalized(String normalized) {
        this.normalized = normalized;
    }

    /**
     * @return double or null if vector contains zeros
     */
    public Double[] getVector() {
        return vector;
    }

    public void setVector(Double[] vector) {
        this.vector = vector;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }
}
