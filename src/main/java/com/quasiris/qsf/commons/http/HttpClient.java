package com.quasiris.qsf.commons.http;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.hc.core5.http.Header;

import javax.annotation.Nullable;

public interface HttpClient extends AutoCloseable {
    <T> HttpResponse<T> postForResponse(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> HttpResponse<T> putForResponse(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> HttpResponse<T> getForResponse(String url, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> HttpResponse<T> deleteForResponse(String url, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> HttpResponse<T> headForResponse(String url, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> T post(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> T put(String url, @Nullable Object data, @Nullable TypeReference<T> responseValueType, Header... headers);

    <T> T get(String url, Class<T> responseValueType, Header... headers);

    <T> T get(String url, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> T delete(String url, @Nullable TypeReference<T> typeReference, Header... headers);

    <T> T head(String url, @Nullable TypeReference<T> typeReference, Header... headers);

    byte[] getBytes(String url, Header... headers);

    @Override
    void close() throws Exception;
}
