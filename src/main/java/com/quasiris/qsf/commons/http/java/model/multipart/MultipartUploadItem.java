package com.quasiris.qsf.commons.http.java.model.multipart;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.InputStream;

public class MultipartUploadItem {
    private String fileName;
    private String contentType = "application/octet-stream";
    @JsonIgnore
    private InputStream conentInputStream;

    public MultipartUploadItem() {
    }

    public MultipartUploadItem(String fileName, String contentType, InputStream conentInputStream) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.conentInputStream = conentInputStream;
    }


    public MultipartUploadItem(String fileName, InputStream conentInputStream) {
        this.fileName = fileName;
        this.conentInputStream = conentInputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getConentInputStream() {
        return conentInputStream;
    }

    public void setConentInputStream(InputStream conentInputStream) {
        this.conentInputStream = conentInputStream;
    }

}
