package com.quasiris.qsf.commons.text.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SupportedDateFormatsParserTest {

    @Test
    void testRequireInstantFromInstant() {
        Instant now = Instant.now();
        Instant fromInstant = SupportedDateFormatsParser.requireInstantFromString(now.toString());
        Assertions.assertEquals(now, fromInstant);
    }

    @Test
    void testRequireFromInstantEqualsRequireFromString() {
        Instant now = Instant.now();

        String fromInstant = SupportedDateFormatsParser.requireFromInstant(now);
        String fromString = SupportedDateFormatsParser.requireFromString(now.toString());
        Assertions.assertEquals(fromString, fromInstant);
    }

    @Test
    public void testRequireFromString() {
        String actual = SupportedDateFormatsParser.requireFromString("2023-07-01T22:55:02.897748Z");
        Assertions.assertEquals("2023-07-01T22:55:02.897+0000", actual);
    }
    @Test
    public void testRequireFromStringOfResultOfRequireFromString() {
        String result1 = SupportedDateFormatsParser.requireFromString(Instant.now().toString());
        String result2 = SupportedDateFormatsParser.requireFromString(result1);
        Assertions.assertEquals(result1, result2);
    }
}