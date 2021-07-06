package com.quasiris.qsf.commons.text.date;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleDateParser {

    public SimpleDateParser(String simpleDate) {
        this.simpleDate = simpleDate;
    }

    private String simpleDate;

    private ZoneId zoneId = ZoneId.of("Europe/Berlin");

    Instant parsedDate;

    private static Map<Integer, String> patterns = new HashMap<>();
    static {
        patterns.put("2020-08-06".length(), "yyyy-MM-dd");
        patterns.put("2020-08-06T22:18:26+0000".length(), "yyyy-MM-dd'T'HH:mm:ssZ");
        patterns.put("2020-08-06T22:18:26.528+0000".length(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        patterns.put("2020-08-06T22:18:26.528+00:00".length(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    public Instant getInstant() {
        if(parsedDate == null) {
            parsedDate = getDateTimeFormatter().parse(simpleDate, Instant::from);
        }
        return parsedDate;
    }
    public Date getDate() {
        if(parsedDate == null) {
            getInstant();
        }
        return Date.from(parsedDate);
    }

    DateTimeFormatter getDateTimeFormatter() {
        if(simpleDate.length() == "2020-08-06".length() ) {
            return  new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
                    .toFormatter()
                    .withZone(zoneId);
        }
        return null;
    }



}
