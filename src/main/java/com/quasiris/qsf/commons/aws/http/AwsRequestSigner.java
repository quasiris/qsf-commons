package com.quasiris.qsf.commons.aws.http;

import com.quasiris.qsf.dto.http.aws.AwsCredentialsValue;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;


/*
* Examples:
*
*     @Test
    public void getLargeFile() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://qsc-api-gateway-bucket.s3.eu-central-1.amazonaws.com/somefolder/embedding-2.0.7.zip";
        URI uri = new URI(url);
        Map<String, String> signedHeaders = AwsRequestSigner.signRequest(
                uri,
                "GET",
                "",
                System.getenv("ACCESS_KEY"),
                System.getenv("SECRET_ACCESS_KEY"),
                "s3",
                "eu-central-1"
        );

        File file = restTemplate.execute(uri, HttpMethod.GET, r -> {
            for (Map.Entry<String, String> entry : signedHeaders.entrySet()) {
                r.getHeaders().set(entry.getKey(), entry.getValue());
            }
        }, clientHttpResponse -> {
            File ret = new File("returnvalue.zip");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });

    }
*
*
* Put file works for files up to 5 Gib
*    @Test
    public void putLargeFile() throws URISyntaxException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://qsc-api-gateway-bucket.s3.eu-central-1.amazonaws.com/someotherfolder/embedding-2.0.7.zip");
        File file = new File("returnvalue.zip");
        try (FileInputStream isForHash = new FileInputStream(file)) {
            Map<String, String> signedHeaders = AwsRequestSigner.signRequest(
                    uri,
                    "PUT",
                    isForHash,
                    System.getenv("ACCESS_KEY"),
                    System.getenv("SECRET_ACCESS_KEY"),
                    "s3",
                    "eu-central-1"
            );
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setBufferRequestBody(false);
            restTemplate.setRequestFactory(requestFactory);
            HttpEntity<FileSystemResource> httpEntity = new HttpEntity<>(
                    new FileSystemResource(file),
                    new HttpHeaders() {{
                        for (Entry<String, String> entry : signedHeaders.entrySet()) {
                            add(entry.getKey(), entry.getValue());
                        }
                    }});

            ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
            System.out.println("exchange.getHeaders() = " + exchange.getHeaders());
            System.out.println("exchange.getStatusCode() = " + exchange.getStatusCode());
        }
    }
*
*
*    @Test
    public void deleteTest() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://qsc-prometheus-bucket.s3.amazonaws.com/01GE5QMQH6FEAQVEJ279S6RT1T/meta.json";
        URI uri = new URI(url);
        Map<String, String> signedHeaders = AwsRequestSigner.signRequest(
                uri,
                "DELETE",
                "",
                System.getenv("ACCESS_KEY"),
                System.getenv("SECRET_ACCESS_KEY"),
                "s3",
                "eu-central-1"
        );
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, new HttpHeaders() {{
            for (Entry<String, String> entry : signedHeaders.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
        }});
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);
        System.out.println("exchange.getBody() = " + exchange.getBody());
        System.out.println("exchange.getStatusCode() = " + exchange.getStatusCode());
        System.out.println("exchange.getStatusCodeValue() = " + exchange.getStatusCodeValue());
    }
*
*
* */
public class AwsRequestSigner {

    private static final HmacAlgorithms HMAC_ALGORITHM = HmacAlgorithms.HMAC_SHA_256;
    public static final DateTimeFormatter amzFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    public static final DateTimeFormatter dateStampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String algorithm = "AWS4-HMAC-SHA256";

    public static Map<String, String> signS3Request(URI uri,
                                                    String method,
                                                    AwsCredentialsValue credentials,
                                                    String region) {
        return signRequest(uri, method, "", credentials, "s3", region);
    }

    public static Map<String, String> signRequest(URI uri,
                                                  String method,
                                                  AwsCredentialsValue credentials,
                                                  String service,
                                                  String region
    ) {
        return signRequest(uri, method, "", credentials, service, region);
    }

