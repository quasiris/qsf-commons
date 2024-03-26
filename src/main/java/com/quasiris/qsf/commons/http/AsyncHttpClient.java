package com.quasiris.qsf.commons.http;

import com.quasiris.qsf.commons.util.JsonUtil;
import jakarta.annotation.Nullable;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

// TODO use java 11 HttpClient
public class AsyncHttpClient {
    private static Logger LOG = LoggerFactory.getLogger(AsyncHttpClient.class);

    private Long timeout;

    public AsyncHttpClient() {
    }

    public AsyncHttpClient(Long timeout) {
        this.timeout = timeout;
    }

    public void postAsyncWithoutResponse(String url, @Nullable Object data, Header... headers) {
        HttpAsyncClientBuilder asyncClientBuilder = HttpAsyncClients.custom()
                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1);
        if(timeout != null) {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(timeout))
                    .setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout))
                    .setResponseTimeout(Timeout.ofMilliseconds(timeout)).build();
            IOReactorConfig reactorConfig = IOReactorConfig.custom()
                    .setSelectInterval(Timeout.ofMilliseconds(timeout))
                    .setSoTimeout(Timeout.ofMilliseconds(timeout))
                    .build();
            asyncClientBuilder
                    .setIOReactorConfig(reactorConfig)
                    .setDefaultRequestConfig(config);
        }
        CloseableHttpAsyncClient client = asyncClientBuilder.build();
        client.start();
        SimpleRequestBuilder simpleRequestBuilder = SimpleRequestBuilder.post(url)
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        appendHeadersAndPayload(simpleRequestBuilder, data, headers);
        SimpleHttpRequest request = simpleRequestBuilder.build();

        Future<SimpleHttpResponse> future = client.execute(request, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse simpleHttpResponse) {
                LOG.debug("The async request finished successful with code: " + simpleHttpResponse.getCode());

                if(simpleHttpResponse.getCode() >= 300) {
                    LOG.error("The async request failed with code: " + simpleHttpResponse.getCode() + " and body: " + simpleHttpResponse.getBody().getBodyText());
                }
                client.close(CloseMode.GRACEFUL);
            }

            @Override
            public void failed(Exception e) {
                LOG.error("The async request to URL "+url+" failed because " + e.getMessage(), e);
                client.close(CloseMode.GRACEFUL);
            }

            @Override
            public void cancelled() {
                LOG.error("The async request to URL {} was canceled.", url);
                client.close(CloseMode.GRACEFUL);
            }
        });
    }

    public static void appendHeadersAndPayload(SimpleRequestBuilder simpleRequestBuilder, @Nullable Object data, Header... headers) {
        for (Header header : headers) {
            simpleRequestBuilder.setHeader(header);
        }
        if (data != null) {
            String postString = data instanceof String ? data.toString() : JsonUtil.toJson(data);
            simpleRequestBuilder.setBody(postString, ContentType.APPLICATION_JSON);
        }
    }
}
