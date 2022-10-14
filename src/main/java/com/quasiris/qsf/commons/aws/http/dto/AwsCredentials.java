package com.quasiris.qsf.commons.aws.http.dto;

public class AwsCredentials {
    private AwsCredentialsValue value;
    private AwsCredentialsLocation location;

    public AwsCredentialsValue getValue() {
        return value;
    }

    public void setValue(AwsCredentialsValue value) {
        this.value = value;
    }

    public AwsCredentialsLocation getLocation() {
        return location;
    }

    public void setLocation(AwsCredentialsLocation location) {
        this.location = location;
    }
}
