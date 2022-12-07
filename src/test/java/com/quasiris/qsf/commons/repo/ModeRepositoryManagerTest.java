package com.quasiris.qsf.commons.repo;

import com.quasiris.qsf.commons.repo.config.DownloadConfig;
import com.quasiris.qsf.commons.repo.config.HttpDownloadConfig;
import com.quasiris.qsf.commons.repo.config.ModelRepositoryConfig;
import com.quasiris.qsf.commons.repo.config.ModelRepositoryConfigHolder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModeRepositoryManagerTest {

    @BeforeAll
    public static void initConfig() {
        ModelRepositoryConfig config = new ModelRepositoryConfig();
        config.setModelBasePath("/path/to/models");
        config.setUploadBaseUrl("https://upload.quasiris.de/models");

        HttpDownloadConfig httpDownloadConfig = new HttpDownloadConfig();
        httpDownloadConfig.setBaseUrl("https://models.quasiris.de/models/");

        DownloadConfig downloadConfig = new DownloadConfig();
        downloadConfig.setHttp(httpDownloadConfig);

        config.setDownloadConfig(downloadConfig);
        ModelRepositoryConfigHolder.setModelRepositoryConfig(config);
    }

    @Disabled // Ignore because method fails on windows
    @Test
    public void parsePathFromModelname() {
        assertEquals("com/quasiris/qsc/embedding/2.0.1/embedding-2.0.1", ModelRepositoryManager.resolvePath("com.quasiris.qsc|embedding|2.0.1"));
        assertEquals("/mnt/models/com/quasiris/qsc/embedding/2.0.1/embedding-2.0.1/model/german-stopwords.txt", ModelRepositoryManager.resolvePath("com.quasiris.qsc|embedding|2.0.1", "/mnt/models/com.quasiris.qsc|embedding|2.0.1/model/german-stopwords.txt"));
        assertEquals("/mnt/models/com/quasiris/embedding-2020-test/2.0.1/embedding-2020-test-2.0.1/model/german-stopwords.txt", ModelRepositoryManager.resolvePath("com.quasiris|embedding-2020-test|2.0.1", "/mnt/models/com.quasiris|embedding-2020-test|2.0.1/model/german-stopwords.txt"));
    }

    @Test
    public void testPathNamesAndUrl() {

        ModelRepositoryManager modelRepositoryManager = ModelRepositoryManager.Builder.create().
                useDefaultConfig().
                groupId("com.quasiris.qsf").
                artifactId("test-model").
                version("1.2.3").
                build();

        assertEquals("com/quasiris/qsf/test-model/1.2.3/", modelRepositoryManager.getUrlPath());
        assertEquals("com/quasiris/qsf/test-model/1.2.3/test-model-1.2.3.zip", modelRepositoryManager.getUrlZipFile());
        assertEquals("https://models.quasiris.de/models/", modelRepositoryManager.getConfig().getDownloadConfig().getHttp().getBaseUrl());
        assertEquals("/path/to/models/", modelRepositoryManager.getConfig().getModelBasePath());
        assertEquals("/path/to/models/com/quasiris/qsf/test-model/1.2.3/", modelRepositoryManager.getAbsoluteModelPath());
        assertEquals("/path/to/models/com/quasiris/qsf/test-model/1.2.3/test-model-1.2.3/", modelRepositoryManager.getAbsoluteModelFile());
        assertEquals("/path/to/models/com/quasiris/qsf/test-model/1.2.3/test-model-1.2.3/my-model.bin", modelRepositoryManager.getAbsoluteModelFile("my-model.bin"));
        //assertEquals("https://models.quasiris.de/models/com/quasiris/qsf/test-model/1.2.3/test-model-1.2.3.zip", modelRepositoryManager.getModelUrl());
        assertEquals("/path/to/models/com/quasiris/qsf/test-model/1.2.3/test-model-1.2.3.zip", modelRepositoryManager.getZipFile());
        assertEquals("https://upload.quasiris.de/models/", modelRepositoryManager.getConfig().getUploadBaseUrl());
        assertEquals("https://upload.quasiris.de/models/com.quasiris.qsf/test-model/1.2.3", modelRepositoryManager.getUploadUrl());
    }


    @Test
    public void testShortId() {

        ModelRepositoryManager modelRepositoryManager = ModelRepositoryManager.Builder.create().
               shortId("com.quasiris.qsf|test-model|1.2.3").
                build();

        assertEquals("com.quasiris.qsf", modelRepositoryManager.getGroupId());
        assertEquals("test-model", modelRepositoryManager.getArtifactId());
        assertEquals("1.2.3", modelRepositoryManager.getVersion());
    }

    @Test
    public void testShortIdWithFilename() {

        ModelRepositoryManager modelRepositoryManager = ModelRepositoryManager.Builder.create().
                shortId("com.quasiris.qsf|test-model|1.2.3|test-model.bin").
                build();

        assertEquals("com.quasiris.qsf", modelRepositoryManager.getGroupId());
        assertEquals("test-model", modelRepositoryManager.getArtifactId());
        assertEquals("1.2.3", modelRepositoryManager.getVersion());
    }
}