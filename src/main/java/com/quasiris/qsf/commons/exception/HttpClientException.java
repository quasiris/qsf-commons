package com.quasiris.qsf.commons.exception;

public class HttpClientException extends RuntimeException {
    private final int statusCode;
    private final String data;

    public HttpClientException(int statusCode, String data) {
        super(data);
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getData() {
        return data;
    }
}
