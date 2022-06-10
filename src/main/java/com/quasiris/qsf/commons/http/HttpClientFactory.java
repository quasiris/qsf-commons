package com.quasiris.qsf.commons.http;

public class HttpClientFactory {
    private final static HttpClient httpClient = new DefaultHttpClient();
    private final static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(1000L);

    public static HttpClient defaultClient() {
        return httpClient;
    }

    public static AsyncHttpClient defaultAsyncClient() {
        return asyncHttpClient;
    }
}
