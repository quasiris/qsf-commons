package com.quasiris.qsf.commons.http.java.exception;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

public class HttpClientMalformedUrlException extends HttpClientValidationException{

    public HttpClientMalformedUrlException(HttpMetadata httpMetadata) {
        super(httpMetadata);
    }

    public HttpClientMalformedUrlException(String message, HttpMetadata httpMetadata) {
        super(message, httpMetadata);
    }

    public HttpClientMalformedUrlException(Throwable cause, HttpMetadata httpMetadata) {
        super(cause, httpMetadata);
    }

    public HttpClientMalformedUrlException(String message, Throwable cause, HttpMetadata httpMetadata) {
        super(message, cause, httpMetadata);
    }
}
