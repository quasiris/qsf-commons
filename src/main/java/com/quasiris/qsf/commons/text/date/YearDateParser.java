package com.quasiris.qsf.commons.text.date;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class YearDateParser implements DateParser {


    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;

    private Duration duration;


    public YearDateParser(String duration) {
        this.duration = Duration.parse(duration);
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
        Instant start = reference.minusSeconds(duration.getSeconds());
        ZonedDateTime zdt = ZonedDateTime.ofInstant ( start , zoneId );
        ZonedDateTime firstOfYear = zdt.with ( ChronoField.DAY_OF_YEAR , 1 );
        firstOfYear = firstOfYear.toLocalDate().atStartOfDay ( zoneId );
        return firstOfYear;
    }


    @Override
    public Instant getEnd() {
        return getFirstOfYear().plusYears(1).toInstant();
    }
}
