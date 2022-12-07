package com.quasiris.qsf.commons.repo.config;

import com.quasiris.qsf.commons.util.IOUtils;

import java.io.Serializable;

public class ModelRepositoryConfig implements Serializable {

    private String uploadBaseUrl;
    private String modelBasePath;

    private DownloadConfig downloadConfig;

    public String getUploadBaseUrl() {
        return uploadBaseUrl;
    }

    public void setUploadBaseUrl(String uploadBaseUrl) {
        this.uploadBaseUrl = IOUtils.ensureEndingSlash(uploadBaseUrl);;
    }

    public String getModelBasePath() {
        return modelBasePath;
    }

    public void setModelBasePath(String modelBasePath) {
        this.modelBasePath = IOUtils.ensureEndingSlash(modelBasePath);
    }

    public DownloadConfig getDownloadConfig() {
        return downloadConfig;
    }

    public void setDownloadConfig(DownloadConfig downloadConfig) {
        this.downloadConfig = downloadConfig;
    }
}
