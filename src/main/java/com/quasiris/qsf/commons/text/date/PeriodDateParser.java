package com.quasiris.qsf.commons.text.date;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class PeriodDateParser implements DateParser {

    private ZoneId zoneId = ZoneId.of ( "Europe/Berlin" );

    private Instant reference;

    private Period period;

    private ChronoUnit truncateTo = ChronoUnit.DAYS;

    public PeriodDateParser(String period) {
        this.period = Period.parse(period);
    }
    public PeriodDateParser(String period, ChronoUnit truncateTo) {
        this.period = Period.parse(period);
        this.truncateTo = truncateTo;
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;
    }

    @Override
    public Instant getStart() {

        ZonedDateTime referernceZonedDateTime = ZonedDateTime.ofInstant ( reference , zoneId );
        ZonedDateTime start;
        if(truncateTo.equals(ChronoUnit.WEEKS)) {
            start = referernceZonedDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .truncatedTo(ChronoUnit.DAYS);
        } else if(truncateTo.equals(ChronoUnit.MONTHS)) {
            start = referernceZonedDateTime.with(TemporalAdjusters.firstDayOfMonth())
                    .truncatedTo(ChronoUnit.DAYS);
        } else if(truncateTo.equals(ChronoUnit.YEARS)) {
            start = referernceZonedDateTime.with(TemporalAdjusters.firstDayOfYear())
                    .truncatedTo(ChronoUnit.DAYS);
        } else {
            start = referernceZonedDateTime.truncatedTo(truncateTo);
        }

        ZonedDateTime zdt = start.minus(period);
        return zdt.toInstant();

    }

    @Override
    public Instant getEnd() {

        // always truncate to the end of the current day
        return reference.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
    }




    public ChronoUnit getTruncateTo() {
        return truncateTo;
    }

    public void setTruncateTo(ChronoUnit truncateTo) {
        this.truncateTo = truncateTo;
    }
}
