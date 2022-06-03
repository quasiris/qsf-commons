package com.quasiris.qsf.commons.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Type;

public class GenericUtils {
    public static <T> TypeReference<T> castTypeReference(Class<T> cls) {
        return new TypeReference<T>() {
            @Override
            public Type getType() {
                return cls;
            }
        };
    }

    public static <T> Class<?> castClass(TypeReference<T> typeReference) {
        return TypeFactory.defaultInstance().constructType(typeReference).getRawClass();
    }

    public static boolean instanceOf(Class<?> cls, Class<?> clsTarget) {
        return cls.isAssignableFrom(clsTarget);
    }
}
