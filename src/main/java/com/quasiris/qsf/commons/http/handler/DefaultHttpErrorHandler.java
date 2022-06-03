package com.quasiris.qsf.commons.http.handler;

import com.quasiris.qsf.commons.exception.HttpClientException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DefaultHttpErrorHandler implements HttpErrorHandler {
    @Override
    public boolean hasError(CloseableHttpResponse response) {
        return response.getCode() >= 400;
    }

    @Override
    public void handle(CloseableHttpResponse response) {
        String content = null;
        try {
            content = EntityUtils.toString(response.getEntity(), UTF_8);
        } catch (Exception ignore) {
        }
        throw new HttpClientException(response.getCode(), content);
    }
}
