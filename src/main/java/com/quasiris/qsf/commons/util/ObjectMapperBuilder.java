package com.quasiris.qsf.commons.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Deprecated // use JsonUtil
public class ObjectMapperBuilder {

    private static final ObjectMapper defaultMapper;

    static {
        defaultMapper = new ObjectMapper();
        defaultMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        defaultMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static ObjectMapper defaultMapper() {
        return defaultMapper;
    }
}
