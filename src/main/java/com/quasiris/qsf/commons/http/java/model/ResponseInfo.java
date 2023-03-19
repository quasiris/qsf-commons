package com.quasiris.qsf.commons.http.java.model;

import java.util.List;
import java.util.Map;

public class ResponseInfo {
    private Integer statusCode;
    private Object body;
    private Map<String, List<String>> headers;

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

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
}
