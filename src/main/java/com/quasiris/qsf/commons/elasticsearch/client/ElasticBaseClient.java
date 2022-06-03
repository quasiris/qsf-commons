package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.DefaultHttpClient;
import com.quasiris.qsf.commons.http.HttpClient;

public class ElasticBaseClient {
    private HttpClient restClient;

    public ElasticBaseClient() {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        defaultHttpClient.setParseResponseOnError(true);
        this.restClient = defaultHttpClient;
    }

    public ElasticBaseClient(HttpClient restClient) {
        this.restClient = restClient;
    }

    protected HttpClient getRestClient() {
        return restClient;
    }
}
