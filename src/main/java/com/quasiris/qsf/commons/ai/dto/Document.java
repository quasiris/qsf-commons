package com.quasiris.qsf.commons.ai.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class Document<T> {
    private String id;
    private Map<String, T> fields = new LinkedHashMap<>();

    public Document() {
    }

    public Document(String id) {
        this.id = id;
    }

    public Document(String id, Map<String, T> fields) {
        this.id = id;
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, T> getFields() {
        return fields;
    }

    public void setFields(Map<String, T> fields) {
        this.fields = fields;
    }
}
