package com.quasiris.qsf.commons.http;

import java.io.Serializable;

public class HttpResponse<T> implements Serializable {
    private Integer statusCode;
    private T payload;

    public HttpResponse() {
    }

    public HttpResponse(Integer statusCode, T payload) {
        this.statusCode = statusCode;
        this.payload = payload;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean is2xx() {
        return statusCode != null && statusCode >= 200 && statusCode < 300;
    }
}
