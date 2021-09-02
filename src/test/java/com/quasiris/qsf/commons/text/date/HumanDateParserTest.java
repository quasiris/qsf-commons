package com.quasiris.qsf.commons.text.date;


import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;


public class HumanDateParserTest {
    @Test
    public void  testFuture() {
        HumanDateParser parser = new HumanDateParser("zukunft");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-02-10T10:15:30Z", parser.getStart().toString());
        assertEquals(Instant.MAX, parser.getEnd());
    }
    @Test
    public void  testPast() {
        HumanDateParser parser = new HumanDateParser("vergangenheit");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals(Instant.MIN, parser.getStart());
        assertEquals("2021-02-10T10:15:30Z", parser.getEnd().toString());
    }

    @Test
    public void  testHeute() {
        HumanDateParser parser = new HumanDateParser("heute");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-02-09T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-10T23:00:00Z", parser.getEnd().toString());
    }
    @Test
    public void  letzten7Tage() {
        HumanDateParser parser = new HumanDateParser("letzten 7 Tage");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2021-02-03T00:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-11T00:00:00Z", parser.getEnd().toString());
    }

    @Test
    public void  letzten3Monate() {
        HumanDateParser parser = new HumanDateParser("letzten 3 Monate");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2020-10-31T23:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-11T00:00:00Z", parser.getEnd().toString());
    }

    @Test
    public void  letzten6Monate() {
        HumanDateParser parser = new HumanDateParser("letzten 6 Monate");
        parser.setNow(Instant.parse("2021-02-10T10:15:30.00Z"));
        assertEquals("2020-07-31T22:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-11T00:00:00Z", parser.getEnd().toString());
    }


    @Test
    public void  letzten6MonateMonatsanfang() {
        HumanDateParser parser = new HumanDateParser("letzten 6 Monate");
        parser.setNow(Instant.parse("2021-02-01T10:15:30.00Z"));
        assertEquals("2020-07-31T22:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-02T00:00:00Z", parser.getEnd().toString());
    }

    @Test
    public void  letzten6MonateMonatsende() {
        HumanDateParser parser = new HumanDateParser("letzten 6 Monate");
        parser.setNow(Instant.parse("2021-01-31T10:15:30.00Z"));
        assertEquals("2020-06-30T22:00:00Z", parser.getStart().toString());
        assertEquals("2021-02-01T00:00:00Z", parser.getEnd().toString());
    }

    @Test
    public void  jahr2000() {
        HumanDateParser parser = new HumanDateParser("2000");
        parser.setNow(Instant.parse("2021-01-31T10:15:30.00Z"));
        assertEquals("1999-12-31T23:00:00Z", parser.getStart().toString());
        assertEquals("2000-12-31T23:00:00Z", parser.getEnd().toString());
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
