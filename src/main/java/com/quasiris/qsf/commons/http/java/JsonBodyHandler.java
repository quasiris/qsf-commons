package com.quasiris.qsf.commons.http.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.util.GenericUtils;
import com.quasiris.qsf.commons.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class JsonBodyHandler<W> implements HttpResponse.BodyHandler<W> {
    private final TypeReference<W> typeReference;
    public JsonBodyHandler(TypeReference<W> typeReference) {
        this.typeReference = typeReference;
    }

    private <W> W map(HttpResponse.ResponseInfo responseInfo, String body, TypeReference<W> typeReference) {
        if(responseInfo.statusCode() >= 400) {
            String errMsg = "Request with status code "+responseInfo.statusCode()+" not successful!";
            if(StringUtils.isNotEmpty(body)) {
                errMsg += " Body: "+body;
            }
            throw new RuntimeException(errMsg);
        }

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

    private <W> HttpResponse.BodySubscriber<W> asJSON(TypeReference<W> typeReference, HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> map(responseInfo, body, typeReference));
    }

    @Override
    public HttpResponse.BodySubscriber<W> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(typeReference, responseInfo);
    }
}
