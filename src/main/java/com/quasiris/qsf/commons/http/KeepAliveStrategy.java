package com.quasiris.qsf.commons.http;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public class KeepAliveStrategy implements ConnectionKeepAliveStrategy {
    private final static String CONN_KEEP_ALIVE = "Keep-Alive";
    private int keepAliveIfHeadersNotPresentMills = 5 * 1000;

    public KeepAliveStrategy() {
    }

    public KeepAliveStrategy(int keepAliveIfHeadersNotPresentMills) {
        this.keepAliveIfHeadersNotPresentMills = keepAliveIfHeadersNotPresentMills;
    }

    @Override
    public TimeValue getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
        BasicHeaderElementIterator it = new BasicHeaderElementIterator(httpResponse.headerIterator(CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.next();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase
                    ("timeout")) {
                return Timeout.ofMilliseconds(Long.parseLong(value) * 1000);
            }
        }
        return Timeout.ofMilliseconds(keepAliveIfHeadersNotPresentMills);
    }

    public int getKeepAliveIfHeadersNotPresentMills() {
        return keepAliveIfHeadersNotPresentMills;
    }
}
