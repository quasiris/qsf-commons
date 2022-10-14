package com.quasiris.qsf.commons.aws.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quasiris.qsf.commons.aws.http.dto.AwsCredentials;
import com.quasiris.qsf.commons.aws.http.dto.AwsCredentialsLocation;
import com.quasiris.qsf.commons.aws.http.dto.AwsCredentialsValue;
import com.quasiris.qsf.commons.util.JsonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class AwsCredentialsHelper {
    public static AwsCredentialsValue getCredentials(AwsCredentials credentials) {
        if (credentials.getLocation() != null) {
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
