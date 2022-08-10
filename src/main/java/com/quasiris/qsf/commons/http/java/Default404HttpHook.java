package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.exception.ResourceNotFoundException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Default404HttpHook implements HttpHook {
    @Override
    public <T> HttpResponse<T> handle(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpResponse<T> httpResponse, Exception e) throws IOException {
        if(httpResponse != null && httpResponse.statusCode() == 404) {
            throw new ResourceNotFoundException("Resource "+request.uri().toString()+" not found!", e);
        }
        return httpResponse;
    }
}
