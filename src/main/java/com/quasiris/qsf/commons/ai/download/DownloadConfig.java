package com.quasiris.qsf.commons.ai.download;

import java.io.Serializable;

public class DownloadConfig implements Serializable {

    public static final String AWS_TYPE = "aws";

    // aws
    private String type;
    private AwsDownloadConfig aws;

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
}
