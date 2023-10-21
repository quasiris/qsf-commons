package com.quasiris.qsf.commons.http.java.model;

public class RequestInfo {
    private String uri;
    private String method;
    private Object body;
    private String boundary;

    public RequestInfo() {
    }

    public RequestInfo(String uri, String method, Object body) {
        this.uri = uri;
        this.method = method;
        this.body = body;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }
}
