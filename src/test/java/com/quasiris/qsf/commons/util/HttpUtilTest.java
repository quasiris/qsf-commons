package com.quasiris.qsf.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilTest {

    @Test
    void createBasicAuthHeader() {
        String header = HttpUtil.createBasicAuthHeader("john", "ns9GsZ8C1IeXUNl6dvBMx8Akmd2lce");
        assertEquals("Authorization: Basic am9objpuczlHc1o4QzFJZVhVTmw2ZHZCTXg4QWttZDJsY2U=", header);
    }

    @Test
    void createBearerTokenHeader() {
        String header = HttpUtil.createBearerTokenHeader("ns9GsZ8C1IeXUNl6dvBMx8Akmd2lce");
        assertEquals("Authorization: Bearer ns9GsZ8C1IeXUNl6dvBMx8Akmd2lce", header);
    }
}