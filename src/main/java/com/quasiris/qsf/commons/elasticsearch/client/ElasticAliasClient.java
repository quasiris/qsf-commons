package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.HttpResponse;
import com.quasiris.qsf.dto.elasticsearch.AliasActions;
import com.quasiris.qsf.dto.elasticsearch.IndexWithAliases;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.quasiris.qsf.commons.util.GenericUtils.castTypeReference;


/**
 * Elastic client that access some Aliases APIs
 * ref:
 * - https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-aliases.html
 * - https://www.elastic.co/guide/en/elasticsearch/reference/current/aliases.html
 */
public class ElasticAliasClient extends ElasticBaseClient {


    /**
     * Perform multiple alias actions as one transaction
     * ref:
     * - https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-aliases.html
     * - https://www.elastic.co/guide/en/elasticsearch/reference/current/aliases.html
     * @param baseUrl of elastic cluster
     * @param actions to perform
     * @return response object
     */
    public HttpResponse<Map> performActions(String baseUrl, AliasActions actions) {
        // POST _aliases
        String apiUrl = baseUrl+"/_aliases";
        return getRestClient().postForResponse(apiUrl, actions, castTypeReference(Map.class));
    }

    /**
     * Create alias for index or datastream
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-add-alias.html
     * @param baseUrl of elastic cluster
     * @param index name. Wildcards supported
     * @param alias name
     * @return response object
     */
    public HttpResponse<Map> create(String baseUrl, String index, String alias) {
        String apiUrl = baseUrl+"/"+index+"/_alias/"+alias;
        return getRestClient().putForResponse(apiUrl, null, castTypeReference(Map.class));
    }

    /**
     * Remove alias from index
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-delete-alias.html
     * @param baseUrl of elastic cluster
     * @param index name. Wildcards supported. To include all indices in the cluster, use _all or *
     * @param alias name
     * @return response object
     */
    public HttpResponse<Map> remove(String baseUrl, String index, String alias) {
        String apiUrl = baseUrl+"/"+index+"/_alias/"+alias;
        return getRestClient().deleteForResponse(apiUrl, castTypeReference(Map.class));
    }

    /**
     * Check if Alias exists
     * ref: https://www.elastic.co/guide/en/elasticsearch/reference/7.10/indices-alias-exists.html
     * @param baseUrl of elastic cluster
     * @param alias name
     * @return response object
     */
    public boolean exists(String baseUrl, String alias) {
        String apiUrl = baseUrl+"/_alias/"+alias;
        HttpResponse httpResponse = getRestClient().headForResponse(apiUrl, null);
        return httpResponse.is2xx();
    }

    /**
     * Get all indexes with aliases.
     *
     * @param baseUrl the baseUrl of the index.
     * @return a map of indexes with aliases
     */
    public HttpResponse<Map> getAll(String baseUrl) {
        return getAll(baseUrl, null);
    }

    /**
     * Get all indexes with aliases.
     *
     * @param baseUrl the baseUrl of the index.
     * @param aliasFilter filter the alias names
     * @return a map of indexes with aliases
     */
    public HttpResponse<Map> getAll(String baseUrl, String aliasFilter) {
        String apiUrl = baseUrl+"/_alias";
        if(StringUtils.isNotEmpty(aliasFilter)) {
            apiUrl = apiUrl + "/" + aliasFilter;
        }
        HttpResponse httpResponse = getRestClient().getForResponse(apiUrl, castTypeReference(Map.class));
        return httpResponse;
    }

    /**
     * Get all indices with aliases.
     * @param baseUrl the baseUrl of the index.
     * @param aliasFilter filter the alias names
     * @return response object
     */
    public List<IndexWithAliases> getIndicesWithAliases(String baseUrl, String aliasFilter) {

        HttpResponse<Map> response = getAll(baseUrl, aliasFilter);
        if(!response.is2xx()) {
            throw new RuntimeException("Could not retrieve indices with aliases for baseUrl: " + baseUrl + " and filters: " + aliasFilter);
        }
        List<IndexWithAliases> indexWithAliases = parseAliasesResponse(response.getPayload());

        return indexWithAliases;
    }

    protected List<IndexWithAliases> parseAliasesResponse(Map<String, Map<String, Map>> aliasesResponse) {
        List<IndexWithAliases> indexWithAliases = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map>> stringMapEntry : aliasesResponse.entrySet()) {
            String index = stringMapEntry.getKey();
            Map<String, Object> aliases = (Map<String, Object>) stringMapEntry.getValue().get("aliases");
            List<String> aliasList = new ArrayList(aliases.keySet());
            IndexWithAliases aliasResponse = new IndexWithAliases();
            aliasResponse.setIndexName(index);
            aliasResponse.setAliases(aliasList);
            indexWithAliases.add(aliasResponse);
        }

        return indexWithAliases;
    }

    /**
     * Find aliases for a given index
     * @param baseUrl of elastic cluster
     * @param index name. Wildcards supported
     * @return list with responses
     */
    public List<IndexWithAliases> findAliasesForIndex(String baseUrl, String index) {
        String apiUrl = baseUrl+"/"+index+"/_alias";
        HttpResponse<Map> httpResponse = getRestClient().getForResponse(apiUrl, castTypeReference(Map.class));
        List<IndexWithAliases> indexWithAliases = new ArrayList<>();
        if(httpResponse.is2xx()) {
            indexWithAliases = parseAliasesResponse(httpResponse.getPayload());
        }else if(httpResponse.getStatusCode() == 404) {
            // alias not found return empty list
        } else {
            throw new RuntimeException("Could not retrieve indices with aliases for baseUrl: "+baseUrl+" and index pattern: "+index);
        }
        return indexWithAliases;
    }
}
