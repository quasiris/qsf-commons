package com.quasiris.qsf.commons.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

public class MustacheUtil {
    public static String compileMustache(String template, Object data) {
        StringReader reader = new StringReader(template);
        return compileMustache(reader, data);
    }

    public static String compileMustache(Reader reader, Object data) {
        String text = null;
        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache m = mf.compile(reader, "todo.mustache");
            StringWriter writer = new StringWriter();
            m.execute(writer, data).flush();
            text = writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not compile mustache template!", e);
        }
        return text;
    }
}
