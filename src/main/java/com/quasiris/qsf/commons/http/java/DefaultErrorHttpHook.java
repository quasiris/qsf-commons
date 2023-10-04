package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;
import jakarta.annotation.Nullable;
import com.quasiris.qsf.commons.http.java.exception.HttpClientStatusException;
import com.quasiris.qsf.commons.http.java.exception.HttpClientUnexpectedException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DefaultErrorHttpHook implements HttpHook {
    @Override
    public <T> HttpResponse<T> handle(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpResponse<T> httpResponse, Throwable e, HttpMetadata metadata) {
        if (httpResponse == null) {
            throw new HttpClientUnexpectedException(e, metadata);
        } else if (httpResponse.statusCode() >= 400) {
            throw new HttpClientStatusException(e, metadata);
        }
        return httpResponse;
    }
}
