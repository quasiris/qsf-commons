package com.quasiris.qsf.commons.ai.embedding;

import com.quasiris.qsf.commons.ai.dto.Document;
import com.quasiris.qsf.commons.ai.dto.TextVector;
import com.quasiris.qsf.commons.ai.dto.TextVectorDocument;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BertAsAServiceEncoderTest {
    BertAsAServiceEncoder encoder = new BertAsAServiceEncoder("http://localhost:8125/encode", 30000);

    @Ignore // require external service for test
    @Test
    public void embed() {
        List<TextVector> textVectors = encoder.embed("convert this text into a vector please", null, false);

        assertNotNull(textVectors);
    }

    @Ignore // require external service for test
    @Test
    public void embedDoc() {
        Document doc = new Document("example-doc");
        doc.getFields().put("title", "convert this text into a vector please");
        doc.getFields().put("description", "this text will be converted into a vector.");
        TextVectorDocument vectorDoc = encoder.embed(doc, null, false);

        assertNotNull(vectorDoc);
        assertEquals(doc.getId(), vectorDoc.getId());
        assertEquals(2, vectorDoc.getFields().size());
    }

    @Ignore // require external service for test
    @Test
    public void embedBulk() {
        List<Document<String>> docs = new ArrayList<>();
        Document doc = new Document("example-doc");
        doc.getFields().put("title", "convert this text into a vector please");
        doc.getFields().put("emptyfield", "");
        doc.getFields().put("description", "this text will be converted into a vector.");
        doc.getFields().put("nullfield", null);
        docs.add(doc);
        Document doc2 = new Document("example-doc2");
        doc2.getFields().put("title", "convert this text also into a vector please");
        doc2.getFields().put("description", "this second text will be also converted into a vector.");
        docs.add(doc2);
        List<TextVectorDocument> vectorDocs = encoder.embedBulk(docs, null, false);

        assertNotNull(vectorDocs);
        assertEquals(2, vectorDocs.size());
    }
}