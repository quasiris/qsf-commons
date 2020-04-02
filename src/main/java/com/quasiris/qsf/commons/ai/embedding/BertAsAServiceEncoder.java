package com.quasiris.qsf.commons.ai.embedding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasiris.qsf.commons.ai.dto.Document;
import com.quasiris.qsf.commons.ai.dto.TextVector;
import com.quasiris.qsf.commons.ai.dto.TextVectorDocument;
import com.quasiris.qsf.commons.nlp.SentenceSplitter;
import com.quasiris.qsf.commons.text.TextSplitter;
import com.quasiris.qsf.commons.text.normalizer.TextNormalizerService;
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

import java.util.*;

/**
 * BertAsAServiceEncoder
 */
public class BertAsAServiceEncoder implements TextEmbeddingEncoder {
    private static final Logger logger = LoggerFactory.getLogger(BertAsAServiceEncoder.class);

    private static String BULK_FIELD = "_bulk";

    private String baseUrl;
    private Integer timeout;
    private ObjectMapper objectMapper;

    public BertAsAServiceEncoder(String baseUrl, Integer timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<TextVector> embed(String text, TextNormalizerService normalizer, boolean autosplit) {
        List<TextVector> textVectors = new ArrayList<>();
        List<TextVectorDocument> textVectorDocuments = embedTextBulk(Arrays.asList(text), normalizer, autosplit);
        if(textVectorDocuments.size() == 1) {
            textVectors = textVectorDocuments.get(0).getFields().get(BULK_FIELD);
        }

        return textVectors;
    }

    private List<TextVectorDocument> embedTextBulk(List<String> textList, TextNormalizerService normalizer, boolean autosplit) {
        TextSplitter textSplitter = new SentenceSplitter();
        List<String> allSentences = new ArrayList<>();
        List<TextVector> allVectors = new ArrayList<>();
        List<TextVectorDocument> vectorDocs = new ArrayList<>();
        for (String text : textList) {
            List<String> sentences = autosplit ? textSplitter.split(text) : Arrays.asList(text);
            TextVectorDocument vectorDoc = new TextVectorDocument();
            vectorDoc.getFields().put(BULK_FIELD, new ArrayList<>());
            for(String sentence : sentences) {
                String normalized = normalizer != null ? normalizer.normalize(sentence) : sentence;

                if(StringUtils.isNotBlank(normalized)) {
                    TextVector textVector = new TextVector(sentence, normalized, null);
                    vectorDoc.getFields().get(BULK_FIELD).add(textVector);

                    allVectors.add(textVector);
                    allSentences.add(normalized);
                }
            }
            vectorDocs.add(vectorDoc);
        }

        try {
            HttpEntity requestEntity = buildEntity(allSentences);
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
                    if (responseBody.containsKey("result")) {
                        List<Object> outputs = (List<Object>) responseBody.get("result");
                        if(outputs.size() != allVectors.size()) {
                            throw new Exception("Input and output vectors does not match!");
                        }
                        for (int i = 0; i < outputs.size(); i++) {
                            List<Double> vectorList = (List<Double>) outputs.get(i);
                            Double[] vector = vectorList.stream().toArray(Double[]::new);
                            allVectors.get(i).setVector(vector);
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("Something gone wrong in GET document for BertAsAServiceEncoder!", e);
            }
        } catch (JsonProcessingException e) {
            logger.warn(e.getMessage());
        }

        return vectorDocs;
    }

    @Override
    public TextVectorDocument embed(Document<String> doc, TextNormalizerService normalizer, boolean autosplit) {
        TextVectorDocument vectorDoc = new TextVectorDocument(doc.getId());
        List<TextVectorDocument> vectorDocs = embedBulk(Arrays.asList(doc), normalizer, autosplit);
        if(vectorDocs.size() == 1) {
            vectorDoc = vectorDocs.get(0);
        }
        return vectorDoc;
    }

    @Override
    public List<TextVectorDocument> embedBulk(List<Document<String>> docs, TextNormalizerService normalizer, boolean autosplit) {
        List<TextVectorDocument> vectorDocs = new ArrayList<>();
        for (Document<String> doc : docs) {
            TextVectorDocument vectorDoc = new TextVectorDocument(doc.getId());
            List<String> allSentences = new ArrayList<>();
            for (Map.Entry<String, String> entry : doc.getFields().entrySet()) {
                allSentences.add(entry.getValue());
            }

            List<TextVectorDocument> textVectorDocuments = embedTextBulk(allSentences, normalizer, autosplit);
            if(doc.getFields().values().size() == textVectorDocuments.size()) {
                int i = 0;
                for (Map.Entry<String, String> entry : doc.getFields().entrySet()) {
                    vectorDoc.getFields().put(entry.getKey(), textVectorDocuments.get(i).getFields().get(BULK_FIELD));
                    i++;
                }
            }
            vectorDocs.add(vectorDoc);
        }

        return vectorDocs;
    }

    private HttpEntity buildEntity(List<String> sentences) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("id", "");
        body.put("texts", sentences);
        body.put("is_tokenized", false);
        String payload = objectMapper.writeValueAsString(body);
        return new StringEntity(payload, "UTF-8");
    }
}
