package com.quasiris.qsf.commons.http.java.exception;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

public class HttpClientUnexpectedException extends HttpClientException{
    public HttpClientUnexpectedException(HttpMetadata httpMetadata) {
        super(httpMetadata);
    }

    public HttpClientUnexpectedException(String message, HttpMetadata httpMetadata) {
        super(message, httpMetadata);
    }

    public HttpClientUnexpectedException(Throwable cause, HttpMetadata httpMetadata) {
        super(cause, httpMetadata);
    }

    public HttpClientUnexpectedException(String message, Throwable cause, HttpMetadata httpMetadata) {
        super(message, cause, httpMetadata);
    }
}
