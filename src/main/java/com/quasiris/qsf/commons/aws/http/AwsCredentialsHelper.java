package com.quasiris.qsf.commons.aws.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.util.JsonUtil;
import com.quasiris.qsf.dto.http.aws.AwsCredentials;
import com.quasiris.qsf.dto.http.aws.AwsCredentialsLocation;
import com.quasiris.qsf.dto.http.aws.AwsCredentialsValue;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class AwsCredentialsHelper {
    public static AwsCredentialsValue getCredentials(AwsCredentials credentials) {
        if (credentials.getLocation() != null &&
            (!Boolean.TRUE.equals(credentials.getPrioritizeValue()))
        ) {
            return getCredentials(credentials.getLocation());
        }
        return new AwsCredentialsValue(credentials.getValue());
    }

    public static AwsCredentialsValue getCredentials(AwsCredentialsLocation location) {
        try {
            URL url = new URL(location.getUri());
            Map<String, String> response = JsonUtil.defaultMapper().readValue(url, new TypeReference<>() {
            });
            String accessKeyId = response.get(location.getAccessKeyIdPathKey());
            String secretAccessKey = response.get(location.getSecretAccessKeyPathKey());
            String sessionToken = response.get(location.getSessionTokenPathKey());
            return new AwsCredentialsValue(accessKeyId, secretAccessKey, sessionToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
