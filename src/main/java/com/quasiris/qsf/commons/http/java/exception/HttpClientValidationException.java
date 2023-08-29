package com.quasiris.qsf.commons.http.java.exception;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

public class HttpClientValidationException extends HttpClientException{

    public HttpClientValidationException(HttpMetadata httpMetadata) {
        super(httpMetadata);
    }

    public HttpClientValidationException(String message, HttpMetadata httpMetadata) {
        super(message, httpMetadata);
    }

    public HttpClientValidationException(Throwable cause, HttpMetadata httpMetadata) {
        super(cause, httpMetadata);
    }

    public HttpClientValidationException(String message, Throwable cause, HttpMetadata httpMetadata) {
        super(message, cause, httpMetadata);
    }
}
