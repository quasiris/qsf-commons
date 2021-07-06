package com.quasiris.qsf.commons.text.date;

import java.time.Instant;

public interface DateParser {

    void setReference(Instant reference);
    Instant getStart();
    Instant getEnd();
}
