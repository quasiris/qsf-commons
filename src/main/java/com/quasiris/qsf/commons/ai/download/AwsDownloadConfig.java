package com.quasiris.qsf.commons.ai.download;

import com.quasiris.qsf.commons.aws.http.dto.AwsCredentials;

public class AwsDownloadConfig {
    private String service;
    private String region;

    private AwsCredentials credentials;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public AwsCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(AwsCredentials credentials) {
        this.credentials = credentials;
    }
}
