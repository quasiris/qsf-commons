package com.quasiris.qsf.commons.text.date;

import java.time.Instant;
import java.time.ZoneId;

public class FutureParser implements DateParser {


    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;


    public FutureParser() {
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;

    }

    @Override
    public Instant getStart() {
        return reference;
    }

    @Override
    public Instant getEnd() {
        return Instant.MAX;
    }
}
