package com.quasiris.qsf.commons.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterUtilsTest {

    @Test
    public void testGetParameter_string() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("age", "25");

        // Act
        String result = ParameterUtils.getParameter(params, "age");

        // Assert
        assertEquals("25", result);
    }
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


    @Test
    public void testGetParameterTypeReference_existingParameterWithCorrectType() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("age", 25);

        // Act
        Integer result = ParameterUtils.getParameter(params, "age", 0, new TypeReference<Integer>() {});

        // Assert
        assertEquals(25, result);
    }

    @Test
    public void testGetParameterTypeReference_existingParameterWithIncorrectType() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("age", "25"); // Value is a string, not an integer

        // Act
        Integer result = ParameterUtils.getParameter(params, "age", 0, new TypeReference<Integer>() {});

        // Assert
        assertEquals(0, result); // Should return default value since types don't match
    }

    @Test
    public void testGetParameterTypeReference_existingParameterWithListType() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("names", Arrays.asList("Alice", "Bob"));

        // Act
        List<String> result = ParameterUtils.getParameter(params, "names", Arrays.asList("Default"), new TypeReference<List<String>>() {});

        // Assert
        assertEquals(Arrays.asList("Alice", "Bob"), result); // Should return the list of names
    }

    @Test
    public void testGetParameterTypeReference_existingParameterWithMismatchedListType() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("names", Arrays.asList(1, 2, 3)); // Value is a list of integers

        // Act
        List<String> result = ParameterUtils.getParameter(params, "names", Arrays.asList("Default"), new TypeReference<List<String>>() {});

        // Assert
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testGetParameterTypeReference_nonExistingParameter() {
        // Setup
        Map<String, Object> params = new HashMap<>();

        // Act
        String result = ParameterUtils.getParameter(params, "nonExistingKey", "DefaultValue", new TypeReference<String>() {});

        // Assert
        assertEquals("DefaultValue", result); // Should return the default value
    }

    @Test
    public void testGetParameterTypeReference_nullValueInMap() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        params.put("name", null);

        // Act
        String result = ParameterUtils.getParameter(params, "name", "DefaultValue", new TypeReference<String>() {});

        // Assert
        assertEquals("DefaultValue", result); // Should return the default value since the parameter is null
    }

    @Test
    public void testGetParameterTypeReference_withComplexObject() {
        // Setup
        Map<String, Object> params = new HashMap<>();
        Person person = new Person("John", 30);
        params.put("person", person);

        // Act
        Person result = ParameterUtils.getParameter(params, "person", new Person("Default", 0), new TypeReference<Person>() {});

        // Assert
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge()); // Should return the Person object
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
