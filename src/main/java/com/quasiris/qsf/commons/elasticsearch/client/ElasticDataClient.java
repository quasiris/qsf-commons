package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.exception.ResourceAlreadyExistException;
import com.quasiris.qsf.commons.exception.ResourceNotFoundException;
import com.quasiris.qsf.commons.http.DefaultHttpClient;
import com.quasiris.qsf.commons.http.HttpResponse;
import com.quasiris.qsf.dto.elasticsearch.BulkRequest;
import com.quasiris.qsf.dto.elasticsearch.BulkResponse;
import com.quasiris.qsf.dto.elasticsearch.IndexAction;
import com.quasiris.qsf.dto.elasticsearch.IndexDocument;
import jakarta.annotation.Nullable;

import java.util.Map;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Access and modify elastic data
 */
public class ElasticDataClient extends ElasticBaseClient {
    private static final int DEFAULT_TIMEOUT_MILLS = 300000;
    private static final int numRetries = 3;
    private boolean requireAlias = false;

    public ElasticDataClient() {
        super(new DefaultHttpClient(DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, numRetries));
    }

    public ElasticDataClient(boolean requireAlias) {
        super(new DefaultHttpClient(DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, numRetries));
        this.requireAlias = requireAlias;
    }

    public ElasticDataClient(int numRetries, boolean requireAlias) {
        super(new DefaultHttpClient(DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, DEFAULT_TIMEOUT_MILLS, numRetries));
        this.requireAlias = requireAlias;
    }

    public IndexDocument patch(String baseUrl, String index, String id, Map changes) throws ResourceNotFoundException {
        IndexDocument origin = findOne(baseUrl, index, id);
        if(origin == null) {
            throw new ResourceNotFoundException();
        }

        if(changes != null) {
            origin.getFields().putAll(changes);
        }

        IndexDocument result = update(baseUrl, index, id, origin.getFields());
        return result;
    }

    public IndexDocument create(String baseUrl, String index, String id, Map source) throws ResourceAlreadyExistException {
        HttpResponse<Object> httpResponse = update(baseUrl, index, id, source, IndexAction.OpType.CREATE);
        IndexDocument doc = null;
        if (httpResponse.is2xx()) {
            doc = new IndexDocument(id, source);
        } else if(httpResponse.getStatusCode() == 409) {
            throw new ResourceAlreadyExistException();
        }
        return doc;
    }

    public IndexDocument update(String baseUrl, String index, String id, Map source) throws ResourceNotFoundException {
        HttpResponse<Object> httpResponse = update(baseUrl, index, id, source, null);
        IndexDocument doc = null;
        if (httpResponse.is2xx()) {
            doc = new IndexDocument(id, source);
        } else if(httpResponse.getStatusCode() == 404) {
            throw new ResourceNotFoundException();
        }
        return doc;
    }

    protected HttpResponse<Object> update(String baseUrl, String index, String id, Map source, @Nullable IndexAction.OpType opType) {
        if(opType == null) {
            opType = IndexAction.OpType.INDEX;
        }
        String apiUrl = baseUrl+"/"+index+"/_doc/"+id+"?op_type="+opType.getLowercase()+"&require_alias="+requireAlias;

        IndexDocument doc = null;
        return getRestClient().putForResponse(apiUrl, source, null);
    }

    public IndexDocument findOne(String baseUrl, String index, String id) throws ResourceNotFoundException {
        String apiUrl = baseUrl+"/"+index+"/_doc/"+id;

        IndexDocument result = null;
        HttpResponse<Map> httpResponse = getRestClient().getForResponse(apiUrl, castTypeReference(Map.class));
        if(httpResponse.is2xx()) {
            Map source = (Map) httpResponse.getPayload().get("_source");
            result = new IndexDocument(id, source);
        } else if(httpResponse.getStatusCode() == 404) {
            throw new ResourceNotFoundException();
        }

        return result;
    }

    public boolean exists(String baseUrl, String index, String id) {
        String apiUrl = baseUrl+"/"+index+"/_doc/"+id;
        HttpResponse<Map> httpResponse = getRestClient().getForResponse(apiUrl, null);
        boolean result = httpResponse.is2xx();
        return result;
    }

    public boolean delete(String baseUrl, String index, String id) throws ResourceNotFoundException {
        String apiUrl = baseUrl+"/"+index+"/_doc/"+id;

        boolean result = false;
        HttpResponse httpResponse = getRestClient().deleteForResponse(apiUrl, null);
        if(httpResponse.is2xx()) {
            result = true;
        } else {
            throw new ResourceNotFoundException();
        }
        return result;
    }

    public Long count(String baseUrl, String index) throws ResourceNotFoundException {
        String apiUrl = baseUrl+"/"+index+"/_count";
        HttpResponse<Map> httpResponse = getRestClient().getForResponse(apiUrl, null);
        Long result = 0l;
        if(httpResponse.is2xx()) {
            if(httpResponse.getPayload().containsKey("count")) {
                result = Long.valueOf(httpResponse.getPayload().get("count").toString());
            }
        } else {
            throw new ResourceNotFoundException("No such index!");
        }
        return result;
    }

    /**
     * Send all provided documents as bulk.
     * For large bulks call this method multiple times.
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/docs-bulk.html
     * @param baseUrl of elastic cluster
     * @param bulkRequest with actions
     * @return response object
     */
    public HttpResponse<BulkResponse> bulk(String baseUrl, BulkRequest bulkRequest) {
        String apiUrl = baseUrl+"/_bulk?require_alias="+requireAlias;

        StringBuilder ndJsonBuilder = new StringBuilder();
        for (IndexAction action : bulkRequest.getActions()) {
            String docBulkJson = ElasticDocSerializer.serializeBulk(action);
            ndJsonBuilder.append(docBulkJson);
            ndJsonBuilder.append("\n");
        }
        String bulkJson = ndJsonBuilder.toString();
        HttpResponse<BulkResponse> bulkResponse = getRestClient().postForResponse(apiUrl, bulkJson, castTypeReference(BulkResponse.class));
        return bulkResponse;
    }

    /**
     * If true a index url must point to an index alias
     * @return true if required
     */
    public boolean isRequireAlias() {
        return requireAlias;
    }

    public void setRequireAlias(boolean requireAlias) {
        this.requireAlias = requireAlias;
    }
}
