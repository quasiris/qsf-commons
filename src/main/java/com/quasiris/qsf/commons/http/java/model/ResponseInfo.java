package com.quasiris.qsf.commons.http.java.model;

public class ResponseInfo {
    private Integer statusCode;
    private Object body;

    public ResponseInfo() {
    }

    public ResponseInfo(Integer statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
