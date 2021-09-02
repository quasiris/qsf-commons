package com.quasiris.qsf.commons.text.date;

import java.time.Instant;
import java.time.ZoneId;

public class PastParser implements DateParser {


    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;


    public PastParser() {
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;

    }

    @Override
    public Instant getStart() {
        return Instant.MIN;
    }


    @Override
    public Instant getEnd() {
        return reference;
    }
}
