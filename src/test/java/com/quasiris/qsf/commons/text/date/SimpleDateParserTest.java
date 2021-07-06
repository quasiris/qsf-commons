package com.quasiris.qsf.commons.text.date;

import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class SimpleDateParserTest {
    @Test
    public void test() {
        SimpleDateParser parser = new SimpleDateParser("2021-02-10");
        assertEquals("2021-02-09T23:00:00Z", parser.getInstant().toString());
        assertEquals("2021-02-10T23:00:00Z", parser.getInstant().plus(1, ChronoUnit.DAYS).toString());
    }
}