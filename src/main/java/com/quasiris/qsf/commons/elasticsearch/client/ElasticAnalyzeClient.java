package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.DefaultHttpClient;
import com.quasiris.qsf.commons.http.HttpResponse;
import com.quasiris.qsf.pipeline.filter.elastic.bean.Analyze;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Elastic client that access the Analyze API
 * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-analyze.html
 */
public class ElasticAnalyzeClient {
    public Analyze analyze(String baseUrl, String payload) {
        String apiUrl = baseUrl+"/_analyze";
        Analyze analyze = null;
        try (DefaultHttpClient restClient = new DefaultHttpClient()) {
            HttpResponse<Analyze> httpResponse = restClient.postForResponse(apiUrl, payload, castTypeReference(Analyze.class));
            if (httpResponse.is2xx()) {
                analyze = httpResponse.getPayload();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return analyze;
    }

    public HttpResponse<Map> analyze(String baseUrl, String text, @Nullable Object charFilters, @Nullable Object tokenizer, @Nullable Object filters) {
        return analyze(baseUrl, text, charFilters, tokenizer, filters, false);
    }

    public HttpResponse<Map> analyze(String baseUrl, String text, @Nullable Object charFilters, @Nullable Object tokenizer, @Nullable Object filters, boolean explain) {
        String apiUrl = baseUrl + "/_analyze";

        Map<String, Object> params = new HashMap<>();
        if (charFilters != null) {
            params.put("char_filter", charFilters);
        }
        if (tokenizer != null) {
            params.put("tokenizer", tokenizer);
        }
        if (filters != null) {
            params.put("filter", filters);
        }
        params.put("text", text);
        if (explain) {
            params.put("explain", explain);
        }

        HttpResponse<Map> httpResponse;
        try (DefaultHttpClient restClient = new DefaultHttpClient()) {
            httpResponse = restClient.postForResponse(apiUrl, params, castTypeReference(Map.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return httpResponse;
    }
}
