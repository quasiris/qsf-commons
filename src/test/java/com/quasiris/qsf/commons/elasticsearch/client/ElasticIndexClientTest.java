package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class ElasticIndexClientTest {
    ElasticIndexClient indexClient = new ElasticIndexClient();
    String baseUrl = "http://localhost:9200";
    String index = "junit-index";

    @AfterEach
    void tearDown() {
        indexClient.remove(baseUrl, index);
    }

    @Test
    void create_existingIndex() {
        // given
        indexClient.create(baseUrl, index);

        // when
        HttpResponse<Map> response = indexClient.create(baseUrl, index);

        // then
        assertTrue(response.getPayload().toString().contains("resource_already_exists_exception"));
    }

    @Test
    void createAndRemove() {
        // given
        String index = "junit-index";

        // when
        indexClient.create(baseUrl, index, null);

        // then
        assertTrue(indexClient.exists(baseUrl, index));

        // when
        indexClient.remove(baseUrl, index);

        // then
        assertFalse(indexClient.exists(baseUrl, index));
    }

    @Test
    void remove_nonExisting() {
        // given
        String index = "junit-index";

        // when
        HttpResponse<Map> response = indexClient.remove(baseUrl, index);

        // then
        assertEquals(404, response.getStatusCode());
        assertTrue(response.getPayload().toString().contains("index_not_found_exception"));
    }

    @Test
    void exists_notExist() {
        // given
        String index = "junit-index";

        // when
        boolean exists = indexClient.exists(baseUrl, index);

        // then
        assertFalse(exists);
    }

    @Test
    void exists_noCluster() {
        // given
        String elasticBaseUrlNonExisting = this.baseUrl +"nonexisting";
        String index = "junit-index";

        // when
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> indexClient.exists(elasticBaseUrlNonExisting, index)
        );
    }
}