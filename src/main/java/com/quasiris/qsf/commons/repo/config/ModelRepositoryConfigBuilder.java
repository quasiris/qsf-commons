package com.quasiris.qsf.commons.repo.config;

public class ModelRepositoryConfigBuilder {
    private String modelBasePath;
    private String uploadBaseUrl;
    private String modelBaseUrl;

    // Private constructor to enforce usage of the builder methods
    private ModelRepositoryConfigBuilder() {
    }

    public static ModelRepositoryConfigBuilder create() {
        return new ModelRepositoryConfigBuilder();
    }

    public ModelRepositoryConfigBuilder modelBasePath(String modelBasePath) {
        this.modelBasePath = modelBasePath;
        return this;
    }

    public ModelRepositoryConfigBuilder uploadBaseUrl(String uploadBaseUrl) {
        this.uploadBaseUrl = uploadBaseUrl;
        return this;
    }

    public ModelRepositoryConfigBuilder modelBaseUrl(String modelBaseUrl) {
        this.modelBaseUrl = modelBaseUrl;
        return this;
    }

    public String getModelBasePath() {
        return modelBasePath;
    }

    public String getUploadBaseUrl() {
        return uploadBaseUrl;
    }

    public String getModelBaseUrl() {
        return modelBaseUrl;
    }

    @Override
    public String toString() {
        return "ModelRepositoryConfig{" +
                "modelBasePath='" + modelBasePath + '\'' +
                ", uploadBaseUrl='" + uploadBaseUrl + '\'' +
                ", modelBaseUrl='" + modelBaseUrl + '\'' +
                '}';
    }

    public ModelRepositoryConfig build() {
        ModelRepositoryConfig modelRepositoryConfig = new ModelRepositoryConfig();
        modelRepositoryConfig.setModelBasePath(modelBasePath);
        modelRepositoryConfig.setUploadBaseUrl(uploadBaseUrl);
        if(modelBaseUrl != null) {
            DownloadConfig downloadConfig = new DownloadConfig();
            downloadConfig.setType("http");
            HttpDownloadConfig http = new HttpDownloadConfig();
            http.setBaseUrl(modelBaseUrl);
            downloadConfig.setHttp(http);
            modelRepositoryConfig.setDownloadConfig(downloadConfig);
        }

        return modelRepositoryConfig;
    }
}
