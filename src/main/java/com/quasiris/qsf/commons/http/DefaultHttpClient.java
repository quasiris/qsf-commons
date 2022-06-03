package com.quasiris.qsf.commons.http;

import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultHttpClient extends BaseHttpClient {
    private static Logger LOG = LoggerFactory.getLogger(DefaultHttpClient.class);

    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT_MILLS = 4000;
    private static final int DEFAULT_CONNECTION_TIMEOUT_MILLS = 4000;
    private static final int DEFAULT_SOCKET_TIMEOUT_MILLS = 4000;

    public DefaultHttpClient(int connectionTimeOut, int connectionRequestTimeOut, int socketTimeOut, int numRetries) {
        super(HttpClientBuilder.create()
            .setRetryStrategy(new HttpRequestRetryStrategy() {
                @Override
                public boolean retryRequest(HttpRequest httpRequest, IOException e, int i, HttpContext httpContext) {
                    if(i > 1) {
                        LOG.info("RETRY HttpRequest " + i);
                    }
                    return i < numRetries;
                }

                @Override
                public boolean retryRequest(HttpResponse httpResponse, int i, HttpContext httpContext) {
                    if(i > 1) {
                        LOG.info("RETRY HttpResponse " + i);
                    }
                    return i < numRetries && httpResponse.getCode() >= 500;
                }

                @Override
                public TimeValue getRetryInterval(HttpResponse httpResponse, int i, HttpContext httpContext) {
                    return Timeout.ofMilliseconds(2000);
                }
            })
            .setDefaultRequestConfig(
                    RequestConfig.custom()
                            .setConnectTimeout(Timeout.ofMilliseconds(connectionTimeOut))
                            .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionRequestTimeOut))
                            .setResponseTimeout(Timeout.ofMilliseconds(socketTimeOut))
                            .build()
            ).build()
        );
    }

    public DefaultHttpClient(int connectionTimeOut, int connectionRequestTimeOut, int socketTimeOut) {
        super(HttpClientBuilder.create()
            .setDefaultRequestConfig(
                    RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(connectionTimeOut))
                    .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionRequestTimeOut))
                    .setResponseTimeout(Timeout.ofMilliseconds(socketTimeOut))
                    .build()
            ).build()
        );
    }

    public DefaultHttpClient() {
        super(HttpClientBuilder.create()
                .setDefaultRequestConfig(
                    RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(DEFAULT_CONNECTION_TIMEOUT_MILLS))
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(DEFAULT_CONNECTION_REQUEST_TIMEOUT_MILLS))
                        .setResponseTimeout(Timeout.ofMilliseconds(DEFAULT_SOCKET_TIMEOUT_MILLS))
                        .build()
                ).build()
        );
    }
}

