package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;
import jakarta.annotation.Nullable;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Hook for http requests. Useful for error handling, repair, override request or throw IOException/ RuntimeException
 */
public interface HttpHook {
    <T> HttpResponse<T> handle(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpResponse<T> httpResponse, Throwable e, HttpMetadata metadata);
}
