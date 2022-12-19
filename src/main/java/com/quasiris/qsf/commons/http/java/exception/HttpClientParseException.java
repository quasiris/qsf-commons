package com.quasiris.qsf.commons.http.java.exception;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

public class HttpClientParseException extends HttpClientException {

    public HttpClientParseException(HttpMetadata httpMetadata) {
        super(httpMetadata);
    }

    public HttpClientParseException(String message, HttpMetadata httpMetadata) {
        super(message, httpMetadata);
    }

    public HttpClientParseException(Throwable cause, HttpMetadata httpMetadata) {
        super(cause, httpMetadata);
    }

    public HttpClientParseException(String message, Throwable cause, HttpMetadata httpMetadata) {
        super(message, cause, httpMetadata);
    }
}
