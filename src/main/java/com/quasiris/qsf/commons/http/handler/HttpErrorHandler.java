package com.quasiris.qsf.commons.http.handler;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

public interface HttpErrorHandler {
    boolean hasError(CloseableHttpResponse response);

    void handle(CloseableHttpResponse response);
}
