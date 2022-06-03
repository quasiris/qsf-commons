package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.dto.elasticsearch.IndexAction;
import com.quasiris.qsf.dto.elasticsearch.IndexDocument;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElasticDocSerializerTest {
    String index = "junit-index";

    @Test
    void serializeBulk() {
        // given
        IndexAction indexAction = new IndexAction();
        indexAction.setIndex(index);
        indexAction.setType("my-type");
        indexAction.setOpType(IndexAction.OpType.CREATE);
        indexAction.setDoc(new IndexDocument("junit-doc", ElasticDataClientTest.createDocSource()));

        // when
        String ndJsonDoc = ElasticDocSerializer.serializeBulk(indexAction);

        // then
        assertEquals(
                "{\"create\" : {\"_index\" : \"junit-index\", \"_type\" : \"my-type\", \"_id\" : \"junit-doc\"} } \n" +
                "{\"price\":123.95,\"onStock\":true,\"title\":\"The Title\"}", ndJsonDoc);
    }

    @Test
    void serializeBulk_dontAppendDefaultType() {
        // given
        IndexAction indexAction = new IndexAction();
        indexAction.setIndex(index);
        indexAction.setOpType(IndexAction.OpType.CREATE);
        indexAction.setDoc(new IndexDocument("junit-doc", ElasticDataClientTest.createDocSource()));

        // when
        String ndJsonDoc = ElasticDocSerializer.serializeBulk(indexAction);

        // then
        assertEquals(
                "{\"create\" : {\"_index\" : \"junit-index\", \"_id\" : \"junit-doc\"} } \n" +
                        "{\"price\":123.95,\"onStock\":true,\"title\":\"The Title\"}", ndJsonDoc);
    }
}