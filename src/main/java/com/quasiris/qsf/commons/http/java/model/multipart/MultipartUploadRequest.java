package com.quasiris.qsf.commons.http.java.model.multipart;

import java.util.ArrayList;
import java.util.List;

public class MultipartUploadRequest {
    private List<MultipartUploadItem> items = new ArrayList<>();
    private String boundary;

    public MultipartUploadRequest() {
    }

    public MultipartUploadRequest(List<MultipartUploadItem> items) {
        this.items = items;
    }

    public List<MultipartUploadItem> getItems() {
        return items;
    }

    public void setItems(List<MultipartUploadItem> items) {
        this.items = items;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }
}
