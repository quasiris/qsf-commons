package com.quasiris.qsf.commons.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElasticUtilTest {

    @Test
    public void value2FieldNameSlashUmlaut() {

        String fieldName = ElasticUtil.value2FieldName("Farbe / Ausführung");
        Assert.assertEquals("farbe___ausf_hrung", fieldName);
    }

    @Test
    public void value2FieldNameHiphone() {

        String fieldName = ElasticUtil.value2FieldName("Display-Beleuchtung");
        Assert.assertEquals("display_beleuchtung", fieldName);
    }
}