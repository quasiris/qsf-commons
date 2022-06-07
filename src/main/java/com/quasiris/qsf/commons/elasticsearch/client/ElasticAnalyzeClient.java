package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Elastic client that access the Analyze API
 * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-analyze.html
 */
public class ElasticAnalyzeClient extends ElasticBaseClient {
    public HttpResponse<Map> analyze(String baseUrl, String text, Object charFilters, Object tokenizer, Object filters) {
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
