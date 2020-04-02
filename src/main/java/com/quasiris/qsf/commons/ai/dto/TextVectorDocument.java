package com.quasiris.qsf.commons.ai.dto;

import java.util.List;
import java.util.Map;

public class TextVectorDocument extends Document<List<TextVector>> {
    public TextVectorDocument() {
    }

    public TextVectorDocument(String id) {
        super(id);
    }

    public TextVectorDocument(String id, Map<String, List<TextVector>> fields) {
        super(id, fields);
    }
}
