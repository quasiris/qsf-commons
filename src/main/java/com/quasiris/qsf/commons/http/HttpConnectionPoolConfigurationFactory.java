package com.quasiris.qsf.commons.http;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;

public class HttpConnectionPoolConfigurationFactory {

    public static KeepAliveStrategy createDefaultKeepAliveStrategy() {
        return new KeepAliveStrategy(PooledHttpClientParams.DEFAULT_KEEP_ALIVE_IF_HEADERS_NOT_PRESENT_MILLS);
    }

    public static RequestConfig createDefaultRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(PooledHttpClientParams.DEFAULT_CONNECT_TIMEOUT))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(PooledHttpClientParams.DEFAULT_CONNECTION_REQUEST_TIMEOUT))
                .setResponseTimeout(Timeout.ofMilliseconds(PooledHttpClientParams.DEFAULT_SOCKET_TIMEOUT))
                .build();
    }

    public static PoolingHttpClientConnectionManager createDefaultPoolManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(PooledHttpClientParams.DEFAULT_MAX_CONNECTIVITY_COUNT);
        connectionManager.setDefaultMaxPerRoute(PooledHttpClientParams.DEFAULT_MAX_PER_ROUTE);
        if (PooledHttpClientParams.DEFAULT_VALIDATE_AFTER_INACTIVITY) {
            connectionManager.setValidateAfterInactivity(Timeout.ofMilliseconds(PooledHttpClientParams.DEFAULT_VALIDATE_AFTER_INACTIVITY_MILLS));
        }
        connectionManager.setDefaultSocketConfig(
                SocketConfig.custom()
                        .setSoTimeout(Timeout.ofMilliseconds(PooledHttpClientParams.DEFAULT_SOCKET_TIMEOUT))
                        .build()
        );
        return connectionManager;
    }

    public static PoolConfigStack create(PooledHttpClientParams params) {
        PoolConfigStack stack = new PoolConfigStack();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(params.getMaxConnectionCount());
        connectionManager.setDefaultMaxPerRoute(params.getMaxPerRoute());
        if (params.isValidateAfterInactivity()) {
            connectionManager.setValidateAfterInactivity(Timeout.ofMilliseconds(params.getValidateAfterInactivityMills()));
        }
        connectionManager.setDefaultSocketConfig(
                SocketConfig.custom()
                        .setSoTimeout(Timeout.ofMilliseconds(PooledHttpClientParams.DEFAULT_SOCKET_TIMEOUT))
                        .build()
        );
        stack.setConnectionManager(connectionManager);
        stack.setRequestConfig(RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(params.getConnectTimeout()))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(params.getConnectionRequestTimeout()))
                .setResponseTimeout(Timeout.ofMilliseconds(params.getSocketTimeout()))
                .build());

        if (!params.isValidateAfterInactivity()) {
            stack.setIdleConnectionMonitorThread(new IdleConnectionMonitorThread());
            stack.getIdleConnectionMonitorThread().setCloseIdleConnections(params.isCloseIdleConnections());
            stack.getIdleConnectionMonitorThread().setIdleConnectionExpirationTimeMills(params.getIdleConnectionExpirationTimeMills());
            stack.getIdleConnectionMonitorThread().setWaitTimeMills(params.getWaitTimeMills());
            stack.getIdleConnectionMonitorThread().getManagers().add(stack.getConnectionManager());
        }
        stack.setKeepAliveStrategy(new KeepAliveStrategy(params.getKeepAliveIfHeadersNotPresentMills()));
        return stack;
    }


}
