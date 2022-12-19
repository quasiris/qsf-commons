package com.quasiris.qsf.commons.http.java.model;

public class HttpMetadata {
    private RequestInfo request = new RequestInfo();
    private ResponseInfo response = new ResponseInfo();
    private int retries = 0;

    public HttpMetadata() {
    }

    public HttpMetadata(RequestInfo request) {
        this.request = request;
    }

    public HttpMetadata(RequestInfo request, ResponseInfo response) {
        this.request = request;
        this.response = response;
    }

    public RequestInfo getRequest() {
        return request;
    }

    public void setRequest(RequestInfo request) {
        this.request = request;
    }

    public ResponseInfo getResponse() {
        return response;
    }

    public void setResponse(ResponseInfo response) {
        this.response = response;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
