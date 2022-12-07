package com.quasiris.qsf.commons.repo.config;

import java.io.Serializable;

public class DownloadConfig implements Serializable {

    public static final String AWS_TYPE = "aws";
    public static final String HTTP_TYPE = "http";

    private String type = HTTP_TYPE;
    private AwsDownloadConfig aws;
    private HttpDownloadConfig http;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AwsDownloadConfig getAws() {
        return aws;
    }

    public void setAws(AwsDownloadConfig aws) {
        this.aws = aws;
    }

    public HttpDownloadConfig getHttp() {
        return http;
    }

    public void setHttp(HttpDownloadConfig http) {
        this.http = http;
    }
}
