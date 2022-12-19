package com.quasiris.qsf.commons.http.java.exception;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

public class HttpClientStatusException extends HttpClientException {

    public HttpClientStatusException(HttpMetadata httpMetadata) {
        super(httpMetadata);
    }

    public HttpClientStatusException(String message, HttpMetadata httpMetadata) {
        super(message, httpMetadata);
    }

    public HttpClientStatusException(Throwable cause, HttpMetadata httpMetadata) {
        super(cause, httpMetadata);
    }

    public HttpClientStatusException(String message, Throwable cause, HttpMetadata httpMetadata) {
        super(message, cause, httpMetadata);
    }
}
