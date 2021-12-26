package com.quasiris.qsf.commons.text.matcher;

import com.quasiris.qsf.dto.query.FilterOperator;
import com.quasiris.qsf.dto.query.SearchFilterDTO;
import com.quasiris.qsf.dto.response.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchFilterMatcherTest {
    private Document doc;

    @BeforeEach
    public void before() {
        doc = new Document();
        doc.getDocument().put("title", "Samsung Galaxy S21 Ultra 5G 512 GB");
        doc.getDocument().put("price", 1125.95);
        doc.getDocument().put("manufacturer", "Samsung");
        doc.getDocument().put("model", "Galaxy S21 Ultra");
        doc.getDocument().put("processor", "Octa core (2.9 GHz, Single core, Cortex X1 + 2.8 GHz, Tri core, Cortex A78 + 2.2 GHz, Quad core, Cortex A55)");
        doc.getDocument().put("colors", Arrays.asList("Phantom Silver", "Phantom Black", "Phantom Brown"));
        doc.getDocument().put("tags", Arrays.asList("samsung", "5g", "quad-core"));
        doc.getDocument().put("onStock", true);
        doc.getDocument().put("stockCount", 123);
        doc.getDocument().put("lastUpdate", "2021-03-22T20:18:12Z");
    }

    @Test
    public void matchDocument() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("manufacturer");
        filter.setFilterOperator(FilterOperator.OR);
        filter.setValues(Arrays.asList("Samsung", "IPhone"));
        filters.add(filter);
        SearchFilterDTO filter2 = new SearchFilterDTO();
        filter2.setLowerExcluded(true);
        filter2.setUpperExcluded(true);
        filter2.setMinValue(100.00);
        filter2.setMaxValue(200.00);
        filter2.setName("stockCount");
        filters.add(filter2);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertTrue(match);
    }

    @Test
    public void matchDocumentNOT() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("manufacturer");
        filter.setFilterOperator(FilterOperator.OR);
        filter.setValues(Arrays.asList("Samsung", "IPhone"));
        filters.add(filter);
        SearchFilterDTO filter2 = new SearchFilterDTO();
        filter2.setLowerExcluded(true);
        filter2.setUpperExcluded(true);
        filter2.setMinValue(100.00);
        filter2.setMaxValue(200.00);
        filter2.setName("stockCount");
        filters.add(filter2);
        filter2.setFilterOperator(FilterOperator.NOT);
        filters.add(filter2);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void matchOneValue() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("manufacturer");
        filter.setFilterOperator(FilterOperator.OR);
        filter.setValues(Arrays.asList("Samsung", "IPhone"));
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertTrue(match);
    }

    @Test
    public void matchNumber() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setLowerExcluded(true);
        filter.setUpperExcluded(true);
        filter.setMinValue(100.00);
        filter.setMaxValue(200.00);
        filter.setName("stockCount");
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertTrue(match);
    }

    @Test
    public void matchNumberNOT() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setLowerExcluded(true);
        filter.setUpperExcluded(true);
        filter.setMinValue(100.00);
        filter.setMaxValue(200.00);
        filter.setName("stockCount");
        filter.setFilterOperator(FilterOperator.NOT);
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void matchBoolean() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("onStock");
        filter.setFilterOperator(FilterOperator.OR);
        filter.setValues(Arrays.asList("true"));
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertTrue(match);
    }

    @Test
    public void matchBooleanNOT() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("onStock");
        filter.setFilterOperator(FilterOperator.NOT);
        filter.setValues(Arrays.asList("true"));
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void matchValues() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("tags");
        filter.setFilterOperator(FilterOperator.AND);
        filter.setValues(Arrays.asList("5g", "quad-core"));
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertTrue(match);
    }

    @Test
    public void matchValuesNone() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("tags");
        filter.setFilterOperator(FilterOperator.AND);
        filter.setValues(Arrays.asList("5g", "quad-core", "other"));
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void matchValuesNOT() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filter.setName("tags");
        filter.setFilterOperator(FilterOperator.NOT);
        filter.setValues(Arrays.asList("other"));
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertTrue(match);
    }

    @Test
    public void matchOneEmptyValue() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();
        SearchFilterDTO filter = new SearchFilterDTO();
        filters.add(filter);

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void matchEmptyFilters() {
        // given
        List<SearchFilterDTO> filters = new ArrayList<>();

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void matchEmptyDoc() {
        // given
        Map<String, Object> values = new HashMap<>();
        List<SearchFilterDTO> filters = new ArrayList<>();

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void matchNull() {
        // given
        Map<String, Object> values = null;
        List<SearchFilterDTO> filters = null;

        // when
        boolean match = new SearchFilterMatcher().matches(filters, doc.getDocument());

        // then
        assertFalse(match);
    }

    @Test
    public void testSimpleStringListValueAND() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.AND);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertTrue(matcher.matches(searchFilter, Arrays.asList("grün", "gelb")));
        assertFalse(matcher.matches(searchFilter, "grün"));
        assertFalse(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testSimpleStringListValueOR() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.OR);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertTrue(matcher.matches(searchFilter, Arrays.asList("grün", "gelb")));
        assertTrue(matcher.matches(searchFilter, "grün"));
        assertFalse(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testSimpleStringListValueNOT() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.NOT);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertFalse(matcher.matches(searchFilter, Arrays.asList("grün", "gelb")));
        assertFalse(matcher.matches(searchFilter, Arrays.asList("grün", "blau")));
        assertFalse(matcher.matches(searchFilter, "grün"));
        assertTrue(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testSimpleStringValueAND() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.AND);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertFalse(matcher.matches(searchFilter, "grün"));
        assertFalse(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testSimpleStringValueOR() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.OR);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertTrue(matcher.matches(searchFilter, "grün"));
        assertFalse(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testSimpleStringValueNOT() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.NOT);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertFalse(matcher.matches(searchFilter, "grün"));
        assertTrue(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testSimpleStringValueUppercase() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.OR);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertTrue(matcher.matches(searchFilter, "grün"));
        assertTrue(matcher.matches(searchFilter, "Grün"));
        assertTrue(matcher.matches(searchFilter, "GRÜN"));
        assertFalse(matcher.matches(searchFilter, "blau"));
        assertFalse(matcher.matches(searchFilter, "Blau"));
    }

    @Test
    public void testSimpleStringValueContainsAND() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.AND);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertTrue(matcher.matches(searchFilter, Arrays.asList("grün", "gelb")));
        assertFalse(matcher.matches(searchFilter, "grün (metallic)"));
        assertFalse(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testSimpleStringValueContainsOR() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setName("farbe");
        searchFilter.setFilterOperator(FilterOperator.OR);
        searchFilter.setValues(Arrays.asList("grün", "gelb"));

        assertTrue(matcher.matches(searchFilter, "grün (metallic)"));
        assertFalse(matcher.matches(searchFilter, "blau"));
    }

    @Test
    public void testRangeValueLowerIncludedWithLists() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(null);
        searchFilter.setName("speicher");

        assertFalse(matcher.matchesRangeValue(searchFilter, Arrays.asList(126.0, 127.0)));
        assertTrue(matcher.matchesRangeValue(searchFilter, Arrays.asList(126.0, 128.0)));
        assertTrue(matcher.matchesRangeValue(searchFilter, Arrays.asList(126.0, 129.0)));
    }

    @Test
    public void testRangeValueLowerIncludedWithListsNOT() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(null);
        searchFilter.setName("speicher");
        searchFilter.setFilterOperator(FilterOperator.NOT);

        assertTrue(matcher.matchesRangeValue(searchFilter, Arrays.asList(126.0, 127.0)));
        assertFalse(matcher.matchesRangeValue(searchFilter, Arrays.asList(126.0, 128.0)));
        assertFalse(matcher.matchesRangeValue(searchFilter, Arrays.asList(126.0, 129.0)));
    }

    @Test
    public void testRangeValueLowerIncluded() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(null);
        searchFilter.setName("speicher");

        assertFalse(matcher.matchesRangeValue(searchFilter, 127.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 128.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 129.0));
    }

    @Test
    public void testRangeValueLowerIncludedNOT() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(null);
        searchFilter.setName("speicher");
        searchFilter.setFilterOperator(FilterOperator.NOT);

        assertTrue(matcher.matchesRangeValue(searchFilter, 127.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 128.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 129.0));
    }

    @Test
    public void testRangeValueLowerExcluded() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(null);
        searchFilter.setLowerExcluded(true);
        searchFilter.setName("speicher");

        assertFalse(matcher.matchesRangeValue(searchFilter, 127.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 128.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 129.0));
    }

    @Test
    public void testRangeValueUpperIncluded() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(null);
        searchFilter.setMaxValue(128.0);
        searchFilter.setName("speicher");

        assertTrue(matcher.matchesRangeValue(searchFilter, 127.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 128.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 129.0));
    }

    @Test
    public void testRangeValueUpperExcluded() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(null);
        searchFilter.setMaxValue(128.0);
        searchFilter.setUpperExcluded(true);
        searchFilter.setName("speicher");

        assertTrue(matcher.matchesRangeValue(searchFilter, 127.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 128.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 129.0));
    }

    @Test
    public void testRangeValueUpperLowerExcluded() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(256.0);
        searchFilter.setLowerExcluded(true);
        searchFilter.setUpperExcluded(true);
        searchFilter.setName("speicher");

        assertFalse(matcher.matchesRangeValue(searchFilter, 127.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 128.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 129.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 255.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 256.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 257.0));
    }

    @Test
    public void testRangeValueUpperLowerIncluded() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(256.0);
        searchFilter.setName("speicher");

        assertFalse(matcher.matchesRangeValue(searchFilter, 127.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 128.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 129.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 255.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 256.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 257.0));
    }

    @Test
    public void testRangeValueUpperIncludeLowerExclude() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(256.0);
        searchFilter.setUpperExcluded(true);
        searchFilter.setName("speicher");

        assertFalse(matcher.matchesRangeValue(searchFilter, 127.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 128.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 129.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 255.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 256.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 257.0));
    }


    @Test
    public void testRangeValueUpperExcludeLowerInclude() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(128.0);
        searchFilter.setMaxValue(256.0);
        searchFilter.setLowerExcluded(true);
        searchFilter.setName("speicher");

        assertFalse(matcher.matchesRangeValue(searchFilter, 127.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 128.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 129.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 255.0));
        assertTrue(matcher.matchesRangeValue(searchFilter, 256.0));
        assertFalse(matcher.matchesRangeValue(searchFilter, 257.0));
    }

    @Test
    public void testRangeValueMinMaxNull() {
        SearchFilterMatcher matcher = new SearchFilterMatcher();
        Double min = null;
        Double max = null;
        SearchFilterDTO searchFilter = new SearchFilterDTO();
        searchFilter.setMinValue(min);
        searchFilter.setMaxValue(max);
        searchFilter.setUpperExcluded(true);
        searchFilter.setName("speicher");
        assertTrue(matcher.matchesRangeValue(searchFilter, 127.0));
    }
}