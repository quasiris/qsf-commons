package com.quasiris.qsf.commons.util;

import java.util.Base64;

public class HttpUtil {
    public static String createBasicAuthHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Authorization: Basic "+ Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    public static String createBearerTokenHeader(String token) {
        return "Authorization: Bearer "+ token;
    }
}
