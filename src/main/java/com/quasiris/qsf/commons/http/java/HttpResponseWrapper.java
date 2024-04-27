package com.quasiris.qsf.commons.http.java;

import com.quasiris.qsf.commons.http.java.exception.HttpClientUnexpectedException;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

public class HttpResponseWrapper implements HttpResponse<InputStream> {
    private final HttpResponse<InputStream> initialResponse;
    private final HttpMetadata metadata;

    public HttpResponseWrapper(HttpResponse<InputStream> initialResponse,
                               HttpMetadata metadata) {
        this.initialResponse = initialResponse;
        this.metadata = metadata;
    }

    @Override
    public int statusCode() {
        return initialResponse.statusCode();
    }

    @Override
    public HttpRequest request() {
        return initialResponse.request();
    }

    @Override
    public Optional<HttpResponse<InputStream>> previousResponse() {
        return initialResponse.previousResponse()
                .map(initialResponse1 -> new HttpResponseWrapper(initialResponse1, metadata));
    }

    @Override
    public HttpHeaders headers() {
        return initialResponse.headers();
    }

    @Override
    public InputStream body() {
        try {
            return new GZIPInputStream(initialResponse.body());
        } catch (IOException e) {
            throw new HttpClientUnexpectedException("Couldn't convert gzip input stream", e, metadata);
        }
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return initialResponse.sslSession();
    }

    @Override
    public URI uri() {
        return initialResponse.uri();
    }

    @Override
    public HttpClient.Version version() {
        return initialResponse.version();
    }
}
