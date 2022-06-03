package com.quasiris.qsf.commons.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.http.handler.DefaultHttpErrorHandler;
import com.quasiris.qsf.commons.http.handler.HttpErrorHandler;
import com.quasiris.qsf.commons.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


public class BaseHttpClient implements HttpClient {
    public static final String UTF_8 = "UTF-8";

    private HttpErrorHandler errorHandler = new DefaultHttpErrorHandler();

    private final CloseableHttpClient httpClient;

    private boolean parseResponseOnError;

    public BaseHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public <T> HttpResponse<T> postForResponse(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        appendHeadersAndPayload(httpPost, data, headers);
        return performRequest(httpPost, typeReference, false);
    }

    @Override
    public <T> T post(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        appendHeadersAndPayload(httpPost, data, headers);
        return performRequest(httpPost, typeReference, true).getPayload();
    }

    @Override
    public <T> HttpResponse<T> putForResponse(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        appendHeadersAndPayload(httpPut, data, headers);
        return performRequest(httpPut, typeReference, false);
    }


    @Override
    public <T> T put(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        appendHeadersAndPayload(httpPut, data, headers);
        return performRequest(httpPut, typeReference, true).getPayload();
    }

    private void appendHeadersAndPayload(HttpUriRequestBase requestBase, @Nullable Object data, Header... headers) {
        for (Header header : headers) {
            requestBase.setHeader(header);
        }
        if (data != null) {
            String postString = data instanceof String ? data.toString() : JsonUtil.toJson(data);
            StringEntity entity = new StringEntity(postString, StandardCharsets.UTF_8);
            requestBase.setEntity(entity);
        }
    }

    @Override
    public <T> T get(String url, Class<T> responseValueType, Header... headers) {
        return get(url, castTypeReference(responseValueType), headers);
    }

    @Override
    public <T> HttpResponse<T> getForResponse(String url, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpGet httpGet = new HttpGet(url);
        appendHeadersAndPayload(httpGet, null, headers);
        return performRequest(httpGet, typeReference, false);
    }

    @Override
    public <T> T get(String url, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpGet httpGet = new HttpGet(url);
        appendHeadersAndPayload(httpGet, null, headers);
        return performRequest(httpGet, typeReference, true).getPayload();
    }

    @Override
    public <T> HttpResponse<T> deleteForResponse(String url, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpDelete httpDelete = new HttpDelete(url);
        appendHeadersAndPayload(httpDelete, null, headers);
        return performRequest(httpDelete, typeReference, false);
    }

    @Override
    public <T> T delete(String url, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpDelete httpDelete = new HttpDelete(url);
        appendHeadersAndPayload(httpDelete, null, headers);
        return performRequest(httpDelete, typeReference, true).getPayload();
    }

    @Override
    public <T> HttpResponse<T> headForResponse(String url, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpHead httpHead = new HttpHead(url);
        appendHeadersAndPayload(httpHead, null, headers);
        return performRequest(httpHead, typeReference, false);
    }

    @Override
    public <T> T head(String url, @Nullable TypeReference<T> typeReference, Header... headers) {
        HttpHead httpHead = new HttpHead(url);
        appendHeadersAndPayload(httpHead, null, headers);
        return performRequest(httpHead, typeReference, true).getPayload();
    }

    @Override
    public byte[] getBytes(String url, Header... headers) {
        HttpGet httpGet = new HttpGet(url);
        for (Header header : headers) {
            httpGet.setHeader(header);
        }
        try {
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                if (errorHandler.hasError(response)) {
                    errorHandler.handle(response);
                }
                return IOUtils.toByteArray(response.getEntity().getContent());
            } finally {
                if (response != null && response.getEntity() != null) {
                    EntityUtils.consume(response.getEntity());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Problems while get request", e);
        }
    }

    private <T> HttpResponse<T> performRequest(HttpUriRequestBase request, @Nullable TypeReference<T> typeReference, boolean enableErrorHandler) {
        CloseableHttpResponse response = null;
        try {
            T result = null;
            try {
                response = httpClient.execute(request);
                if (enableErrorHandler && errorHandler.hasError(response)) {
                    errorHandler.handle(response);
                }
                Integer statusCode = null;
                if(response != null) {
                    statusCode = response.getCode();
                    boolean parseResponse = parseResponseOnError || statusCode >= 200 && statusCode < 400;
                    if(response.getEntity() != null && response.getEntity().getContent() != null && parseResponse) {
                        if(typeReference == null) {
                            result = (T) JsonUtil.defaultMapper().readValue(response.getEntity().getContent(), Object.class);
                        } else {
                            result = JsonUtil.defaultMapper().readValue(response.getEntity().getContent(), typeReference);
                        }
                    }
                }
                return new HttpResponse<T>(statusCode, result);
            } finally {
                if (result == null && response != null && response.getEntity() != null) {
                    EntityUtils.consume(response.getEntity());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("rest-call failed!", e);
        }
    }

    @Override
    public void close() throws Exception {
        httpClient.close();
    }

    public boolean isParseResponseOnError() {
        return parseResponseOnError;
    }

    public void setParseResponseOnError(boolean parseResponseOnError) {
        this.parseResponseOnError = parseResponseOnError;
    }

    public HttpErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(HttpErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
