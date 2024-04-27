package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.http.java.exception.HttpClientParseException;
import com.quasiris.qsf.commons.http.java.exception.HttpClientStatusException;
import com.quasiris.qsf.commons.http.java.exception.HttpClientUnexpectedException;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;
import com.quasiris.qsf.commons.util.GenericUtils;
import com.quasiris.qsf.commons.util.JsonUtil;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class JsonBodyHandler<W> implements HttpResponse.BodyHandler<W> {
    private final TypeReference<W> typeReference;
    private final HttpMetadata metadata;

    public JsonBodyHandler(TypeReference<W> typeReference, HttpMetadata metadata) {
        this.typeReference = typeReference;
        this.metadata = metadata;
    }

    private <W> W map(HttpResponse.ResponseInfo responseInfo, String body, TypeReference<W> typeReference) {
        copyMetadata(responseInfo, body);
        validateResponse(responseInfo);

        if (typeReference == null) {
            return null;
        } else if (GenericUtils.instanceOf(String.class, GenericUtils.castClass(typeReference))) {
            return (W) body;
        } else {
            try {
                return JsonUtil.defaultMapper().readValue(body, typeReference);
            } catch (JsonProcessingException e) {
                throw new HttpClientParseException("Unable to parse the response", e, metadata);
            }
        }
    }

    private void validateResponse(HttpResponse.ResponseInfo responseInfo) {
        if (responseInfo.statusCode() >= 400) {
            throw new HttpClientStatusException(metadata);
        }
    }

    private void copyMetadata(HttpResponse.ResponseInfo responseInfo, Object body) {
        metadata.getResponse().setBody(body);
        metadata.getResponse().setHeaders(responseInfo.headers().map());
        metadata.getResponse().setStatusCode(responseInfo.statusCode());
    }

    private <W> HttpResponse.BodySubscriber<W> asJSON(TypeReference<W> typeReference, HttpResponse.ResponseInfo responseInfo) {
        Class<?> clsTarget = GenericUtils.castClass(typeReference);
        if (isGzipEncoding(responseInfo.headers())) {
            if (GenericUtils.instanceOf(byte[].class, clsTarget)) {
                return HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.ofByteArray(),
                        body -> {
                            try (InputStream bais = new ByteArrayInputStream(body);
                                 InputStream gzipInputStream = new GZIPInputStream(bais)) {
                                return mapBytes(responseInfo, IOUtils.toByteArray(gzipInputStream));
                            } catch (IOException e) {
                                throw new HttpClientUnexpectedException("Couldn't convert gzip response", metadata);
                            }
                        }
                );
            } else if (GenericUtils.instanceOf(InputStream.class, clsTarget)) {
                return HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.ofInputStream(),
                        body -> mapInputStream(responseInfo, body)
                );
            }
            return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.ofByteArray(),
                    body -> {
                        try (InputStream bais = new ByteArrayInputStream(body);
                             InputStream gzipInputStream = new GZIPInputStream(bais)) {
                            return map(responseInfo,
                                    IOUtils.toString(gzipInputStream, StandardCharsets.UTF_8),
                                    typeReference);
                        } catch (IOException e) {
                            throw new HttpClientUnexpectedException("Couldn't convert gzip response", metadata);
                        }
                    }
            );
        }
        if (GenericUtils.instanceOf(byte[].class, clsTarget)) {
            return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.ofByteArray(),
                    body -> mapBytes(responseInfo, body)
            );
        } else if (GenericUtils.instanceOf(InputStream.class, clsTarget)) {
            return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.ofInputStream(),
                    body -> mapInputStream(responseInfo, body)
            );
        }
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> map(responseInfo, body, typeReference));
    }

    public static boolean isGzipEncoding(HttpHeaders headers) {
        String encoding = headers.firstValue("Content-Encoding")
                .orElse(null);
        return "gzip".equals(encoding);
    }

    private <W> W mapInputStream(HttpResponse.ResponseInfo responseInfo, InputStream body) {
        copyMetadata(responseInfo, body);
        validateResponse(responseInfo);
        return (W) body;
    }

    private <W> W mapBytes(HttpResponse.ResponseInfo responseInfo, byte[] body) {
        copyMetadata(responseInfo, body);
        validateResponse(responseInfo);
        return (W) body;
    }

    @Override
    public HttpResponse.BodySubscriber<W> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(typeReference, responseInfo);
    }
}