    public static Map<String, String> signRequest(URI uri,
                                                  String method,
                                                  String payload,
                                                  AwsCredentialsValue credentials,
                                                  String service,
                                                  String region) {
        String payloadHash = DigestUtils.sha256Hex(Objects.requireNonNullElse(payload, "").getBytes(StandardCharsets.UTF_8));
        return executeSignRequest(uri, method, credentials, service, region, payloadHash);
    }

    public static Map<String, String> signRequest(URI uri,
                                                  String method,
                                                  InputStream payload,
                                                  AwsCredentialsValue credentials,
                                                  String service,
                                                  String region) throws IOException {
        String payloadHash = getPayloadHash(payload);
        return executeSignRequest(uri, method, credentials, service, region, payloadHash);
    }

    public static String getPayloadHash(InputStream payload) throws IOException {
        return DigestUtils.sha256Hex(payload);
    }

    public static Map<String, String> executeSignRequest(URI uri, String method, AwsCredentialsValue credentials, String service, String region, String payloadHash) {
        return executeSignRequest(uri, method, credentials, service, region, payloadHash, new LinkedHashMap<>());
    }

    public static TreeMap<String, String> executeSignRequest(URI uri, String method, AwsCredentialsValue credentials, String service, String region, String payloadHash, Map<String, String> additionalHeaders) {
        String host = uri.getHost();
        String canonicalUri = Objects.requireNonNullElse(uri.getRawPath(), "/");
        String canonicalQueryString = Objects.requireNonNullElse(uri.getRawQuery(), "");

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        String amzDate = now.format(amzFormatter);
        String dateStamp = now.format(dateStampFormatter);
        Map<String, String> headersToSign = new LinkedHashMap<>(additionalHeaders);
        headersToSign.put("host", host);
        headersToSign.put("x-amz-date", amzDate);
        if (credentials.getSessionToken() != null) {
            headersToSign.put("x-amz-security-token", credentials.getSessionToken());
        }
        String canonicalHeaders = headersToSign.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .sorted()
                .reduce((f, s) -> f + "\n" + s).orElse("") + "\n";
        String signedHeaders = headersToSign.keySet().stream().reduce((f, s) -> f + ";" + s).orElse("");
        String canonicalRequest = method + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + payloadHash;
        String credentialScope = dateStamp + "/" + region + "/" + service + "/" + "aws4_request";
        String stringToSign = algorithm + "\n" + amzDate + "\n" + credentialScope + "\n" + DigestUtils.sha256Hex(canonicalRequest);
        byte[] signingKey = getSignatureKey(credentials.getSecretAccessKey(), dateStamp, region, service);
        String signature = new HmacUtils(HMAC_ALGORITHM, signingKey).hmacHex(stringToSign);
        String authorizationHeader = algorithm + " " + "Credential=" + credentials.getAccessKeyId() + "/" + credentialScope + ", " + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;

        return new TreeMap<>() {{
            put("Authorization", authorizationHeader);
            put("x-amz-content-sha256", payloadHash);
            put("x-amz-date", amzDate);
            if (credentials.getSessionToken() != null) {
                put("x-amz-security-token", credentials.getSessionToken());
            }
            putAll(additionalHeaders);
        }};
    }

    private static byte[] getSignatureKey(String awsSecretAccessKey, String dateStamp, String region, String service) {
        byte[] kDate = signRequest(("AWS4" + awsSecretAccessKey).getBytes(StandardCharsets.UTF_8), dateStamp);
        byte[] kRegion = signRequest(kDate, region);
        byte[] kService = signRequest(kRegion, service);
        return signRequest(kService, "aws4_request");
    }

    private static byte[] signRequest(byte[] key, String msg) {
        return new HmacUtils(HMAC_ALGORITHM, key).hmac(msg);
    }

}
