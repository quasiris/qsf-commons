package com.quasiris.qsf.commons.text.matcher;

import com.quasiris.qsf.query.FilterOperator;
import com.quasiris.qsf.query.RangeFilterValue;
import com.quasiris.qsf.query.SearchFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchFilterMatcher {

    private Matcher matcher;

    public SearchFilterMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    public SearchFilterMatcher() {
        this.matcher = new ContainsLowerCaseMatcher();
    }

    public boolean matches(List<SearchFilter> filters, Map<String, Object> values) {
        boolean result = false;
        int matchedFilters = 0;
        if(values != null && values.size() > 0 &&
                filters != null && filters.size() > 0) {
            for (SearchFilter filter : filters) { // concatenate filters with AND
                String key = filter.getName();
                if (values.containsKey(key)) {
                    Object rawValue = values.get(key);
                    boolean match = matches(filter, rawValue);
                    if(match && matchedFilters == 0) {
                        result = true; // required for AND concatenation
                        matchedFilters++;
                    }
                    result &= match;
                    if (!result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public boolean matches(SearchFilter filter, Object value) {
        boolean result = false;
        if(value != null) {
            if (value instanceof List) {
                List rawValues = (List) value;
                if (!rawValues.isEmpty() && rawValues.get(0) instanceof String) {
                    result = matches(filter, (List<String>) rawValues);
                } else if (!rawValues.isEmpty() && rawValues.get(0) instanceof Double) {
                    result = matchesRangeValue(filter, (List<Double>) rawValues);
                }
            } else {
                if (value instanceof Integer) {
                    value = ((Integer)value).doubleValue();
                } else if (value instanceof Float) {
                    value = ((Float)value).doubleValue();
                }

                if (value instanceof Double) {
                    result = matchesRangeValue(filter, (Double) value);
                } else {
                    result = matches(filter, value.toString());
                }
            }
        }
        return result;
    }

    public boolean matches(SearchFilter searchFilter, List<String> values) {
        boolean result = false;
        if(searchFilter != null && values != null && values.size() > 0) {
            result = searchFilter.getFilterOperator() == FilterOperator.AND;
            for (String filterValue : searchFilter.getValues()) { // concatenate with FilterOperator
                boolean valueResults = false;
                for (String value : values) { // concatenate values with OR
                    boolean matches = matcher.matches(value, filterValue);
                    valueResults |= matches;
                    if (matches) {
                        break;
                    }
                }

                if (searchFilter.getFilterOperator() == FilterOperator.AND) {
                    result &= valueResults;
                    if (!result) {
                        break;
                    }
                } else { // otherwise FilterOperator.OR behaviour
                    if (valueResults) {
                        result = true;
                        break;
                    }
                }
            }

            if (searchFilter.getFilterOperator() == FilterOperator.NOT) { // NOT is combined always with OR (aka NOT_OR)
                result = !result; // invert
            }
        }
        return result;
    }

    public boolean matches(SearchFilter searchFilter, String value) {
        boolean result = false;
        if(value != null) {
            result = matches(searchFilter, Arrays.asList(value));
        }
        return result;
    }

    public boolean matchesRangeValue(SearchFilter searchFilter, List<Double> values) {
        boolean result = false;
        for(Double value : values) {
            RangeFilterValue<Double> rangeValue = searchFilter.getRangeValue(Double.class);
            Double minValue = rangeValue.getMinValue();
            Double maxValue = rangeValue.getMaxValue();

            boolean matches = false;
            if(value == null) {
                matches = false;
            } else if(minValue == null && maxValue == null) {
                matches = true;
            } else if(searchFilter.isUpperExcluded() && minValue == null) {
                if(value < maxValue) {
                    matches = true;
                }
            } else if(searchFilter.isUpperIncluded() && minValue == null) {
                if(value <= maxValue) {
                    matches = true;
                }
            } else if(searchFilter.isLowerExcluded() && maxValue == null) {
                if(value > minValue) {
                    matches = true;
                }
            } else if(searchFilter.isLowerIncluded() && maxValue == null ) {
                if(value >= minValue) {
                    matches = true;
                }
            } else if(searchFilter.isLowerExcluded() && searchFilter.isUpperExcluded()) {
                if(value > minValue && value < maxValue) {
                    matches = true;
                }
            } else if(searchFilter.isLowerIncluded() && searchFilter.isUpperIncluded()) {
                if(value >= minValue && value <= maxValue) {
                    matches = true;
                }
            } else if(searchFilter.isLowerIncluded() && searchFilter.isUpperExcluded()) {
                if(value >= minValue && value < maxValue) {
                    matches = true;
                }
            } else if(searchFilter.isLowerExcluded() && searchFilter.isUpperIncluded()) {
                if(value > minValue && value <= maxValue) {
                    matches = true;
                }
            }
            result |= matches;
            if(matches) {
                break;
            }
        }
        if (searchFilter.getFilterOperator() == FilterOperator.NOT) {
            result = !result; // invert
        }

        return result;
    }

    public boolean matchesRangeValue(SearchFilter searchFilter, Double value) {
        boolean result = false;
        if(value != null) {
            result = matches(searchFilter, Arrays.asList(value));
        }
        return result;
    }
}
