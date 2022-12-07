package com.quasiris.qsf.commons.repo.config;

public class ModelRepositoryConfigHolder {

    private static ModelRepositoryConfig modelRepositoryConfig;

    public static ModelRepositoryConfig getModelRepositoryConfig() {
        return modelRepositoryConfig;
    }

    public static void setModelRepositoryConfig(ModelRepositoryConfig modelRepositoryConfig) {
        ModelRepositoryConfigHolder.modelRepositoryConfig = modelRepositoryConfig;
    }
}
