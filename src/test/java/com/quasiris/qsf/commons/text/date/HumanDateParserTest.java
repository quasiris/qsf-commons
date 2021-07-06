package com.quasiris.qsf.commons.text.date;


import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;


public class HumanDateParserTest {
    @Test
    public void  testHeute() {
        HumanDateParser parser = new HumanDateParser("heute");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-02-09T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-10T23:00:00Z", parser.getEnd().toString());
    }


    @Test
    public void  testGestern() {
        HumanDateParser parser = new HumanDateParser("gestern");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-02-08T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-09T23:00:00Z", parser.getEnd().toString());
    }


    @Test
    public void  testLetzteWoche() {
        HumanDateParser parser = new HumanDateParser("letzte Woche");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-01-31T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-07T23:00:00Z", parser.getEnd().toString());
    }

    @Test
    public void  testDieseWoche() {
        HumanDateParser parser = new HumanDateParser("diese Woche");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-02-07T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-14T23:00:00Z", parser.getEnd().toString());
    }

    @Test
    public void  testLetzterMonat() {
        HumanDateParser parser = new HumanDateParser("letzter Monat");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2020-12-31T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-01-31T23:00:00Z", parser.getEnd().toString());
    }
    @Test
    public void  testDieserMonat() {
        HumanDateParser parser = new HumanDateParser("dieser Monat");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-01-31T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-28T23:00:00Z", parser.getEnd().toString());
    }


    @Test
    public void  letztesJahr() {
        HumanDateParser parser = new HumanDateParser("letztes Jahr");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2019-12-31T23:00:00Z", parser.getStart().toString());
        assertEquals("2020-12-31T23:00:00Z", parser.getEnd().toString());
    }
    @Test
    public void  diesesJahr() {
        HumanDateParser parser = new HumanDateParser("dieses Jahr");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2020-12-31T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-12-31T23:00:00Z", parser.getEnd().toString());
    }
}
