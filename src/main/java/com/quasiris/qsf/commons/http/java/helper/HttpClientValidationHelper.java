package com.quasiris.qsf.commons.http.java.helper;

import com.quasiris.qsf.commons.http.java.exception.HttpClientValidationException;
import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

import java.io.InputStream;

public class HttpClientValidationHelper {
    public static void validate(HttpMetadata metadata, int retries) {
        if (metadata.getRequest().getBody() instanceof InputStream && retries > 0) {
            throw new HttpClientValidationException("It's not allowed to use InputStream with retries greater than 0", metadata);
        }
    }
}
