package com.quasiris.qsf.commons.util;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlFactory {
    public static <T> List<T> fromString(String data, Map<String, Object> vars) {
        StringReader reader = new StringReader(data);
        List<T> yamlObjects = fromReader(reader, vars);
        return yamlObjects;
    }

    public static <T> List<T> fromResource(String resourceFilepath, Map<String, Object> vars) {
        InputStream resourceAsStream = YamlFactory.class.getClassLoader().getResourceAsStream(resourceFilepath);
        InputStreamReader reader = new InputStreamReader(resourceAsStream);
        List<T> yamlObjects = fromReader(reader, vars);
        return yamlObjects;
    }

    public static <T> List<T> fromReader(Reader reader, Map<String, Object> vars) {
        List<T> yamlObjects = null;
        try {
            String yamlText = MustacheUtil.compileMustache(reader, vars);

            DumperOptions options = createYamlOptions();
            Yaml yaml = new Yaml(createAllowAllLoadOptions(), options);
            Iterable<Object> objects = yaml.loadAll(yamlText);
            yamlObjects = new ArrayList<>();
            for(Object obj : objects) {
                yamlObjects.add((T) obj);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load yaml!", e);
        }

        return yamlObjects;
    }

    public static DumperOptions createYamlOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setAllowReadOnlyProperties(true);

        return options;
    }

    public static LoaderOptions createAllowAllLoadOptions() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(tag -> true);
        return loaderOptions;
    }

    public static <T> String serialize(T obj) {
        DumperOptions options = createYamlOptions();
        Yaml yaml = new Yaml(createAllowAllLoadOptions(), options);
        String output = yaml.dump(obj);
        return output;
    }
}
