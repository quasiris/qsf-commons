package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class ElasticIndexMetaClientTest {
    ElasticIndexClient indexClient = new ElasticIndexClient();
    ElasticIndexMetaClient metaClient = new ElasticIndexMetaClient();
    String baseUrl = "http://localhost:9200";
    String index = "junit-index";

    @AfterEach
    void tearDown() {
        indexClient.remove(baseUrl, index);
    }

    @Test
    void putAndGet() {
        // given
        Map<String, Object> meta = createMeta();
        indexClient.create(baseUrl, index);

        // when
        metaClient.put(baseUrl, index, meta);

        // then
        HttpResponse<Map> response = metaClient.get(baseUrl, index);
        assertEquals(200, response.getStatusCode());
        assertEquals(meta, response.getPayload());
    }

    Map<String, Object> createMeta() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("feedingHistory", Arrays.asList("2022-02-18-15.27.14", "2022-02-17-14.21.11", "2022-02-17-13.57.54"));
        meta.put("lock", true);
        meta.put("feeding_version", 1.54);
        return meta;
    }
}