package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.HttpResponse;
import jakarta.annotation.Nullable;

import java.util.Map;
import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Elastic client that access the Index APIs
 * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices.html
 */
public class ElasticIndexClient extends ElasticBaseClient {
    /**
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-create-index.html
     * @param baseUrl of elastic cluster
     * @param index name
     * @return response object
     */
    public HttpResponse<Map> create(String baseUrl, String index) {
        return create(baseUrl, index, null);
    }

    /**
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-create-index.html
     * @param baseUrl of elastic cluster
     * @param index name
     * @param settings with mappings
     * @return response object
     */
    public HttpResponse<Map> create(String baseUrl, String index, @Nullable String settings) {
        String apiUrl = baseUrl+"/"+index;
        return getRestClient().putForResponse(apiUrl, settings, castTypeReference(Map.class));
    }

    /**
     * Delete one or multiple indices
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-delete-index.html
     * @param baseUrl of elastic cluster
     * @param index name. Wildcards supported. To include all indices in the cluster, use _all or *
     * @return response object
     */
    public HttpResponse<Map> remove(String baseUrl, String index) {
        String apiUrl = baseUrl+"/"+index;
        return getRestClient().deleteForResponse(apiUrl, castTypeReference(Map.class));
    }

    /**
     * Check if an index exists
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-get-index.html
     * @param baseUrl of elastic cluster
     * @param index name
     * @return true if index exists
     */
    public boolean exists(String baseUrl, String index) {
        String apiUrl = baseUrl+"/"+index;
        HttpResponse httpResponse = getRestClient().getForResponse(apiUrl, null);
        return httpResponse.is2xx();
    }
}
