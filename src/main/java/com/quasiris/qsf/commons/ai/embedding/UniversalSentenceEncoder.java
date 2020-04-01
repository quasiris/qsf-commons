package com.quasiris.qsf.commons.ai.embedding;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasiris.qsf.commons.ai.dto.TextVector;
import com.quasiris.qsf.commons.nlp.SentenceSplitter;
import com.quasiris.qsf.commons.text.TextSplitter;
import com.quasiris.qsf.commons.text.normalizer.TextNormalizerService;
import com.quasiris.qsf.commons.util.EmbeddingUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.*;

/**
 * UniversalSentenceEncoder from TensorFlow Hub
 * Version: 2
 * Language: English
 * Network: DAN
 * Publisher: Google
 * Module type: text-embedding
 * Link: https://tfhub.dev/google/universal-sentence-encoder/2
 */
public class UniversalSentenceEncoder implements TextEmbeddingEncoder {
    private static final Logger logger = LoggerFactory.getLogger(UniversalSentenceEncoder.class);

    private String baseUrl;
    private Integer timeout;
    private ObjectMapper objectMapper;

    public UniversalSentenceEncoder(String baseUrl, Integer timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<TextVector> embed(String text, TextNormalizerService normalizer, boolean autosplit) {
        TextSplitter textSplitter = new SentenceSplitter();
        List<String> sentences = autosplit ? textSplitter.split(text) : Arrays.asList(text);

        List<TextVector> textVectors = new ArrayList<>();
        for(String sentence : sentences) {
            String normalized = normalizer != null ? normalizer.normalize(sentence) : sentence;
            Double[] vector = null;
            if(StringUtils.isNotBlank(normalized)) {
                try {
                    HttpEntity requestEntity = buildEntity(normalized);
                    HttpPost request = new HttpPost(baseUrl);
                    request.setEntity(requestEntity);
                    request.addHeader("Content-Type", "application/json");
                    RequestConfig.Builder requestConfig = RequestConfig.custom();
                    requestConfig.setConnectTimeout(timeout);
                    requestConfig.setConnectionRequestTimeout(timeout);
                    requestConfig.setSocketTimeout(timeout);
                    request.setConfig(requestConfig.build());
                    try (CloseableHttpClient httpClient = HttpClients.createDefault();
                         CloseableHttpResponse response = httpClient.execute(request)) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            String jsonResult = EntityUtils.toString(entity);
                            Map<String, Object> responseBody = objectMapper.readValue(jsonResult, Map.class);
                            if (responseBody.containsKey("outputs")) {
                                List<Object> outputs = (List<Object>) responseBody.get("outputs");
                                if (outputs.size() == 1) {
                                    List<Double> firstVector = (List<Double>) outputs.get(0);
                                    vector = firstVector.stream().toArray(Double[]::new);
                                }
                            }
                        }
                    } catch (IOException e) {
                        logger.warn("Something gone wrong in GET document for UniversalSentenceEncoder!", e);
                    }
                } catch (JsonProcessingException e) {
                    logger.warn(e.getMessage());
                }
            }
            if(EmbeddingUtil.isZeroVector(vector)) {
                vector = null;
            }
            textVectors.add(new TextVector(sentence, null, vector));
        }

        return textVectors;
    }

    private HttpEntity buildEntity(String text) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        HashMap<String, List<String>> inputMap = new HashMap<>();
        inputMap.put("text", Arrays.asList(text));
        body.put("inputs", inputMap);
        String payload = objectMapper.writeValueAsString(body);
        return new StringEntity(payload, "UTF-8");
    }
}
