package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.DefaultHttpClient;
import com.quasiris.qsf.commons.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Access and modify elastic index meta
 * ref: - https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-get-mapping.html
 * - https://www.elastic.co/guide/en/elasticsearch/reference/7.10/mapping-meta-field.html
 */
public class ElasticIndexMetaClient extends ElasticBaseClient {
    private static final int DEFAULT_TIMEOUT_MILLS = 3000;
    private static final int numRetries = 3;

    public ElasticIndexMetaClient() {
        super(new DefaultHttpClient(DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, numRetries));
    }

    /**
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-get-mapping.html
     * @param elasticBaseUrl of elastic cluster
     * @param index name
     * @return response object
     */
    public HttpResponse<Map> get(String elasticBaseUrl, String index) {
        String apiUrl = elasticBaseUrl+"/"+index+"/_mapping";
        HttpResponse<Map> httpResponse = getRestClient().getForResponse(apiUrl, castTypeReference(Map.class));
        Map meta = null;
        if(httpResponse.is2xx()) {
            Map mapping = httpResponse.getPayload();
            meta = parseMetaFromMapping(mapping);
            httpResponse.setPayload(meta);
        }
        return httpResponse;
    }

    protected static Map parseMetaFromMapping(Map elasticMapping) {
        Map result = null;
        if(elasticMapping != null && elasticMapping.values().size() == 1) {
            Object[] wrapper = elasticMapping.values().toArray();
            Map mapping = (Map) ((Map) wrapper[0]).get("mappings");
            if(mapping.containsKey("_meta")) {
                result = (Map) mapping.get("_meta");
            }
        }
        return result;
    }

    /**
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/mapping-meta-field.html
     * @param elasticBaseUrl of elastic cluster
     * @param index name
     * @param meta data
     * @return response object
     */
    public HttpResponse<Map> put(String elasticBaseUrl, String index, Object meta) {
        String apiUrl = elasticBaseUrl+"/"+index+"/_mapping";
        Map<String, Object> metaWrapper = new HashMap<>();
        metaWrapper.put("_meta", meta);
        return getRestClient().putForResponse(apiUrl, metaWrapper, castTypeReference(Map.class));
    }
}
