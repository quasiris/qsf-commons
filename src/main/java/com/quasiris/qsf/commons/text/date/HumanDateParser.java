package com.quasiris.qsf.commons.text.date;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class HumanDateParser {

    public HumanDateParser(String humanDate) {
        this.humanDate = humanDate;
    }

    private String humanDate;

    private Instant now = Instant.now();

    private DateParser dateParser;

    private static Map<String, DateParser> durationMap = new HashMap<>();
    static {
        durationMap.put("last day", new DurationDateParser("P1D"));
        durationMap.put("yesterday", new DayDateParser("P1D"));
        durationMap.put("gestern", new DayDateParser("P1D"));
        durationMap.put("today", new DayDateParser("P0D"));
        durationMap.put("heute", new DayDateParser("P0D"));
        durationMap.put("last 3 days", new DurationDateParser("P3D"));
        durationMap.put("letzten 3 tage", new DurationDateParser("P3D"));
        durationMap.put("this week", new WeekDateParser("P0D"));
        durationMap.put("diese woche", new WeekDateParser("P0D"));
        durationMap.put("last week", new WeekDateParser("P7D"));
        durationMap.put("letzte woche", new WeekDateParser("P7D"));
        durationMap.put("last 3 month", new DurationDateParser("P30D"));
        durationMap.put("letzten 3 monate", new DurationDateParser("P90D"));
        durationMap.put("this month", new MonthDateParser("P0D"));
        durationMap.put("dieser monat", new MonthDateParser("P0D"));
        durationMap.put("last month", new MonthDateParser("P30D"));
        durationMap.put("letzter monat", new MonthDateParser("P30D"));
        durationMap.put("last year", new YearDateParser("P365D"));
        durationMap.put("letztes jahr", new YearDateParser("P365D"));
        durationMap.put("this year", new YearDateParser("P0D"));
        durationMap.put("dieses jahr", new YearDateParser("P0D"));
    }

    public DateParser getDateParser() {
        if(dateParser == null) {
            dateParser = durationMap.get(humanDate.toLowerCase());
            if(dateParser == null) {
                dateParser = new DurationDateParser("P1D");
            }
            dateParser.setReference(now);
        }
        return dateParser;
    }

    public Instant getStart() {
        return getDateParser().getStart();
    }

    public Instant getEnd() {
        return getDateParser().getEnd();
    }

    public void setNow(Instant now) {
        this.now = now;
    }
}
