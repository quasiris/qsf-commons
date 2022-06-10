package com.quasiris.qsf.commons.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.util.JsonUtil;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class AsyncHttpClient implements AutoCloseable {
    private static Logger LOG = LoggerFactory.getLogger(AsyncHttpClient.class);

    private final CloseableHttpAsyncClient httpClient;

    public AsyncHttpClient() {
        this.httpClient = HttpAsyncClients.custom()
            .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1)
            .build();
        this.httpClient.start();
    }

    public AsyncHttpClient(CloseableHttpAsyncClient httpClient) {
        this.httpClient = httpClient;
        this.httpClient.start();
    }

    public AsyncHttpClient(Long timeout) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(timeout))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout))
                .setResponseTimeout(Timeout.ofMilliseconds(timeout)).build();
        IOReactorConfig reactorConfig = IOReactorConfig.custom()
                .setSelectInterval(Timeout.ofMilliseconds(timeout))
                .setSoTimeout(Timeout.ofMilliseconds(timeout))
                .build();
        this.httpClient = HttpAsyncClients.custom()
                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1)
                .setIOReactorConfig(reactorConfig)
                .setDefaultRequestConfig(config)
                .build();
        this.httpClient.start();
    }

    public Future<SimpleHttpResponse> postAsync(String url, @Nullable Object data, Header... headers) {
        SimpleRequestBuilder simpleRequestBuilder = SimpleRequestBuilder.post(url)
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        appendHeadersAndPayload(simpleRequestBuilder, data, headers);
        SimpleHttpRequest httpRequest = simpleRequestBuilder.build();
        return performAsyncRequest(httpRequest);
    }

    private void appendHeadersAndPayload(SimpleRequestBuilder simpleRequestBuilder, @Nullable Object data, Header... headers) {
        for (Header header : headers) {
            simpleRequestBuilder.setHeader(header);
        }
        if (data != null) {
            String postString = data instanceof String ? data.toString() : JsonUtil.toJson(data);
            simpleRequestBuilder.setBody(postString, ContentType.APPLICATION_JSON);
        }
    }

    public Future<SimpleHttpResponse> performAsyncRequest(SimpleHttpRequest httpRequest) {
        return httpClient.execute(httpRequest, null);
    }

    public static <T> HttpResponse<T> waitForFutureResponse(Future<SimpleHttpResponse> future, @Nullable TypeReference<T> typeReference, @Nullable Long timeout, @Nullable Boolean parseResponseOnError) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        SimpleHttpResponse httpResponse = null;
        if(timeout != null) {
            httpResponse = future.get(timeout, TimeUnit.MILLISECONDS);
        } else {
            httpResponse = future.get();
        }

        Integer statusCode = null;
        T result = null;
        if(httpResponse != null) {
            statusCode = httpResponse.getCode();
            boolean parseResponse = Boolean.TRUE.equals(parseResponseOnError) || statusCode >= 200 && statusCode < 400;
            if(httpResponse.getBody() != null && httpResponse.getBody().getBodyBytes() != null && parseResponse) {
                if(typeReference == null) {
                    result = (T) JsonUtil.defaultMapper().readValue(httpResponse.getBody().getBodyBytes(), Object.class);
                } else {
                    result = JsonUtil.defaultMapper().readValue(httpResponse.getBody().getBodyBytes(), typeReference);
                }
            }
        }
        return new HttpResponse<T>(statusCode, result);
    }

    @Override
    public void close() throws Exception {
        if(httpClient != null) {
            httpClient.close();
        }
    }
}
