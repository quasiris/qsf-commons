package com.quasiris.qsf.commons.util;

import java.util.Map;

public class ParameterUtils {

    public static <T> T getParameter(Map<String, Object> parameters, String param, T defaultValue, Class<T> toValueType) {
        if(parameters == null) {
            return defaultValue;
        }
        Object value = parameters.get(param);
        if(value == null) {
            return defaultValue;
        }

        return JsonUtil.defaultMapper().convertValue(value, toValueType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getParameter(Map<String, Object> parameters, String param, T defaultValue) {
        if (parameters != null && parameters.containsKey(param)) {
            Object value = parameters.get(param);
            return attemptConversion(value, defaultValue);
        }
        return defaultValue;
    }

    private static <T> T attemptConversion(Object value, T defaultValue) {
        if (defaultValue == null) {
            return null; // If the defaultValue is null, we can't infer the type
        }

        // Handle specific conversions
        if (defaultValue instanceof Integer) {
            try {
                return (T) Integer.valueOf(value.toString());
            } catch (NumberFormatException e) {
                System.out.println("Failed to convert value to Integer.");
                return defaultValue;
            }
        } else if (defaultValue instanceof Double) {
            try {
                return (T) Double.valueOf(value.toString());
            } catch (NumberFormatException e) {
                System.out.println("Failed to convert value to Double.");
                return defaultValue;
            }
        } else if (defaultValue instanceof Boolean) {
            try {
                return (T) Boolean.valueOf(value.toString());
            } catch (Exception e) {
                System.out.println("Failed to convert value to Boolean.");
                return defaultValue;
            }
        } else if (defaultValue instanceof String) {
            return (T) value.toString(); // Convert any object to string
        }

        // If no conversion is possible, return the default value
        return defaultValue;
    }
}
