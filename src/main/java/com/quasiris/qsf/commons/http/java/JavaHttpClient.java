package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.cache.Cache;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class JavaHttpClient {
    private static Logger LOG = LoggerFactory.getLogger(JavaHttpClient.class);

    public enum RequestMethod { GET, POST, PUT, PATCH, DELETE }
    public enum EventHook { BEFORE_REQUEST, AFTER_REQUEST, ON_ERROR }

    HttpClient client;
    Duration timeout = Duration.ofSeconds(4);

    Cache<Integer, HttpResponse> cache;

    Map<EventHook, List<HttpHook>> hooks = new HashMap<>();

    int numRetries; // TODO check if this exist for async https://gist.github.com/petrbouda/92647b243eac71b089eb4fb2cfa90bf2

    public JavaHttpClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
    }

    public JavaHttpClient(Duration timeout) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
        this.timeout = timeout;
    }

    public JavaHttpClient(HttpClient client) {
        this.client = client;
    }

    public <T> HttpResponse<T> get(String url, @Nullable TypeReference<T> typeReference, String... headers) throws IOException {
        HttpRequest request = buildRequest(RequestMethod.GET.name(), url, null, headers);
        return performRequest(request, typeReference);
    }

    public <T> HttpResponse<T> post(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) throws IOException {
        HttpRequest request = buildRequest(RequestMethod.POST.name(), url, data, headers);
        return performRequest(request, typeReference);
    }

    public <T> HttpResponse<T> put(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) throws IOException {
        HttpRequest request = buildRequest(RequestMethod.PUT.name(), url, data, headers);
        return performRequest(request, typeReference);
    }

    public <T> HttpResponse<T> request(RequestMethod method, String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) throws IOException {
        HttpRequest request = buildRequest(method.name(), url, data, headers);
        return performRequest(request, typeReference);
    }

    protected <T> HttpResponse<T> performRequest(HttpRequest request, @Nullable TypeReference<T> typeReference) throws IOException {
        HttpResponse<T> httpResponse = null;
        if(hooks.get(EventHook.BEFORE_REQUEST) != null) {
            for (HttpHook httpHook : hooks.get(EventHook.BEFORE_REQUEST)) {
                httpResponse = httpHook.handle(request, typeReference, null, null);
            }
        }

        int i = 0;
        while (httpResponse == null && (i == 0 || i <= numRetries)) {
            i++;
            try {
                httpResponse = send(request, typeReference);
            } catch (IOException | InterruptedException e) {
                boolean hasRetriesLeft = i <= numRetries;
                if(hasRetriesLeft) {
                    LOG.warn("Retry failed request with reason:", e);
                    continue; // retry
                } else {
                    if(hooks.get(EventHook.ON_ERROR) != null) {
                        for (HttpHook httpHook : hooks.get(EventHook.ON_ERROR)) {
                            httpResponse = httpHook.handle(request, typeReference, httpResponse, e);
                        }
                    } else {
                        throw new IOException(e);
                    }
                }
            }
        }

        if(hooks.get(EventHook.AFTER_REQUEST) != null) {
            for (HttpHook httpHook : hooks.get(EventHook.AFTER_REQUEST)) {
                httpResponse = httpHook.handle(request, typeReference, httpResponse, null);
            }
        }

        return httpResponse;
    }

    protected <T> HttpResponse<T> send(HttpRequest request, @Nullable TypeReference<T> typeReference) throws IOException, InterruptedException {
        HttpResponse<T> httpResponse = null;

        // load from cache
        int hash = buildCacheHash(request);
        if(cache != null) {
            HttpResponse cachedResponse = cache.get(hash);
            if(cachedResponse != null) {
                httpResponse = cachedResponse;
            }
        }

        // request
        if(httpResponse == null) {
            httpResponse = client.send(request, new JsonBodyHandler<>(typeReference));
        }

        // update cache
        if(cache != null && is2xx(httpResponse)) {
            cache.put(hash, httpResponse);
        }

        return httpResponse;
    }

    public <T> CompletableFuture<HttpResponse<T>> getAsync(String url, @Nullable TypeReference<T> typeReference, String... headers) {
        HttpRequest request = buildRequest(RequestMethod.GET.name(), url, null, headers);
        return performAsyncRequest(request, typeReference);
    }

    public <T> CompletableFuture<HttpResponse<T>> postAsync(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) {
        HttpRequest request = buildRequest(RequestMethod.POST.name(), url, data, headers);
        return performAsyncRequest(request, typeReference);
    }

    public <T> CompletableFuture<HttpResponse<T>> putAsync(String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) {
        HttpRequest request = buildRequest(RequestMethod.PUT.name(), url, data, headers);
        return performAsyncRequest(request, typeReference);
    }

    public <T> CompletableFuture<HttpResponse<T>> requestAsync(RequestMethod method, String url, @Nullable Object data, @Nullable TypeReference<T> typeReference, String... headers) {
        HttpRequest request = buildRequest(method.name(), url, data, headers);
        return performAsyncRequest(request, typeReference);
    }

    protected <T> CompletableFuture<HttpResponse<T>> performAsyncRequest(HttpRequest request, @Nullable TypeReference<T> typeReference) {
        CompletableFuture<HttpResponse<T>> future = null;
        if(hooks.get(EventHook.BEFORE_REQUEST) != null) {
            for (HttpHook httpHook : hooks.get(EventHook.BEFORE_REQUEST)) {
                try {
                    HttpResponse<T> httpResponse = httpHook.handle(request, typeReference, null, null);
                    future = CompletableFuture.completedFuture(httpResponse);
                } catch (IOException ignored) {
                }
            }
        }

        // TODO implement num retries
        future = sendAsync(request, typeReference);

        future = future.thenApply(resp -> {
            if(hooks.get(EventHook.AFTER_REQUEST) != null) {
                for (HttpHook httpHook : hooks.get(EventHook.AFTER_REQUEST)) {
                    try {
                        resp = httpHook.handle(request, typeReference, resp, null);
                    } catch (IOException ignored) {
                    }
                }
            }

            return resp;
        });

        // TODO add EventHook.ON_ERROR

        return future;
    }

    protected <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, @Nullable TypeReference<T> typeReference) {
        CompletableFuture<HttpResponse<T>> future = null;

        // load from cache
        int hash = buildCacheHash(request);
        if(cache != null) {
            HttpResponse<T> cachedResponse = cache.get(hash);
            if(cachedResponse != null) {
                future = CompletableFuture.completedFuture(cachedResponse);
            }
        }

        // request
        if(future == null) {
            future = client.sendAsync(request, new JsonBodyHandler<>(typeReference));
        }

        // update cache
        future = future.thenApply(httpResponse -> {
            if(cache != null && is2xx(httpResponse)) {
                cache.put(hash, httpResponse);
            }
            return httpResponse;
        });

        return future;
    }

    protected static HttpRequest buildRequest(String method, String url, @Nullable Object data, String... headers) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url));
        if(headers != null && headers.length > 0) {
            for (String header : headers) {
                String[] kv = header.split(":");
                if(kv.length != 2) {
                    LOG.warn("Wrong Header: "+kv[0]);
                    continue; // skip broken header
                }
                requestBuilder.setHeader(kv[0], kv[1]);
            }
        }
        if (data != null) {
            boolean hasContentTypeHeader = false; // TODO read from headers
            if(!hasContentTypeHeader) {
                requestBuilder.setHeader("Content-Type", "application/json; charset=utf-8");
            }
            String postString = data instanceof String ? data.toString() : JsonUtil.toJson(data);
            requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(postString));
        }
        return requestBuilder.build();
    }

    public static boolean is2xx(HttpResponse response) {
        return response != null && response.statusCode() >= 200 && response.statusCode() < 300;
    }

    protected static Integer buildCacheHash(HttpRequest request) {
        return request.hashCode() + request.bodyPublisher().hashCode();
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
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
}
