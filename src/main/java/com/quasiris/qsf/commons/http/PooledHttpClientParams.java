package com.quasiris.qsf.commons.http;

public class PooledHttpClientParams {
    public static final int DEFAULT_MAX_CONNECTIVITY_COUNT = 500;
    public static final int DEFAULT_VALIDATE_AFTER_INACTIVITY_MILLS = 30 * 1000;
    public static final boolean DEFAULT_VALIDATE_AFTER_INACTIVITY = false;
    public static final int DEFAULT_MAX_PER_ROUTE = 100;
    public static final int DEFAULT_CONNECT_TIMEOUT = 4000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 4000;
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 4000;
    public static final int DEFAULT_WAIT_TIME_MILLS = 5000;
    public static final boolean DEFAULT_CLOSE_IDLE_CONNECTIONS = true;
    public static final int DEFALUT_IDLE_CONNECTIONS_EXPIRATION_TIME_MILLS = 30 * 1000;
    public static final int DEFAULT_KEEP_ALIVE_IF_HEADERS_NOT_PRESENT_MILLS = 5 * 1000;


    private Integer maxConnectionCount = DEFAULT_MAX_CONNECTIVITY_COUNT;
    private Integer validateAfterInactivityMills = DEFAULT_VALIDATE_AFTER_INACTIVITY_MILLS;
    private boolean validateAfterInactivity = DEFAULT_VALIDATE_AFTER_INACTIVITY;
    private Integer maxPerRoute = DEFAULT_MAX_PER_ROUTE;
    private Integer connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private Integer socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    private Integer connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    private int waitTimeMills = DEFAULT_WAIT_TIME_MILLS;
    private boolean closeIdleConnections = DEFAULT_CLOSE_IDLE_CONNECTIONS;
    private int idleConnectionExpirationTimeMills = DEFALUT_IDLE_CONNECTIONS_EXPIRATION_TIME_MILLS;
    private int keepAliveIfHeadersNotPresentMills = DEFAULT_KEEP_ALIVE_IF_HEADERS_NOT_PRESENT_MILLS;

    private PooledHttpClientParams() {
    }

    public static PooledHttpClientParamsBuilder builder() {
        return PooledHttpClientParamsBuilder.aPooledHttpClientParams();
    }

    public Integer getMaxConnectionCount() {
        return maxConnectionCount;
    }

    public void setMaxConnectionCount(Integer maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public Integer getValidateAfterInactivityMills() {
        return validateAfterInactivityMills;
    }

    public void setValidateAfterInactivityMills(Integer validateAfterInactivityMills) {
        this.validateAfterInactivityMills = validateAfterInactivityMills;
    }

    public boolean isValidateAfterInactivity() {
        return validateAfterInactivity;
    }

    public void setValidateAfterInactivity(boolean validateAfterInactivity) {
        this.validateAfterInactivity = validateAfterInactivity;
    }

    public Integer getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getWaitTimeMills() {
        return waitTimeMills;
    }

    public void setWaitTimeMills(int waitTimeMills) {
        this.waitTimeMills = waitTimeMills;
    }

    public boolean isCloseIdleConnections() {
        return closeIdleConnections;
    }

    public void setCloseIdleConnections(boolean closeIdleConnections) {
        this.closeIdleConnections = closeIdleConnections;
    }

    public int getIdleConnectionExpirationTimeMills() {
        return idleConnectionExpirationTimeMills;
    }

    public void setIdleConnectionExpirationTimeMills(int idleConnectionExpirationTimeMills) {
        this.idleConnectionExpirationTimeMills = idleConnectionExpirationTimeMills;
    }

    public int getKeepAliveIfHeadersNotPresentMills() {
        return keepAliveIfHeadersNotPresentMills;
    }

    public void setKeepAliveIfHeadersNotPresentMills(int keepAliveIfHeadersNotPresentMills) {
        this.keepAliveIfHeadersNotPresentMills = keepAliveIfHeadersNotPresentMills;
    }

    public static final class PooledHttpClientParamsBuilder {
        private PooledHttpClientParams pooledHttpClientParams;

        private PooledHttpClientParamsBuilder() {
            pooledHttpClientParams = new PooledHttpClientParams();
        }

        public static PooledHttpClientParamsBuilder aPooledHttpClientParams() {
            return new PooledHttpClientParamsBuilder();
        }

        public PooledHttpClientParamsBuilder maxConnectionCount(Integer maxConnectionCount) {
            pooledHttpClientParams.setMaxConnectionCount(maxConnectionCount);
            return this;
        }

        public PooledHttpClientParamsBuilder validateAfterInactivityMills(Integer validateAfterInactivityMills) {
            pooledHttpClientParams.setValidateAfterInactivityMills(validateAfterInactivityMills);
            return this;
        }

        public PooledHttpClientParamsBuilder validateAfterInactivity(boolean validateAfterInactivity) {
            pooledHttpClientParams.setValidateAfterInactivity(validateAfterInactivity);
            return this;
        }

        public PooledHttpClientParamsBuilder maxPerRoute(Integer maxPerRoute) {
            pooledHttpClientParams.setMaxPerRoute(maxPerRoute);
            return this;
        }

        public PooledHttpClientParamsBuilder connectTimeout(Integer connectTimeout) {
            pooledHttpClientParams.setConnectTimeout(connectTimeout);
            return this;
        }

        public PooledHttpClientParamsBuilder socketTimeout(Integer socketTimeout) {
            pooledHttpClientParams.setSocketTimeout(socketTimeout);
            return this;
        }

        public PooledHttpClientParamsBuilder connectionRequestTimeout(Integer connectionRequestTimeout) {
            pooledHttpClientParams.setConnectionRequestTimeout(connectionRequestTimeout);
            return this;
        }

        public PooledHttpClientParamsBuilder waitTimeMills(int waitTimeMills) {
            pooledHttpClientParams.setWaitTimeMills(waitTimeMills);
            return this;
        }

        public PooledHttpClientParamsBuilder closeIdleConnections(boolean closeIdleConnections) {
            pooledHttpClientParams.setCloseIdleConnections(closeIdleConnections);
            return this;
        }

        public PooledHttpClientParamsBuilder idleConnectionExpirationTimeMills(int idleConnectionExpirationTimeMills) {
            pooledHttpClientParams.setIdleConnectionExpirationTimeMills(idleConnectionExpirationTimeMills);
            return this;
        }

        public PooledHttpClientParamsBuilder keepAliveIfHeadersNotPresentMills(int keepAliveIfHeadersNotPresentMills) {
            pooledHttpClientParams.setKeepAliveIfHeadersNotPresentMills(keepAliveIfHeadersNotPresentMills);
            return this;
        }

        public PooledHttpClientParams build() {
            return pooledHttpClientParams;
        }
    }
}
