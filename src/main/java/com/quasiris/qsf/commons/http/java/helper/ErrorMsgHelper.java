package com.quasiris.qsf.commons.http.java.helper;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;

public class ErrorMsgHelper {

    public static final int BYTES_TO_STRING_BODY_LIMIT = 1024;

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
        Object respBodyMsg = httpMetadata.getResponse().getBody();
        if (respBodyMsg instanceof byte[]) {
            byte[] bytes = (byte[]) respBodyMsg;
            int length = Math.min(BYTES_TO_STRING_BODY_LIMIT, bytes.length);
            respBodyMsg = new String(bytes, 0, length);
        }
        return new StringBuilder()
                .append("statusCode: ").append(httpMetadata.getResponse().getStatusCode()).append("\n")
                .append("retries: ").append(httpMetadata.getRetries()).append("\n")
                .append("responseBody: \n-----BEGIN RESPONSE BODY-----\n")
                .append(respBodyMsg)
                .append("\n-----END RESPONSE BODY-----\n");
    }

    public static StringBuilder createRequestData(HttpMetadata httpMetadata) {
        return new StringBuilder()
                .append("requestUri: ").append(httpMetadata.getRequest().getUri()).append("\n")
                .append("method: ").append(httpMetadata.getRequest().getMethod()).append("\n")
                .append("requestBody: \n-----BEGIN REQUEST BODY-----\n")
                .append(httpMetadata.getRequest().getBody())
                .append("\n-----END REQUEST BODY-----\n");
    }
}
