package com.quasiris.qsf.commons.http.java.exception;

import com.quasiris.qsf.commons.http.java.helper.ErrorMsgHelper;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

public abstract class HttpClientException extends RuntimeException {
    private HttpMetadata httpMetadata;

    public HttpClientException(HttpMetadata httpMetadata) {
        this.httpMetadata = httpMetadata;
    }

    public HttpClientException(String message, HttpMetadata httpMetadata) {
        super(message + "\n" + ErrorMsgHelper.createErrMsg(httpMetadata));
        this.httpMetadata = httpMetadata;
    }

    public HttpClientException(Throwable cause, HttpMetadata httpMetadata) {
        super(ErrorMsgHelper.createErrMsg(httpMetadata), cause);
        this.httpMetadata = httpMetadata;
    }

    public HttpClientException(String message, Throwable cause, HttpMetadata httpMetadata) {
        super(message + "\n" + ErrorMsgHelper.createErrMsg(httpMetadata), cause);
        this.httpMetadata = httpMetadata;
    }

    public HttpMetadata getHttpMetadata() {
        return httpMetadata;
    }

    public void setHttpMetadata(HttpMetadata httpMetadata) {
        this.httpMetadata = httpMetadata;
    }
}
