package com.quasiris.qsf.commons.text.date;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class DurationDateParser implements DateParser {

    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;

    private Duration duration;

    private ChronoUnit truncateTo = ChronoUnit.DAYS;

    public DurationDateParser(String duration) {
        this.duration = Duration.parse(duration);
    }

    public DurationDateParser(String duration, ChronoUnit truncateTo) {
        this.duration = Duration.parse(duration);
        this.truncateTo = truncateTo;
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;
    }

    @Override
    public Instant getStart() {
        if(truncateTo == ChronoUnit.MONTHS) {
            Instant start = reference.minusSeconds(duration.getSeconds());
            ZonedDateTime zdt = ZonedDateTime.ofInstant ( start , zoneId );
            ZonedDateTime firstOfMonth = zdt.with ( ChronoField.DAY_OF_MONTH , 1 );
            firstOfMonth = firstOfMonth.toLocalDate().atStartOfDay ( zoneId );
            return firstOfMonth.toInstant();
        }

        return reference.minusSeconds(duration.getSeconds()).truncatedTo(truncateTo);
    }

    @Override
    public Instant getEnd() {
        return reference.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
    }
}
