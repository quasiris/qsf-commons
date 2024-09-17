package com.quasiris.qsf.commons.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterUtilsTest {

    @Test
    public void testGetParameter_existingParameterWithCorrectType() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("age", 25);

        // Act
        int result = ParameterUtils.getParameter(params, "age", 0);

        // Assert
        assertEquals(25, result);
    }

    @Test
    public void testGetParameter_existingParameterWithIncorrectType() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("age", "25"); // String instead of expected int

        // Act
        Integer result = ParameterUtils.getParameter(params, "age", 0);

        // Assert
        assertEquals(25, result); // Expecting the default value due to type mismatch
    }

    @Test
    public void testGetParameter_nonExistingParameter() {
        // Setup
        Map<String, Object> params = new HashMap<>();

        // Act
        String result = ParameterUtils.getParameter(params, "name", "DefaultName");

        // Assert
        assertEquals("DefaultName", result); // Should return the default value
    }

    @Test
    public void testGetParameter_existingParameterWithString() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("name", "John");

        // Act
        String result = ParameterUtils.getParameter(params, "name", "DefaultName");

        // Assert
        assertEquals("John", result);
    }

    @Test
    public void testGetParameter_nullMap() {
        // Act
        String result = ParameterUtils.getParameter(null, "name", "DefaultName");

        // Assert
        assertEquals("DefaultName", result); // Expecting the default value since the map is null
    }

    @Test
    public void testGetParameter_nullKeyInMap() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put(null, "value");

        // Act
        String result = ParameterUtils.getParameter(params, null, "Default");

        // Assert
        assertEquals("value", result); // Should return the value associated with the null key
    }

    @Test
    public void testGetParameter_nullDefaultValue() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("name", "John");

        // Act
        String result = ParameterUtils.getParameter(params, "nonExistingKey", null);

        // Assert
        assertNull(result); // Should return null as the default value is null
    }

    @Test
    public void testGetParameterWithType_existingParameterWithCorrectType() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("age", 25);

        // Act
        Integer result = ParameterUtils.getParameter(params, "age", 0, Integer.class);

        // Assert
        assertEquals(25, result);
    }

    @Test
    public void testGetParameterWithType_existingParameterWithConvertibleStringToInt() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("age", "30");

        // Act
        Integer result = ParameterUtils.getParameter(params, "age", 0, Integer.class);

        // Assert
        assertEquals(30, result); // String should be successfully converted to Integer
    }

    @Test
    public void testGetParameterWithType_existingParameterWithConvertibleStringToDouble() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("price", "49.99");

        // Act
        Double result = ParameterUtils.getParameter(params, "price", 0.0, Double.class);

        // Assert
        assertEquals(49.99, result); // String should be converted to Double
    }

    @Test
    public void testGetParameterWithType_existingParameterWithBooleanConversion() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("isActive", "true");

        // Act
        Boolean result = ParameterUtils.getParameter(params, "isActive", false, Boolean.class);

        // Assert
        assertTrue(result); // String "true" should be converted to Boolean true
    }

    @Test
    public void testGetParameterWithType_nonExistingParameter() {
        // Setup
        Map<String, Object> params = new HashMap<>();

        // Act
        String result = ParameterUtils.getParameter(params, "name", "DefaultName", String.class);

        // Assert
        assertEquals("DefaultName", result); // Should return the default value because key does not exist
    }

    @Test
    public void testGetParameterWithType_nullMap() {
        // Act
        String result = ParameterUtils.getParameter(null, "name", "DefaultName", String.class);

        // Assert
        assertEquals("DefaultName", result); // Should return the default value since the map is null
    }

    @Test
    public void testGetParameterWithType_nullParameterValue() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("name", null);

        // Act
        String result = ParameterUtils.getParameter(params, "name", "DefaultName", String.class);

        // Assert
        assertEquals("DefaultName", result); // Should return the default value because the parameter value is null
    }

    @Test
    public void testGetParameterWithType_withComplexObjectConversion() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("person", new HashMap<String, Object>() {{
            put("name", "John");
            put("age", 30);
        }});

        // Act
        Person result = ParameterUtils.getParameter(params, "person", new Person("Default", 0), Person.class);

        // Assert
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge()); // Should convert map to Person object
    }

    public static class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
