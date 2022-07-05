package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.util.GenericUtils;
import com.quasiris.qsf.commons.util.JsonUtil;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class JsonBodyHandler<W> implements HttpResponse.BodyHandler<W> {
    private final TypeReference<W> typeReference;
    public JsonBodyHandler(TypeReference<W> typeReference) {
        this.typeReference = typeReference;
    }

    private <W> W map(String body, TypeReference<W> typeReference) {
        if(typeReference == null) {
            return null;
        } else if(GenericUtils.instanceOf(String.class, GenericUtils.castClass(typeReference))) {
            return (W) body;
        } else {
            try {
                return JsonUtil.defaultMapper().readValue(body, typeReference);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <W> HttpResponse.BodySubscriber<W> asJSON(TypeReference<W> typeReference) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> map(body, typeReference));
    }

    @Override
    public HttpResponse.BodySubscriber<W> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(typeReference);
    }
}
