package com.quasiris.qsf.commons.text.matcher;

import com.quasiris.qsf.dto.FilterOperator;
import com.quasiris.qsf.dto.SearchFilterDTO;

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

    public boolean matches(List<SearchFilterDTO> filters, Map<String, Object> values) {
        boolean result = false;
        int matchedFilters = 0;
        if(values != null && values.size() > 0 &&
                filters != null && filters.size() > 0) {
            for (SearchFilterDTO filter : filters) { // concatenate filters with AND
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

    public boolean matches(SearchFilterDTO filter, Object value) {
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

    public boolean matches(SearchFilterDTO searchFilter, List<String> values) {
        boolean result = false;
        if(searchFilter != null && values != null && values.size() > 0) {
            result = searchFilter.getFilterOperator() == FilterOperator.AND;
            for (Object filterValue : searchFilter.getValues()) { // concatenate with FilterOperator
                boolean valueResults = false;
                for (String value : values) { // concatenate values with OR
                    boolean matches = matcher.matches(value, filterValue.toString());
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

    public boolean matches(SearchFilterDTO searchFilter, String value) {
        boolean result = false;
        if(value != null) {
            result = matches(searchFilter, Arrays.asList(value));
        }
        return result;
    }

    public boolean matchesRangeValue(SearchFilterDTO searchFilter, List<Double> values) {
        boolean result = false;
        for(Double value : values) {
            Double minValue = null;
            if(searchFilter.getMinValue() != null) {
                try {
                    minValue = Double.parseDouble(searchFilter.getMinValue().toString());
                } catch (NumberFormatException ignored) {
                }
            }

            Double maxValue = null;
            if(searchFilter.getMaxValue() != null) {
                try {
                    maxValue = Double.parseDouble(searchFilter.getMaxValue().toString());
                } catch (NumberFormatException ignored) {
                }
            }

            boolean matches = false;
            boolean lowerIncluded = searchFilter.getLowerIncluded() == null || searchFilter.getLowerIncluded();
            boolean upperIncluded = searchFilter.getUpperIncluded() == null || searchFilter.getUpperIncluded();
            boolean lowerExcluded = searchFilter.getLowerExcluded() != null && searchFilter.getLowerExcluded();
            boolean upperExcluded = searchFilter.getUpperExcluded() != null && searchFilter.getUpperExcluded();
            if(value == null) {
                matches = false;
            } else if(minValue == null && maxValue == null) {
                matches = true;
            } else if(upperExcluded && minValue == null) {
                if(value < maxValue) {
                    matches = true;
                }
            } else if(upperIncluded && minValue == null) {
                if(value <= maxValue) {
                    matches = true;
                }
            } else if(lowerExcluded && maxValue == null) {
                if(value > minValue) {
                    matches = true;
                }
            } else if(lowerIncluded && maxValue == null ) {
                if(value >= minValue) {
                    matches = true;
                }
            } else if(lowerExcluded && upperExcluded) {
                if(value > minValue && value < maxValue) {
                    matches = true;
                }
            } else if(lowerIncluded && upperIncluded) {
                if(value >= minValue && value <= maxValue) {
                    matches = true;
                }
            } else if(lowerIncluded && upperExcluded) {
                if(value >= minValue && value < maxValue) {
                    matches = true;
                }
            } else if(lowerExcluded && upperIncluded) {
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

    public boolean matchesRangeValue(SearchFilterDTO searchFilter, Double value) {
        boolean result = false;
        if(value != null) {
            result = matches(searchFilter, Arrays.asList(value));
        }
        return result;
    }
}
