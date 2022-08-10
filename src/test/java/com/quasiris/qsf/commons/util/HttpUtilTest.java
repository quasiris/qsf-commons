package com.quasiris.qsf.commons.util;

import com.quasiris.qsf.dto.common.HttpRequestDTO;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

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

    @Test
    void extractUsernamePassword() throws MalformedURLException {
        String urlWithUsernamePassword = "https://admin:B%C3%A4r@example.com/search";
        URL url = new URL(urlWithUsernamePassword);
        String userInfo = url.getUserInfo();
        String userInfoDecoded = UrlUtil.decode(userInfo);
        assertEquals("admin:Bär", userInfoDecoded);
    }

    @Test
    void buildUrl() {
        // given
        HttpRequestDTO requestDTO = new HttpRequestDTO();
        requestDTO.setUrl("https://example.com/search");
        requestDTO.getParams().put("q", "bär");
        requestDTO.getParams().put("filter", "price:[10,100]");
        requestDTO.getParams().put("filter", "color:blue");
        requestDTO.getParams().put("filter", "onStock:true");
        requestDTO.getParams().put("sort", "price:desc");
        requestDTO.getParams().put("gerät", "smartphone");

        // when
        String url = HttpUtil.buildUrl(requestDTO);

        // then
        assertEquals("https://example.com/search?filter=price%3A%5B10%2C100%5D&filter=color%3Ablue&filter=onStock%3Atrue&q=b%C3%A4r&sort=price%3Adesc&ger%C3%A4t=smartphone", url);
    }
}