package com.quasiris.qsf.commons.http;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

public class PooledHttpClient extends BaseHttpClient {
    private final PoolingHttpClientConnectionManager pool;
    private final KeepAliveStrategy keepAliveStrategy;
    private final RequestConfig requestConfig;
    private final IdleConnectionMonitorThread idleConnectionMonitorThread;

    public PooledHttpClient() {
        this(HttpConnectionPoolConfigurationFactory.create(PooledHttpClientParams.builder().build()));
    }

    public PooledHttpClient(PoolConfigStack stack) {
        this(stack.getConnectionManager(), stack.getKeepAliveStrategy(), stack.getRequestConfig(), stack.getIdleConnectionMonitorThread());
    }

    public PooledHttpClient(PoolingHttpClientConnectionManager pool, KeepAliveStrategy keepAliveStrategy, RequestConfig requestConfig, IdleConnectionMonitorThread idleConnectionMonitorThread) {
        super(initClient(pool, keepAliveStrategy, requestConfig));
        this.pool = pool;
        this.keepAliveStrategy = keepAliveStrategy;
        this.requestConfig = requestConfig;
        this.idleConnectionMonitorThread = idleConnectionMonitorThread;
    }

    public PooledHttpClient(PooledHttpClient other) {
        super(initClient(other.pool, other.keepAliveStrategy, other.requestConfig));
        this.pool = other.pool;
        this.keepAliveStrategy = other.keepAliveStrategy;
        this.requestConfig = other.requestConfig;
        this.idleConnectionMonitorThread = other.idleConnectionMonitorThread;
    }

    public PooledHttpClient(PooledHttpClientParams params) {
        this(HttpConnectionPoolConfigurationFactory.create(params));
    }

    public static PooledHttpClientBuilder builder() {
        return new PooledHttpClientBuilder();
    }

    private static CloseableHttpClient initClient(PoolingHttpClientConnectionManager pool, KeepAliveStrategy keepAliveStrategy, RequestConfig requestConfig) {
        return HttpClients.custom()
                .setConnectionManager(pool)
                .setKeepAliveStrategy(keepAliveStrategy)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public PoolingHttpClientConnectionManager getPool() {
        return pool;
    }

    public KeepAliveStrategy getKeepAliveStrategy() {
        return keepAliveStrategy;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public IdleConnectionMonitorThread getIdleConnectionMonitorThread() {
        return idleConnectionMonitorThread;
    }

    public static final class PooledHttpClientBuilder {
        private PoolingHttpClientConnectionManager pool;
        private KeepAliveStrategy keepAliveStrategy;
        private RequestConfig requestConfig;
        private IdleConnectionMonitorThread idleConnectionMonitorThread;

        private PooledHttpClientBuilder() {
        }

        public static PooledHttpClientBuilder aPooledHttpClient() {
            return new PooledHttpClientBuilder();
        }

        public PooledHttpClientBuilder pool(PoolingHttpClientConnectionManager pool) {
            this.pool = pool;
            return this;
        }

        public PooledHttpClientBuilder keepAliveStrategy(KeepAliveStrategy keepAliveStrategy) {
            this.keepAliveStrategy = keepAliveStrategy;
            return this;
        }

        public PooledHttpClientBuilder requestConfig(RequestConfig requestConfig) {
            this.requestConfig = requestConfig;
            return this;
        }

        public PooledHttpClientBuilder idleConnectionMonitorThread(IdleConnectionMonitorThread idleConnectionMonitorThread) {
            this.idleConnectionMonitorThread = idleConnectionMonitorThread;
            return this;
        }

        public PooledHttpClient build() {
            if (keepAliveStrategy == null) {
                keepAliveStrategy = HttpConnectionPoolConfigurationFactory.createDefaultKeepAliveStrategy();
            }
            if (requestConfig == null) {
                requestConfig = HttpConnectionPoolConfigurationFactory.createDefaultRequestConfig();
            }
            if (pool == null) {
                pool = HttpConnectionPoolConfigurationFactory.createDefaultPoolManager();
            }
            return new PooledHttpClient(pool, keepAliveStrategy, requestConfig, idleConnectionMonitorThread);
        }
    }
}
