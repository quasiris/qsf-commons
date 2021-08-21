package com.quasiris.qsf.commons.text.date;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class YearParser implements DateParser {


    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;

    private String year;

    public YearParser(String year) {
        this.year = year;
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;

    }

    @Override
    public Instant getStart() {
        return getFirstOfYear().toInstant();
    }


    ZonedDateTime getFirstOfYear() {
        return ZonedDateTime.of(Integer.valueOf(year), 1, 1,0,0,0,0, zoneId );

    }




    @Override
    public Instant getEnd() {
        return getFirstOfYear().plusYears(1).toInstant();
    }
}
