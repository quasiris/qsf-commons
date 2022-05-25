package com.quasiris.qsf.commons.ai.embedding;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasiris.qsf.commons.ai.dto.Document;
import com.quasiris.qsf.commons.ai.dto.TextVector;
import com.quasiris.qsf.commons.ai.dto.TextVectorDocument;
import com.quasiris.qsf.commons.exception.NormalizerNotSupportedException;
import com.quasiris.qsf.commons.nlp.SentenceSplitter;
import com.quasiris.qsf.commons.text.TextSplitter;
import com.quasiris.qsf.commons.text.normalizer.TextNormalizerService;
import com.quasiris.qsf.commons.util.EmbeddingUtil;
import com.quasiris.qsf.commons.util.JsonUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public UniversalSentenceEncoder(String baseUrl, Integer timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
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
                    request.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
                    RequestConfig.Builder requestConfig = RequestConfig.custom();
                    requestConfig.setConnectTimeout(Timeout.ofMilliseconds(timeout));
                    requestConfig.setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout));
                    requestConfig.setResponseTimeout(Timeout.ofMilliseconds(timeout));
                    request.setConfig(requestConfig.build());
                    try (CloseableHttpClient httpClient = HttpClients.createDefault();
                         CloseableHttpResponse response = httpClient.execute(request)) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null && response.getCode() == HttpStatus.SC_OK) {
                            String jsonResult = EntityUtils.toString(entity);
                            Map<String, Object> responseBody = JsonUtil.defaultMapper().readValue(jsonResult, Map.class);
                            if (responseBody.containsKey("outputs")) {
                                List<Object> outputs = (List<Object>) responseBody.get("outputs");
                                if (outputs.size() == 1) {
                                    List<Double> firstVector = (List<Double>) outputs.get(0);
                                    vector = firstVector.stream().toArray(Double[]::new);
                                }
                            }
                        }
                    } catch (IOException | ParseException e) {
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

    @Override
    public TextVectorDocument embed(Document<String> doc, TextNormalizerService normalizer, boolean autosplit) throws NormalizerNotSupportedException {
        throw new NotImplementedException("This method supported yet!");
    }

    @Override
    public TextVectorDocument embedDoc(Document<List<String>> doc, TextNormalizerService normalizer) throws NormalizerNotSupportedException {
        throw new NotImplementedException("This method not supported yet!");
    }

    @Override
    public List<TextVectorDocument> embedBulk(List<Document<String>> docs, TextNormalizerService normalizer, boolean autosplit) throws NormalizerNotSupportedException {
        throw new NotImplementedException("This method supported yet!");
    }

    private HttpEntity buildEntity(String text) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        HashMap<String, List<String>> inputMap = new HashMap<>();
        inputMap.put("text", Arrays.asList(text));
        body.put("inputs", inputMap);
        String payload = JsonUtil.defaultMapper().writeValueAsString(body);
        return new StringEntity(payload, StandardCharsets.UTF_8);
    }
}
