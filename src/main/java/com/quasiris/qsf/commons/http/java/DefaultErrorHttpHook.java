package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DefaultErrorHttpHook implements HttpHook {
    @Override
    public <T> HttpResponse<T> handle(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpResponse<T> httpResponse, Exception e) throws IOException {
        if(httpResponse == null) {
            throw new IOException("Response to url: "+request.uri().toString()+" was not successful! Response is null!", e);
        } else if(httpResponse.statusCode() >= 400) {
            throw new IOException("Response to url: "+request.uri().toString()+" was not successful! Return code: "+httpResponse.statusCode(), e);
        }
        return httpResponse;
    }
}
