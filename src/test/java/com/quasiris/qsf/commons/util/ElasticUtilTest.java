package com.quasiris.qsf.commons.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElasticUtilTest {

    @Test
    public void value2FieldNameSlashUmlaut() {

        String fieldName = ElasticUtil.value2FieldName("Farbe / Ausführung");
        Assert.assertEquals("farbe___ausfuehrung", fieldName);
    }

    @Test
    public void value2FieldNameEUR() {

        String fieldName = ElasticUtil.value2FieldName("Preis in €");
        Assert.assertEquals("preis_in_eur", fieldName);
    }

    @Test
    public void value2FieldNameHiphone() {

        String fieldName = ElasticUtil.value2FieldName("Display-Beleuchtung");
        Assert.assertEquals("display_beleuchtung", fieldName);
    }
}