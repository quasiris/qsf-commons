package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.HttpResponse;
import com.quasiris.qsf.pipeline.filter.elastic.bean.Analyze;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Elastic client that access the Analyze API
 * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-analyze.html
 */
public class ElasticAnalyzeClient extends ElasticBaseClient {
    public Analyze analyze(String baseUrl, String payload) {
        String apiUrl = baseUrl+"/_analyze";
        HttpResponse<Analyze> httpResponse = getRestClient().postForResponse(apiUrl, payload, castTypeReference(Analyze.class));
        Analyze analyze = null;
        if(httpResponse.is2xx()) {
            analyze = httpResponse.getPayload();
        }
        return analyze;
    }

    public HttpResponse<Map> analyze(String baseUrl, String text, @Nullable Object charFilters, @Nullable Object tokenizer, @Nullable Object filters) {
        String apiUrl = baseUrl+"/_analyze";

        Map<String, Object> params = new HashMap();
        if(charFilters != null) {
            params.put("char_filter", charFilters);
        }
        if(tokenizer != null) {
            params.put("tokenizer", tokenizer);
        }
        if(filters != null) {
            params.put("filter", filters);
        }
        params.put("text", text);
        return getRestClient().postForResponse(apiUrl, params, castTypeReference(Map.class));
    }
}
