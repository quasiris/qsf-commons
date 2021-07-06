package com.quasiris.qsf.commons.text.date;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DayDateParser implements DateParser {


    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;

    private Duration duration;


    public DayDateParser(String duration) {
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
        zdt = zdt.toLocalDate().atStartOfDay ( zoneId );
        return zdt.toInstant();
    }

    @Override
    public Instant getEnd() {
        Instant start = reference.minusSeconds(duration.getSeconds());
        ZonedDateTime zdt = ZonedDateTime.ofInstant ( start , zoneId );
        zdt = zdt.toLocalDate ().atStartOfDay ( zoneId );
        ZonedDateTime nextDay = zdt.plusDays( 1 );
        return nextDay.toInstant();
    }
}
