package com.quasiris.qsf.commons.http;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

public class PoolConfigStack {

    private PoolingHttpClientConnectionManager connectionManager;
    private RequestConfig requestConfig;
    private IdleConnectionMonitorThread idleConnectionMonitorThread;
    private KeepAliveStrategy keepAliveStrategy;

    public PoolingHttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public IdleConnectionMonitorThread getIdleConnectionMonitorThread() {
        return idleConnectionMonitorThread;
    }

    public void setIdleConnectionMonitorThread(IdleConnectionMonitorThread idleConnectionMonitorThread) {
        this.idleConnectionMonitorThread = idleConnectionMonitorThread;
    }

    public KeepAliveStrategy getKeepAliveStrategy() {
        return keepAliveStrategy;
    }

    public void setKeepAliveStrategy(KeepAliveStrategy keepAliveStrategy) {
        this.keepAliveStrategy = keepAliveStrategy;
    }
}
