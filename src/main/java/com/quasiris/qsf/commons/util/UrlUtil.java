package com.quasiris.qsf.commons.util;

import com.quasiris.qsf.dto.common.MultiMap;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlUtil {

    public static String removePassword(String url) {
        return url.replaceAll("(.*)://(.*)@(.*)", "$1://$3");
    }


    public static String replacePath(String url, String newPath) {
        try {
            URI originalUri = new URI(url);

            if (newPath == null) {
                newPath = "";
            }

            return url.replaceAll(originalUri.getPath(), newPath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



    public static Map<String, Object> encode(Map<String, Object> values, String suffix) {
        Map<String, Object> ret = new HashMap<>();

        for(Map.Entry<String, Object> entry : values.entrySet()) {
            Object encoded = entry.getValue();
            if(entry.getValue() instanceof String) {
                encoded = encode((String) entry.getValue());
            }
            if(suffix != null) {
                ret.put(entry.getKey(), entry.getValue());
                ret.put(entry.getKey() + suffix, encoded);
            } else {
                ret.put(entry.getKey(), encoded);
            }

        }
        return ret;
    }

    public static Map<String, Object> encode(Map<String, Object> values) {
        return encode(values, null);
    }

    public static String encode(String value) {
        try {
            String encoded = URLEncoder.encode(value, "UTF-8");
            encoded = encoded.replace("+", "%20");
            return encoded;
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public static String buildQuerystring(MultiMap<String, Object> params) {
        String querystring = "";
        if(params != null && params.entrySet() != null) {
            for (Map.Entry<String, List<Object>> entry : params.entrySet()) {
                String key = UrlUtil.encode(entry.getKey());
                for (Object o : entry.getValue()) {
                    String value = UrlUtil.encode(o.toString());
                    querystring += key+"="+value+"&";
                }
            }
        }
        if(querystring.endsWith("&")) {
            querystring = querystring.substring(0, querystring.length() - 1);
        }
        return querystring;
    }

    public static String appendQuerystring(String url, String querystring) {
        if(url.contains("?")) {
            if(url.endsWith("?") || url.endsWith("&")) {
                url = url + querystring;
            } else {
                url = url + "&" + querystring;
            }
        } else {
            url = url + "?" + querystring;
        }
        return url;
    }

    public static String extractUsernamePassword(String url) throws MalformedURLException {
        URL u = new URL(url);
        String userInfo = u.getUserInfo();
        if(StringUtils.isNotEmpty(userInfo)) {
            userInfo = UrlUtil.decode(userInfo);
        }
        return userInfo;
    }

    public static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
