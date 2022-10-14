package com.quasiris.qsf.commons.aws.http.dto;

public class AwsCredentialsLocation {
    private static final String DEFAULT_ACCESS_KEY_ID_PATH = "AccessKeyId";
    private static final String DEFAULT_SECRET_ACCESS_KEY_PATH = "SecretAccessKey";
    private static final String DEFAULT_TOKEN_PATH = "Token";

    private String uri;
    private String accessKeyIdPathKey = DEFAULT_ACCESS_KEY_ID_PATH;
    private String secretAccessKeyPathKey = DEFAULT_SECRET_ACCESS_KEY_PATH;
    private String sessionTokenPathKey = DEFAULT_TOKEN_PATH;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAccessKeyIdPathKey() {
        return accessKeyIdPathKey;
    }

    public void setAccessKeyIdPathKey(String accessKeyIdPathKey) {
        this.accessKeyIdPathKey = accessKeyIdPathKey;
    }

    public String getSecretAccessKeyPathKey() {
        return secretAccessKeyPathKey;
    }

    public void setSecretAccessKeyPathKey(String secretAccessKeyPathKey) {
        this.secretAccessKeyPathKey = secretAccessKeyPathKey;
    }

    public String getSessionTokenPathKey() {
        return sessionTokenPathKey;
    }

    public void setSessionTokenPathKey(String sessionTokenPathKey) {
        this.sessionTokenPathKey = sessionTokenPathKey;
    }
}
