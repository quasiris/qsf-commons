package com.quasiris.qsf.commons.text.date;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HumanDateParser {

    public HumanDateParser(String humanDate) {
        this.humanDate = humanDate;
    }

    private String humanDate;

    private Instant now = Instant.now();

    private DateParser dateParser;

    private static Map<String, DateParser> durationMap = new HashMap<>();
    static {
        durationMap.put("vergangenheit", new PastParser());
        durationMap.put("past", new PastParser());
        durationMap.put("zukunft", new FutureParser());
        durationMap.put("future", new FutureParser());
        durationMap.put("last day", new DurationDateParser("P1D"));
        durationMap.put("letzter tag", new DurationDateParser("P1D"));
        durationMap.put("yesterday", new DayDateParser("P1D"));
        durationMap.put("gestern", new DayDateParser("P1D"));
        durationMap.put("today", new DayDateParser("P0D"));
        durationMap.put("heute", new DayDateParser("P0D"));
        durationMap.put("last 3 days", new DurationDateParser("P3D"));
        durationMap.put("letzten 3 tage", new DurationDateParser("P3D"));
        durationMap.put("letzte 3 tage", new DurationDateParser("P3D"));
        durationMap.put("letzten 7 tage", new DurationDateParser("P7D"));
        durationMap.put("letzte 7 tage", new DurationDateParser("P7D"));
        durationMap.put("letzten 30 tage", new DurationDateParser("P30D"));
        durationMap.put("this week", new WeekDateParser("P0D"));
        durationMap.put("diese woche", new WeekDateParser("P0D"));
        durationMap.put("last week", new WeekDateParser("P7D"));
        durationMap.put("letzte woche", new WeekDateParser("P7D"));
        durationMap.put("last 3 month", new PeriodDateParser("P3M"));
        durationMap.put("letzten 3 monate", new PeriodDateParser("P3M"));
        durationMap.put("letzten 6 monate", new PeriodDateParser("P6M"));
        durationMap.put("this month", new MonthDateParser("P0D"));
        durationMap.put("dieser monat", new MonthDateParser("P0D"));
        durationMap.put("last month", new MonthDateParser("P30D"));
        durationMap.put("letzter monat", new MonthDateParser("P30D"));
        durationMap.put("last year", new YearDateParser("P365D"));
        durationMap.put("letztes jahr", new YearDateParser("P365D"));
        durationMap.put("this year", new YearDateParser("P0D"));
        durationMap.put("dieses jahr", new YearDateParser("P0D"));
    }

    private static Map<String, String> durationTypenMap = new HashMap<>();
    static {
        durationTypenMap.put("tage", "D");
        durationTypenMap.put("days", "D");
        durationTypenMap.put("month", "M");
        durationTypenMap.put("months", "M");
        durationTypenMap.put("monat", "M");
        durationTypenMap.put("monate", "M");
    }

    public DateParser getDateParser() {
        if(dateParser == null) {
            dateParser = durationMap.get(humanDate.toLowerCase());
            if(dateParser == null) {
                dateParser = getDateParserByRegex();
            }
            if (dateParser == null) {
                dateParser = new DurationDateParser("P1D");
            }
            dateParser.setReference(now);
        }
        return dateParser;
    }

    public DateParser getDateParserByRegex() {
        if(humanDate.matches("^\\d{4}$")) {
            return new YearParser(humanDate);
        }

        String[] splitted = humanDate.toLowerCase().split(" ");
        if(splitted.length == 3) {
            String number = splitted[1];
            String type = durationTypenMap.get(splitted[2]);
            if(type != null) {
                if("D". equals(type)) {
                    return new DurationDateParser("P" + number + "D");
                }
                if("M". equals(type)) {
                    return new PeriodDateParser("P" + number + "M");
                }
            }
        }
        return null;
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
