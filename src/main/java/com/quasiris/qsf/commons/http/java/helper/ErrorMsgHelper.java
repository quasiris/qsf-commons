package com.quasiris.qsf.commons.http.java.helper;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;
import com.quasiris.qsf.commons.http.java.model.multipart.MultipartUploadRequest;
import com.quasiris.qsf.commons.util.JsonUtil;

import java.io.File;
import java.io.InputStream;

public class ErrorMsgHelper {

    public static final int STRING_BODY_LIMIT = 1024;

    public static String createErrMsg(HttpMetadata httpMetadata) {
        StringBuilder requestData = createRequestData(httpMetadata);
        StringBuilder result = new StringBuilder().append("REQUEST:\n").append(requestData);
        result.append("RESPONSE:");
        if (httpMetadata.getResponse().getStatusCode() != null) {
            StringBuilder responseData = createResponseData(httpMetadata);
            result.append("\n").append(responseData);
        } else {
            result.append(" absent");
        }
        return result.toString();
    }

    public static StringBuilder createResponseData(HttpMetadata httpMetadata) {
        String dataType = calcDataType(httpMetadata.getResponse().getBody());
        String contentRepresentation = calcContentRepresentation(httpMetadata.getResponse().getBody(), false);
        return new StringBuilder()
                .append("statusCode: ").append(httpMetadata.getResponse().getStatusCode()).append("\n")
                .append("retries: ").append(httpMetadata.getRetries()).append("\n")
                .append("responseBodyDataType: ").append(dataType).append("\n")
                .append("responseBody: \n-----BEGIN RESPONSE BODY-----\n")
                .append(contentRepresentation)
                .append("\n-----END RESPONSE BODY-----\n");
    }

    public static StringBuilder createRequestData(HttpMetadata httpMetadata) {
        Object body = httpMetadata.getRequest().getBody();
        String dataType = calcDataType(body);
        String contentRepresentation = calcContentRepresentation(body, true);
        return new StringBuilder()
                .append("requestUri: ").append(httpMetadata.getRequest().getUri()).append("\n")
                .append("method: ").append(httpMetadata.getRequest().getMethod()).append("\n")
                .append("requestBodyDataType: ").append(dataType).append("\n")
                .append("requestBody: \n-----BEGIN REQUEST BODY-----\n")
                .append(contentRepresentation)
                .append("\n-----END REQUEST BODY-----\n");
    }

    private static String calcContentRepresentation(Object body, boolean displayObjBody) {
        if (body == null) {
            return null;
        }
        if (body instanceof byte[]) {
            byte[] bytes = (byte[]) body;
            int length = Math.min(STRING_BODY_LIMIT, bytes.length);
            return new String(bytes, 0, length);
        } else if (body instanceof InputStream) {
            return "skip";
        } else if (body instanceof File) {
            return ((File) body).getAbsolutePath();
        } else if (body instanceof MultipartUploadRequest) {
            return JsonUtil.toJson(body);
        } else if (body instanceof String) {
            String strBody = body.toString();
            return strBody.substring(0, Math.min(STRING_BODY_LIMIT, strBody.length()));
        } else {
            String strBody;
            if (displayObjBody) {
                try {
                    strBody = JsonUtil.defaultMapper().writeValueAsString(body);
                } catch (Exception ex) {
                    return "can-not-serialize-as-json-string";
                }
            } else {
                strBody = body.toString();
            }
            return strBody.substring(0, Math.min(STRING_BODY_LIMIT, strBody.length()));
        }
    }

    private static String calcDataType(Object body) {
        if (body == null) {
            return "null";
        }
        if (body instanceof byte[]) {
            return "bytes";
        } else if (body instanceof InputStream) {
            return "inputStream";
        } else if (body instanceof File) {
            return "file";
        } else if (body instanceof MultipartUploadRequest) {
            return "multipart";
        } else {
            return "object";
        }
    }
}
