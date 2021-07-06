package com.quasiris.qsf.commons.text.date;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

public class MonthDateParser implements DateParser {


    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;

    private Duration duration;


    public MonthDateParser(String duration) {
        this.duration = Duration.parse(duration);
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;

    }

    @Override
    public Instant getStart() {
        Instant start = reference.minusSeconds(duration.getSeconds());
        ZonedDateTime zdt = ZonedDateTime.ofInstant ( start , zoneId );
        ZonedDateTime firstOfWeek = zdt.with ( ChronoField.DAY_OF_MONTH , 1 ); // ISO 8601, Monday is first day of week.
        firstOfWeek = firstOfWeek.toLocalDate().atStartOfDay ( zoneId );
        return firstOfWeek.toInstant();
    }

    @Override
    public Instant getEnd() {
        Instant start = reference.minusSeconds(duration.getSeconds());
        ZonedDateTime zdt = ZonedDateTime.ofInstant ( start , zoneId );
        ZonedDateTime firstOfWeek = zdt.with ( ChronoField.DAY_OF_MONTH , 1 ); // ISO 8601, Monday is first day of week.
        firstOfWeek = firstOfWeek.toLocalDate ().atStartOfDay ( zoneId );
        ZonedDateTime firstOfNextWeek = firstOfWeek.plusMonths ( 1 );
        return firstOfNextWeek.toInstant();
    }
}
