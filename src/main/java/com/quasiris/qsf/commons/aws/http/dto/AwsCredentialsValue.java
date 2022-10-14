package com.quasiris.qsf.commons.aws.http.dto;

public class AwsCredentialsValue {
    private String accessKeyId;
    private String secretAccessKey;
    private String sessionToken;

    public AwsCredentialsValue() {
    }

    public AwsCredentialsValue(String accessKeyId, String secretAccessKey, String sessionToken) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.sessionToken = sessionToken;
    }

    public AwsCredentialsValue(String accessKeyId, String secretAccessKey) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
    }

    public AwsCredentialsValue(AwsCredentialsValue awsCredentialsValue) {
        this.accessKeyId = awsCredentialsValue.accessKeyId;
        this.secretAccessKey = awsCredentialsValue.secretAccessKey;
        this.sessionToken = awsCredentialsValue.sessionToken;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
