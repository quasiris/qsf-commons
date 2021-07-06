package com.quasiris.qsf.commons.text.date;

import java.time.Duration;
import java.time.Instant;

public class DurationDateParser implements DateParser {

    private Instant reference;

    private Duration duration;

    public DurationDateParser(String duration) {
        this.duration = Duration.parse(duration);
    }

    @Override
    public void setReference(Instant reference) {
        this.reference = reference;
    }

    @Override
    public Instant getStart() {
        return reference.minusSeconds(duration.getSeconds());
    }

    @Override
    public Instant getEnd() {
        return reference;
    }
}
