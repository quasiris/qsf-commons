package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.exception.ResourceNotFoundException;
import com.quasiris.qsf.commons.http.BaseHttpClient;
import com.quasiris.qsf.commons.http.DefaultHttpClient;
import com.quasiris.qsf.commons.http.HttpResponse;
import com.quasiris.qsf.commons.http.java.JavaHttpClient;
import com.quasiris.qsf.pipeline.filter.elastic.bean.ElasticResult;
import com.quasiris.qsf.pipeline.filter.elastic.bean.MultiElasticResult;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Elastic client that access Search API
 * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-search.html
 */
public class ElasticSearchClient {
    JavaHttpClient httpClient;

    boolean useApacheClient = true;
    private long timeout = 4000L;

    public ElasticSearchClient() {
        httpClient = new JavaHttpClient(HttpClient.newBuilder()
                .build());
    }

    public ElasticResult search(String baseUrl, String index, String jsonQuery) {
        String indexUrl = baseUrl + "/" + index;
        return search(indexUrl, jsonQuery);
    }

    public ElasticResult search(String indexUrl, String jsonQuery) {
        return search(indexUrl, jsonQuery, Duration.ofMillis(timeout));
    }

    public ElasticResult search(String indexUrl, String jsonQuery, Duration requestTimeout) {
        String apiUrl = indexUrl + "/_search";
        ElasticResult result = null;

        if (useApacheClient) {
            int reqTimeoutMills = (int) requestTimeout.toMillis();
            try (DefaultHttpClient restClient = new DefaultHttpClient(reqTimeoutMills, reqTimeoutMills, reqTimeoutMills)) {
                HttpResponse<ElasticResult> httpResponse = restClient.postForResponse(apiUrl, jsonQuery, castTypeReference(ElasticResult.class));
                if (httpResponse.is2xx()) {
                    result = httpResponse.getPayload();
                } else if (httpResponse.getStatusCode() == 404) {
                    throw new ResourceNotFoundException();
                } else if (httpResponse.getStatusCode() >= 400) {
                    throw new RuntimeException("Error for http request! Url: " + apiUrl + " return code: " + httpResponse.getStatusCode());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            java.net.http.HttpResponse<ElasticResult> httpResponse = httpClient.request(
                    JavaHttpClient.RequestMethod.POST,
                    apiUrl,
                    jsonQuery,
                    castTypeReference(ElasticResult.class),
                    requestTimeout);
            result = httpResponse.body();
        }
        return result;
    }


    public MultiElasticResult multiSearch(String baseUrl, String index, List<String> jsonQueries) {
        String indexUrl = baseUrl + "/" + index;
        return multiSearch(indexUrl, jsonQueries);
    }

    public MultiElasticResult multiSearch(String indexUrl, List<String> jsonQueries) {
        String apiUrl = indexUrl + "/_msearch";

        // create jsonNd
        StringBuilder multiRequest = new StringBuilder();
        for (String request : jsonQueries) {
            multiRequest.append("{}").append("\n");
            multiRequest.append(request).append("\n");
        }
        String jsonNd = multiRequest.toString();

        // build request
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_NDJSON.toString());
        BaseHttpClient.appendHeadersAndPayload(httpPost, jsonNd);

        MultiElasticResult result = null;
        if (useApacheClient) {
            int intTimeout = (int) timeout;
            try (DefaultHttpClient restClient = new DefaultHttpClient(intTimeout, intTimeout, intTimeout)) {
                // perform request
                HttpResponse<MultiElasticResult> httpResponse = restClient.performRequest(httpPost, castTypeReference(MultiElasticResult.class), false);
                if (httpResponse.is2xx()) {
                    result = httpResponse.getPayload();
                } else if (httpResponse.getStatusCode() == 404) {
                    throw new ResourceNotFoundException();
                } else if (httpResponse.getStatusCode() >= 400) {
                    throw new RuntimeException("Error for http request! Url: " + apiUrl + " return code: " + httpResponse.getStatusCode());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            java.net.http.HttpResponse<MultiElasticResult> httpResponse = httpClient.request(
                    JavaHttpClient.RequestMethod.POST,
                    apiUrl,
                    jsonNd,
                    castTypeReference(MultiElasticResult.class),
                    Duration.ofMillis(timeout),
                    "Content-Type: " + ContentType.APPLICATION_NDJSON);
            result = httpResponse.body();
        }
        return result;
    }

    public boolean isUseApacheClient() {
        return useApacheClient;
    }

    public void setUseApacheClient(boolean useApacheClient) {
        this.useApacheClient = useApacheClient;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
