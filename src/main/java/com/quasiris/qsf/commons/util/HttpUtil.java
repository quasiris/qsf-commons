package com.quasiris.qsf.commons.util;

import com.quasiris.qsf.dto.common.HttpRequestDTO;

import java.util.Base64;

public class HttpUtil {
    public static String createBasicAuthHeader(String username, String password) {
        String headerValue = createBasicAuthHeaderValue(username, password);
        return "Authorization: " + headerValue;
    }

    public static String createBasicAuthHeaderValue(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    public static String createBearerTokenHeader(String token) {
        return "Authorization: Bearer "+ token;
    }

    public static String buildUrl(HttpRequestDTO requestDTO) {
        String url = requestDTO.getUrl();
        String queryString = UrlUtil.buildQuerystring(requestDTO.getParams());
        url = UrlUtil.appendQuerystring(url, queryString);
        return url;
    }
}
