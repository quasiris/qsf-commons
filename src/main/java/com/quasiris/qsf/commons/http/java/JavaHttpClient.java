package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.http.java.exception.*;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;
import com.quasiris.qsf.commons.util.HttpUtil;
import com.quasiris.qsf.commons.util.JsonUtil;
import com.quasiris.qsf.commons.util.UrlUtil;
import com.quasiris.qsf.commons.util.model.Holder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.cache.Cache;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class JavaHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(JavaHttpClient.class);

    public enum RequestMethod {GET, POST, PUT, PATCH, DELETE, HEAD}

    public enum EventHook {BEFORE_REQUEST, AFTER_REQUEST, ON_ERROR}

    HttpClient client;

    private Duration requestTimeout = Duration.ofSeconds(4);
    Cache<Integer, HttpResponse> cache;

    Map<EventHook, List<HttpHook>> hooks = new HashMap<>();

    int numRetries; // TODO check if this exist for async https://gist.github.com/petrbouda/92647b243eac71b089eb4fb2cfa90bf2

    public JavaHttpClient() {
        // Connection timeout is forever, request timeout is default
        this.client = HttpClient.newBuilder().build();
    }

    public JavaHttpClient(Duration requestTimeout) {
        // Connection timeout is forever, request timeout is defined
        this.client = HttpClient.newBuilder().build();
        this.requestTimeout = requestTimeout;
    }

    public JavaHttpClient(Duration connectionTimeout, Duration requestTimeout) {
        HttpClient.Builder builder = HttpClient.newBuilder();
        if (connectionTimeout != null) {
            builder = builder.connectTimeout(connectionTimeout);
        }
        this.client = builder.build();
        this.requestTimeout = requestTimeout;
    }

    public JavaHttpClient(HttpClient client) {
        this.client = client;
    }

    public JavaHttpClient(HttpClient client, Duration requestTimeout) {
        this.client = client;
        this.requestTimeout = requestTimeout;
    }

    public <T> HttpResponse<T> get(String url, @Nullable TypeReference<T> typeReference, String... headers) throws HttpClientException {
        return request(RequestMethod.GET, url, null, typeReference, headers);
    }

    public <T> HttpResponse<T> post(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) throws HttpClientException {
        return request(RequestMethod.POST, url, data, typeReference, headers);
    }

    public <T> HttpResponse<T> put(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) throws HttpClientException {
        return request(RequestMethod.PUT, url, data, typeReference, headers);
    }

    public <T> HttpResponse<T> delete(String url, @Nullable TypeReference<T> typeReference, String... headers) throws HttpClientException {
        return request(RequestMethod.DELETE, url, null, typeReference, headers);
    }

    public HttpResponse<String> head(String url, String... headers) throws HttpClientException {
        return request(RequestMethod.HEAD, url, null, new TypeReference<String>() {
        }, headers);
    }

    public <T> HttpResponse<T> request(RequestMethod method, String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) throws HttpClientException {
        return request(method, url, data, typeReference, null, headers);
    }

    public <T> HttpResponse<T> request(RequestMethod method, String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, Duration requestTimeout, String... headers) throws HttpClientException {
        HttpMetadata metadata = new HttpMetadata();
        HttpRequest request = buildRequest(method.name(), url, data, metadata, requestTimeout, headers);
        return performRequest(request, typeReference, metadata);
    }

    protected <T> HttpResponse<T> performRequest(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpMetadata metadata) throws HttpClientException {
        Holder<HttpResponse<T>> holder = new Holder<>();
        if (hooks.get(EventHook.BEFORE_REQUEST) != null) {
            for (HttpHook httpHook : hooks.get(EventHook.BEFORE_REQUEST)) {
                holder.value = httpHook.handle(request, typeReference, null, null, metadata);
            }
        }

        int i = 0;
        while (holder.value == null && (i == 0 || i <= numRetries)) {
            i++;
            tryToSend(request, typeReference, metadata, holder, i);
        }

        if (hooks.get(EventHook.AFTER_REQUEST) != null) {
            for (HttpHook httpHook : hooks.get(EventHook.AFTER_REQUEST)) {
                holder.value = httpHook.handle(request, typeReference, holder.value, null, metadata);
            }
        }

        return holder.value;
    }

    private <T> void tryToSend(HttpRequest request, TypeReference<T> typeReference, HttpMetadata metadata, Holder<HttpResponse<T>> holder, int currentTry) {
        try {
            holder.value = send(request, typeReference, metadata);
        } catch (Exception e) {
            if (!handleRetriesAndErrorHook(request, typeReference, metadata, holder, currentTry, e)) {
                Throwable cause = e.getCause();
                if (cause instanceof HttpClientParseException) {
                    throw new HttpClientParseException(e, metadata);
                } else if (cause instanceof HttpClientStatusException) {
                    throw new HttpClientStatusException(e, metadata);
                } else {
                    throw new HttpClientUnexpectedException(e, metadata);
                }
            }
        }
    }

    private <T> boolean handleRetriesAndErrorHook(HttpRequest request, TypeReference<T> typeReference, HttpMetadata metadata, Holder<HttpResponse<T>> httpResponseHolder, int currentTry, Exception e) {
        boolean hasRetriesLeft = currentTry <= numRetries;
        if (hasRetriesLeft && shouldRetry(metadata)) {
            metadata.setRetries(metadata.getRetries() + 1);
            LOG.warn("Retry failed request with reason:", e);
            return true;
        } else if (hooks.get(EventHook.ON_ERROR) != null) {
            for (HttpHook httpHook : hooks.get(EventHook.ON_ERROR)) {
                httpResponseHolder.value = httpHook.handle(request, typeReference, httpResponseHolder.value, e, metadata);
            }
            return true;
        }
        return false;
    }


    private boolean shouldRetry(HttpMetadata metadata) {
        boolean shouldRetry = true;
        if (metadata.getResponse().getStatusCode() != null) {
            if (metadata.getResponse().getStatusCode() == 404) {
                shouldRetry = false;
            }
        }
        return shouldRetry;
    }

    protected <T> HttpResponse<T> send(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpMetadata metadata) throws IOException, InterruptedException {
        HttpResponse<T> httpResponse = null;

        // load from cache
        int hash = buildCacheHash(request);
        if (cache != null) {
            HttpResponse cachedResponse = cache.get(hash);
            if (cachedResponse != null) {
                httpResponse = cachedResponse;
                metadata.getResponse().setStatusCode(httpResponse.statusCode());
                metadata.getResponse().setBody(httpResponse.body());
                metadata.getResponse().setHeaders(httpResponse.headers().map());
            }
        }

        // request
        if (httpResponse == null) {
            httpResponse = client.send(request, new JsonBodyHandler<>(typeReference, metadata));
        }

        if (httpResponse != null && httpResponse.statusCode() < 400) {
            // update cache
            if (cache != null) {
                cache.put(hash, httpResponse);
            }
        }

        return httpResponse;
    }

    public <T> CompletableFuture<HttpResponse<T>> getAsync(String url, @Nullable TypeReference<T> typeReference, String... headers) {
        return requestAsync(RequestMethod.GET, url, null, typeReference, headers);
    }

    public <T> CompletableFuture<HttpResponse<T>> postAsync(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) {
        return requestAsync(RequestMethod.POST, url, data, typeReference, headers);
    }

    public <T> CompletableFuture<HttpResponse<T>> putAsync(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) {
        return requestAsync(RequestMethod.PUT, url, data, typeReference, headers);
    }

    public <T> CompletableFuture<HttpResponse<T>> requestAsync(RequestMethod method, String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) {
        HttpMetadata metadata = new HttpMetadata();
        HttpRequest request = buildRequest(method.name(), url, data, metadata, null, headers);
        return performAsyncRequest(request, typeReference, metadata);
    }

    protected <T> CompletableFuture<HttpResponse<T>> performAsyncRequest(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpMetadata metadata) {
        CompletableFuture<HttpResponse<T>> future = null;
        if (hooks.get(EventHook.BEFORE_REQUEST) != null) {
            for (HttpHook httpHook : hooks.get(EventHook.BEFORE_REQUEST)) {
                HttpResponse<T> httpResponse = httpHook.handle(request, typeReference, null, null, metadata);
                future = CompletableFuture.completedFuture(httpResponse);
            }
        }

        future = sendAsync(request, typeReference, metadata);

        future = future.thenApply(resp -> {
            if (hooks.get(EventHook.AFTER_REQUEST) != null) {
                for (HttpHook httpHook : hooks.get(EventHook.AFTER_REQUEST)) {
                    resp = httpHook.handle(request, typeReference, resp, null, metadata);
                }
            }

            return resp;
        });

        return future;
    }

    protected <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, @Nullable TypeReference<T> typeReference, HttpMetadata metadata) {
        CompletableFuture<HttpResponse<T>> future = null;

        // load from cache
        int hash = buildCacheHash(request);
        if (cache != null) {
            HttpResponse<T> cachedResponse = cache.get(hash);
            if (cachedResponse != null) {
                metadata.getResponse().setBody(cachedResponse.body());
                metadata.getResponse().setStatusCode(cachedResponse.statusCode());
                metadata.getResponse().setHeaders(cachedResponse.headers().map());
                future = CompletableFuture.completedFuture(cachedResponse);
            }
        }

        // request
        if (future == null) {
            future = client.sendAsync(request, new JsonBodyHandler<>(typeReference, metadata));
            for (int i = 0; i < numRetries; i++) {
                future = future.exceptionally(throwable -> {
                    if (throwable.getCause() instanceof HttpClientException) {
                        HttpClientException cause = (HttpClientException) throwable.getCause();
                        if (!shouldRetry(cause.getHttpMetadata())) {
                            return (HttpResponse<T>) CompletableFuture.failedFuture(throwable).join();
                        }
                    }
                    LOG.warn("Retry failed request with reason:", throwable);
                    metadata.setRetries(metadata.getRetries() + 1);
                    return client.sendAsync(request, new JsonBodyHandler<>(typeReference, metadata)).join();
                });
            }
            future = future.handle((httpResponse, throwable) -> {
                if (throwable != null) {
                    Throwable cause = throwable.getCause();
                    if (hooks.get(EventHook.ON_ERROR) != null) {
                        HttpResponse<T> retValue = null;
                        for (HttpHook httpHook : hooks.get(EventHook.ON_ERROR)) {
                            retValue = httpHook.handle(request, typeReference, retValue, cause, metadata);
                        }
                    }
                    if (cause instanceof HttpClientParseException) {
                        throw new HttpClientParseException(throwable, metadata);
                    } else if (cause instanceof HttpClientStatusException) {
                        throw new HttpClientStatusException(throwable, metadata);
                    } else {
                        throw new HttpClientUnexpectedException(throwable, metadata);
                    }
                }
                return httpResponse;
            });
        }

        // update cache
        future = future.thenApply(httpResponse -> {
            if (cache != null && is2xx(httpResponse)) {
                cache.put(hash, httpResponse);
            }
            return httpResponse;
        });

        return future;
    }

    protected HttpRequest buildRequest(String method, String url, @Nullable Object data, HttpMetadata metadata, @Nullable Duration requestTimeout, String... headers) {
        metadata.getRequest().setMethod(method);
        metadata.getRequest().setBody(data);
        metadata.getRequest().setUri(url);
        ArrayList<String> headerList = headers != null && headers.length > 0 ?
                new ArrayList<>(Arrays.asList(headers))
                : new ArrayList<>();

        requestTimeout = requestTimeout != null ? requestTimeout : this.requestTimeout;

        // add basic auth header
        try {
            String usernamePassword = UrlUtil.extractUsernamePassword(url);
            if (StringUtils.isNotEmpty(usernamePassword)) {
                String[] userPwParts = usernamePassword.split(":", 2);
                String basicAuthHeader = HttpUtil.createBasicAuthHeader(userPwParts[0], userPwParts[1]);
                headerList.add(basicAuthHeader);
                url = UrlUtil.removePassword(url);
            }
        } catch (MalformedURLException e) {
            throw new HttpClientMalformedUrlException(e, metadata);
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url));
        if (requestTimeout != null) {
            requestBuilder = requestBuilder.timeout(requestTimeout);
        }
        boolean hasContentTypeHeader = false;
        for (String header : headerList) {
            String[] kv = header.split(":", 2);
            if (kv.length != 2) {
                LOG.warn("Wrong Header: " + kv[0]);
                continue; // skip broken header
            }
            String key = kv[0];
            String value = kv[1];
            if (StringUtils.equalsIgnoreCase(key, "Content-Type")) {
                hasContentTypeHeader = true;
            }
            requestBuilder.setHeader(key, value);
        }
        if (data != null) {
            if (!hasContentTypeHeader) {
                requestBuilder.setHeader("Content-Type", "application/json; charset=utf-8");
            }
            if (data instanceof byte[]) {
                metadata.getRequest().setBody(data);
                requestBuilder.method(method, HttpRequest.BodyPublishers.ofByteArray((byte[]) data));
            } else if (data instanceof String) {
                metadata.getRequest().setBody(data);
                requestBuilder.method(method, HttpRequest.BodyPublishers.ofString((String) data));
            } else {
                String postString = JsonUtil.toJson(data);
                metadata.getRequest().setBody(postString);
                requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(postString));
            }
        } else {
            requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
        }
        return requestBuilder.build();
    }

    public static boolean is2xx(HttpResponse response) {
        return response != null && response.statusCode() >= 200 && response.statusCode() < 300;
    }

    protected static Integer buildCacheHash(HttpRequest request) {
        return request.hashCode() + request.bodyPublisher().hashCode();
    }

    public Map<EventHook, List<HttpHook>> getHooks() {
        return hooks;
    }

    public void setHooks(Map<EventHook, List<HttpHook>> hooks) {
        this.hooks = hooks;
    }

    public int getNumRetries() {
        return numRetries;
    }

    public void setNumRetries(int numRetries) {
        this.numRetries = numRetries;
    }

    public Cache<Integer, HttpResponse> getCache() {
        return cache;
    }

    public void setCache(Cache<Integer, HttpResponse> cache) {
        this.cache = cache;
    }

    public Optional<Duration> getRequestTimeout() {
        return Optional.ofNullable(this.requestTimeout);
    }

    public void setRequestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Optional<Duration> getConnectionTimeout() {
        return client.connectTimeout();
    }
}
