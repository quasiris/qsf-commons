package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;
import jakarta.annotation.Nullable;
import com.quasiris.qsf.commons.http.java.exception.HttpClientStatusException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Default404HttpHook implements HttpHook {
    @Override
    public <T> HttpResponse<T> handle(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpResponse<T> httpResponse, Throwable e, HttpMetadata metadata) {
        if (httpResponse != null && httpResponse.statusCode() == 404) {
            metadata.getResponse().setStatusCode(404);
            metadata.getResponse().setHeaders(httpResponse.headers().map());
            throw new HttpClientStatusException(e, metadata);
        }
        return httpResponse;
    }
}
