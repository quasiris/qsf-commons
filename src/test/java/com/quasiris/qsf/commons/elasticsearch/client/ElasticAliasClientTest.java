package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.http.HttpResponse;
import com.quasiris.qsf.dto.elasticsearch.AliasAction;
import com.quasiris.qsf.dto.elasticsearch.AliasActionAdd;
import com.quasiris.qsf.dto.elasticsearch.AliasActionRemove;
import com.quasiris.qsf.dto.elasticsearch.AliasActions;
import com.quasiris.qsf.dto.elasticsearch.IndexWithAliases;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class ElasticAliasClientTest {
    ElasticIndexClient indexClient = new ElasticIndexClient();
    ElasticAliasClient aliasClient = new ElasticAliasClient();
    String baseUrl = "http://localhost:9200";
    String index = "junit-index";
    String alias = "junit-alias";

    @AfterEach
    void tearDown() {
        indexClient.remove(baseUrl, index);
        aliasClient.remove(baseUrl, index, alias);
    }

    AliasActionAdd createJunitIndexAliasActionAdd() {
        AliasActionAdd action = new AliasActionAdd();
        action.setIndices("junit-index");
        action.setAliases("junit-alias");
        return action;
    }

    AliasActionRemove createJunitIndexAliasActionRemove() {
        AliasActionRemove action = new AliasActionRemove();
        action.setIndices("junit-index");
        action.setAliases("junit-alias");
        return action;
    }

    HttpResponse<Map> createJunitIndexAlias(String elasticBaseUrl) {
        AliasActions actions = new AliasActions();
        AliasAction action = new AliasAction();
        AliasActionAdd actionConfig = createJunitIndexAliasActionAdd();
        action.setAdd(actionConfig);
        actions.getActions().add(action);

        return aliasClient.performActions(elasticBaseUrl, actions);
    }

    HttpResponse<Map> removeJunitIndexAlias(String elasticBaseUrl) {
        AliasActions actions = new AliasActions();
        AliasAction action = new AliasAction();
        AliasActionRemove actionConfig = createJunitIndexAliasActionRemove();
        action.setRemove(actionConfig);
        actions.getActions().add(action);

        return aliasClient.performActions(elasticBaseUrl, actions);
    }

    @Test
    void performActions_success() {
        // given
        indexClient.create(baseUrl, index);

        // when
        HttpResponse<Map> response = createJunitIndexAlias(baseUrl);

        // then
        assertTrue(response.is2xx());
    }

    @Test
    void performActions_testCreateAliasForNonExistingIndex() {
        // when
        HttpResponse<Map> response = createJunitIndexAlias(baseUrl);

        // then
        assertTrue(response.getPayload().toString().contains("index_not_found_exception"));
    }

    @Test
    void create_testCreateAliasForExistingAlias() {
        // given
        indexClient.create(baseUrl, index);
        aliasClient.create(baseUrl, index, alias);

        // when
        aliasClient.create(baseUrl, index, alias);
    }

    @Test
    void remove_success() {
        // given
        indexClient.create(baseUrl, index);
        aliasClient.create(baseUrl, index, alias);

         // when
        aliasClient.remove(baseUrl, index, alias);
    }

    @Test
    void remove_nonExistingAlias() {
        // given
        indexClient.create(baseUrl, index);

        // when
        HttpResponse<Map> response = aliasClient.remove(baseUrl, index, alias);

        // then
        assertEquals(404, response.getStatusCode());
        assertTrue(response.getPayload().toString().contains("aliases_not_found_exception"));
    }

    @Test
    void remove_nonExistingIndex() {
        // when
        HttpResponse<Map> response = aliasClient.remove(baseUrl, index, alias);

        // then
        assertTrue(response.getPayload().toString().contains("index_not_found_exception"));
    }

    @Test
    void exists_success() {
        // given
        indexClient.create(baseUrl, index);

        // when
        aliasClient.create(baseUrl, index, alias);
        boolean exists = aliasClient.exists(baseUrl, alias);

        // then
        assertTrue(exists);
    }

    @Test
    void exists_notExist() {
        // when
        boolean exists = aliasClient.exists(baseUrl, alias);

        // then
        assertFalse(exists);
    }

    @Test
    void exists_butIndex() {
        // given
        indexClient.create(baseUrl, index);

        // when
        boolean exists = aliasClient.exists(baseUrl, index);

        // cleanup
        indexClient.remove(baseUrl, index);

        // then
        assertFalse(exists);
    }

    @Test
    void exists_notCluster() {
        // given
        String elasticBaseUrlNonExisting = this.baseUrl +"nonexisting";

        // when
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> aliasClient.exists(elasticBaseUrlNonExisting, index)
        );
    }

    @Test
    void getIndicesWithAliasesNoAliasExists() {
        indexClient.create(baseUrl, index);
        List<IndexWithAliases> response = aliasClient.getIndicesWithAliases(this.baseUrl, null);
        IndexWithAliases jUnitIndex = response.stream().filter(i -> i.getIndexName().equals("junit-index")).findFirst().orElse(null);
        assertNotNull(jUnitIndex);
        assertEquals(0, jUnitIndex.getAliases().size());
    }
    @Test
    void getIndicesWithAliasesAliasExists() {
        indexClient.create(baseUrl, index);
        aliasClient.create(baseUrl, index, alias);
        List<IndexWithAliases> response = aliasClient.getIndicesWithAliases(this.baseUrl, null);
        IndexWithAliases jUnitIndex = response.stream().filter(i -> i.getIndexName().equals("junit-index")).findFirst().orElse(null);
        assertNotNull(jUnitIndex);
        assertEquals(1, jUnitIndex.getAliases().size());
        assertEquals("junit-alias", jUnitIndex.getAliases().get(0));
    }


    @Test
    void getIndicesWithAliasesMultipleAliasExists() {
        indexClient.create(baseUrl, index);
        aliasClient.create(baseUrl, index, alias);
        aliasClient.create(baseUrl, index, "junit-alias-2");
        List<IndexWithAliases> response = aliasClient.getIndicesWithAliases(this.baseUrl, null);
        IndexWithAliases jUnitIndex = response.stream().filter(i -> i.getIndexName().equals("junit-index")).findFirst().orElse(null);
        assertNotNull(jUnitIndex);
        assertEquals(2, jUnitIndex.getAliases().size());
        assertEquals("junit-alias", jUnitIndex.getAliases().get(0));
        assertEquals("junit-alias-2", jUnitIndex.getAliases().get(1));
    }

    @Test
    void getIndicesWithAliasesWithFilterMatches() {
        indexClient.create(baseUrl, index);
        aliasClient.create(baseUrl, index, alias);
        aliasClient.create(baseUrl, index, "junit-alias-2");
        List<IndexWithAliases> response = aliasClient.getIndicesWithAliases(this.baseUrl, "j*");
        IndexWithAliases jUnitIndex = response.stream().filter(i -> i.getIndexName().equals("junit-index")).findFirst().orElse(null);
        assertNotNull(jUnitIndex);
        assertEquals(2, jUnitIndex.getAliases().size());
        assertEquals("junit-alias", jUnitIndex.getAliases().get(0));
        assertEquals("junit-alias-2", jUnitIndex.getAliases().get(1));
    }

    @Test
    void getIndicesWithAliasesWithFilterNotMatches() {
        indexClient.create(baseUrl, index);
        aliasClient.create(baseUrl, index, alias);
        aliasClient.create(baseUrl, index, "junit-alias-2");
        List<IndexWithAliases> response = aliasClient.getIndicesWithAliases(this.baseUrl, "t*");
        IndexWithAliases jUnitIndex = response.stream().filter(i -> i.getIndexName().equals("junit-index")).findFirst().orElse(null);
        assertNull(jUnitIndex);
    }
}