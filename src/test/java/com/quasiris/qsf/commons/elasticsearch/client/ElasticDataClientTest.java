package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.exception.ResourceAlreadyExistException;
import com.quasiris.qsf.commons.exception.ResourceNotFoundException;
import com.quasiris.qsf.commons.http.HttpResponse;
import com.quasiris.qsf.dto.elasticsearch.BulkRequest;
import com.quasiris.qsf.dto.elasticsearch.BulkResponse;
import com.quasiris.qsf.dto.elasticsearch.IndexAction;
import com.quasiris.qsf.dto.elasticsearch.IndexDocument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class ElasticDataClientTest {
    ElasticIndexClient indexClient = new ElasticIndexClient();
    ElasticDataClient dataClient = new ElasticDataClient();
    String baseUrl = "http://localhost:9200";
    String index = "junit-index";
    String docId = "junit-doc1";
    Map<String, Object> docSource = createDocSource();

    @BeforeEach
    void setUp() {
        indexClient.create(baseUrl, index);
    }

    @AfterEach
    void tearDown() {
        indexClient.remove(baseUrl, index);
    }

    @Test
    void patch() {
        // given
        Map<String, Object> docPath = createDocSource();
        docPath.put("price", 115.45);
        docPath.put("offer", true);
        try {
            dataClient.create(baseUrl, index, docId, docSource);
        } catch (ResourceAlreadyExistException ignored) {
        }

        // when
        IndexDocument patched = null;
        try {
            patched = dataClient.patch(baseUrl, index, docId, docPath);
        } catch (ResourceNotFoundException ignored) {
        }

        // then
        assertNotNull(patched);
        assertEquals(115.45, patched.getFields().get("price"));
        assertEquals(true, patched.getFields().get("offer"));
    }

    @Test
    void patch_nonExisting() {
        // when - then
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> dataClient.patch(baseUrl, index, docId, docSource)
        );
    }

    @Test
    void create_new() throws ResourceAlreadyExistException {
        // when
        IndexDocument indexDocument = dataClient.create(baseUrl, index, docId, docSource);

        // then
        assertNotNull(indexDocument);
        assertEquals(docSource, indexDocument.getFields());
    }

    @Test
    void create_existing() throws ResourceAlreadyExistException {
        // when
        IndexDocument indexDocument = dataClient.create(baseUrl, index, docId, docSource);
        ResourceAlreadyExistException thrown = assertThrows(
                ResourceAlreadyExistException.class,
                () -> dataClient.create(baseUrl, index, docId, docSource)
        );
    }

    @Test
    void update() throws ResourceNotFoundException {
        // given
        Map<String, Object> docPath = createDocSource();
        docPath.put("price", 115.45);
        docPath.put("offer", true);
        try {
            dataClient.create(baseUrl, index, docId, docSource);
        } catch (ResourceAlreadyExistException ignored) {
        }

        // when
        IndexDocument updated = dataClient.update(baseUrl, index, docId, docPath);

        // then
        assertNotNull(updated);
        assertEquals(115.45, updated.getFields().get("price"));
        assertEquals(true, updated.getFields().get("offer"));
    }

    @Test
    void update_nonExistingEnabled() throws ResourceNotFoundException {
        // given
        Map<String, Object> docPath = createDocSource();

        // when
        IndexDocument created = dataClient.update(baseUrl, index, docId, docPath);

        // then
        assertNotNull(created);
        assertEquals(123.95, created.getFields().get("price"));
    }

    @Test
    void update_nonExistingNotEnabled() throws ResourceNotFoundException {
        // given
        Map<String, Object> docPath = createDocSource();
        ElasticDataClient dataClient = new ElasticDataClient(true);

        // when - then
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> dataClient.update(baseUrl, index, docId, docPath)
        );
    }

    @Test
    void findOne() throws ResourceNotFoundException {
        // given
        try {
            dataClient.create(baseUrl, index, docId, docSource);
        } catch (ResourceAlreadyExistException ignored) {
        }

        // when
        IndexDocument doc = dataClient.findOne(baseUrl, index, docId);

        // then
        assertNotNull(doc);
    }

    @Test
    void findOne_nonExisting() {
        // when - then
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> dataClient.findOne(baseUrl, index, docId)
        );
    }

    @Test
    void exists() {
        // given
        try {
            dataClient.create(baseUrl, index, docId, docSource);
        } catch (ResourceAlreadyExistException ignored) {
        }

        // when
        boolean exists = dataClient.exists(baseUrl, index, docId);

        // then
        assertTrue(exists);
    }

    @Test
    void exists_not() {
        // when
        boolean exists = dataClient.exists(baseUrl, index, docId);

        // then
        assertFalse(exists);
    }

    @Test
    void delete() throws ResourceAlreadyExistException {
        // given
        dataClient.create(baseUrl, index, docId, docSource);

        // when
        boolean deleted = false;
        try {
            deleted = dataClient.delete(baseUrl, index, docId);
        } catch (ResourceNotFoundException ignored) {
        }

        // then
        assertTrue(deleted);
    }

    @Test
    void delete_nonExisting() {
        // when - then
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> dataClient.delete(baseUrl, index, docId)
        );
    }

    @Test
    void bulk() {
        // given
        BulkRequest bulkRequest = new BulkRequest();
        for(int i = 1; i <= 3; i++) {
            IndexAction indexAction = new IndexAction();
            indexAction.setIndex(index);
            indexAction.setOpType(IndexAction.OpType.INDEX);
            indexAction.setDoc(new IndexDocument("junit-doc"+i, createDocSource()));
            bulkRequest.getActions().add(indexAction);
        }
        bulkRequest.setTimeout(10);

        // when
        HttpResponse<BulkResponse> bulkResponse = dataClient.bulk(baseUrl, bulkRequest);

        // then
        assertNotNull(bulkResponse);
        assertTrue(bulkResponse.is2xx());
        assertNotNull(bulkResponse.getPayload());
        assertFalse(bulkResponse.getPayload().getErrors());
        assertEquals(3, bulkResponse.getPayload().getItems().size());
        assertEquals(201, ((Map)(bulkResponse.getPayload().getItems().get(0).get("index"))).get("status"));
    }

    @Test
    void bulk_withErrors() {
        // given
        BulkRequest bulkRequest = new BulkRequest();
        for(int i = 1; i <= 3; i++) {
            IndexAction indexAction = new IndexAction();
            indexAction.setIndex(index);
            indexAction.setOpType(IndexAction.OpType.CREATE);
            indexAction.setDoc(new IndexDocument("junit-doc"+i, createDocSource()));
            bulkRequest.getActions().add(indexAction);
            bulkRequest.getActions().add(indexAction);
        }
        bulkRequest.setTimeout(10);

        // when
        HttpResponse<BulkResponse> bulkResponse = dataClient.bulk(baseUrl, bulkRequest);

        // then
        assertNotNull(bulkResponse);
        assertTrue(bulkResponse.is2xx());
        assertNotNull(bulkResponse.getPayload());
        assertTrue(bulkResponse.getPayload().getErrors());
        assertEquals(6, bulkResponse.getPayload().getItems().size());
        assertEquals("junit-doc1", ((Map)(bulkResponse.getPayload().getItems().get(0).get("create"))).get("_id"));
        assertEquals(201, ((Map)(bulkResponse.getPayload().getItems().get(0).get("create"))).get("status"));
        assertEquals("junit-doc1", ((Map)(bulkResponse.getPayload().getItems().get(1).get("create"))).get("_id"));
        assertEquals(409, ((Map)(bulkResponse.getPayload().getItems().get(1).get("create"))).get("status"));
    }

    public static Map<String, Object> createDocSource() {
        Map<String, Object> doc = new HashMap<>();
        doc.put("title", "The Title");
        doc.put("price", 123.95);
        doc.put("onStock", true);
        return doc;
    }

    @Test
    void count_zero() throws ResourceNotFoundException {
        // when
        Long count = dataClient.count(baseUrl, index);

        // then
        assertEquals(0, count);
    }

    @Test
    void count_multiple() throws ResourceNotFoundException {
        // given
        BulkRequest bulkRequest = new BulkRequest();
        for(int i = 1; i <= 3; i++) {
            IndexAction indexAction = new IndexAction();
            indexAction.setIndex(index);
            indexAction.setOpType(IndexAction.OpType.CREATE);
            indexAction.setDoc(new IndexDocument("junit-doc"+i, createDocSource()));
            bulkRequest.getActions().add(indexAction);
        }
        dataClient.bulk(baseUrl, bulkRequest);
        waitForElastic();

        // when
        Long count = dataClient.count(baseUrl, index);

        // then
        assertEquals(3, count);
    }

    private void waitForElastic() {
        try {
            // elastic need a short delay
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
    }

    @Test
    void count_noIndex() {
        // given
        tearDown();

        // when - then
        assertThrows(
            ResourceNotFoundException.class,
            () -> dataClient.count(baseUrl, index)
        );
    }
}