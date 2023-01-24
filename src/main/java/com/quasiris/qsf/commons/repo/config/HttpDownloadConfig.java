package com.quasiris.qsf.commons.repo.config;

import com.quasiris.qsf.commons.util.IOUtils;

public class HttpDownloadConfig {
    private String baseUrl;
    private BasicAuth basic;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = IOUtils.ensureEndingSlash(baseUrl);
    }

    public BasicAuth getBasic() {
        return basic;
    }

    public void setBasic(BasicAuth basic) {
        this.basic = basic;
    }
}
