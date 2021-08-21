package com.quasiris.qsf.commons.text.date;

import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class PeriodDateParser implements DateParser {

    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;

    private Period period;

    private ChronoUnit truncateTo = ChronoUnit.MONTHS;

    public PeriodDateParser(String period) {
        this.period = Period.parse(period);
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;
    }

    @Override
    public Instant getStart() {

        ZonedDateTime referernceZonedDateTime = ZonedDateTime.ofInstant ( reference , zoneId );
        ZonedDateTime zdt = referernceZonedDateTime.minus(period);
        ZonedDateTime firstOfMonth = zdt.with ( ChronoField.DAY_OF_MONTH , 1 );
        firstOfMonth = firstOfMonth.toLocalDate().atStartOfDay ( zoneId );
        return firstOfMonth.toInstant();

    }

    @Override
    public Instant getEnd() {
        return reference.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
    }
}
