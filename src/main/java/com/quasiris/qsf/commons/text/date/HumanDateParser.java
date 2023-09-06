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
        durationMap.put("last day", new PeriodDateParser("P1D"));
        durationMap.put("letzter tag", new PeriodDateParser("P1D"));
        durationMap.put("yesterday", new DayDateParser("P1D"));
        durationMap.put("gestern", new DayDateParser("P1D"));
        durationMap.put("today", new DayDateParser("P0D"));
        durationMap.put("heute", new DayDateParser("P0D"));
        durationMap.put("this week", new PeriodDateParser("P0D", ChronoUnit.WEEKS));
        durationMap.put("diese woche", new PeriodDateParser("P0D", ChronoUnit.WEEKS));
        durationMap.put("last week", new PeriodDateParser("P7D", ChronoUnit.WEEKS));
        durationMap.put("letzte woche", new PeriodDateParser("P7D", ChronoUnit.WEEKS));
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
        durationTypenMap.put("minuten", "m");
        durationTypenMap.put("minute", "m");
        durationTypenMap.put("minutes", "m");
        durationTypenMap.put("stunden", "H");
        durationTypenMap.put("stunde", "H");
        durationTypenMap.put("hour", "H");
        durationTypenMap.put("hours", "H");
        durationTypenMap.put("tage", "D");
        durationTypenMap.put("days", "D");
        durationTypenMap.put("month", "M");
        durationTypenMap.put("months", "M");
        durationTypenMap.put("monat", "M");
        durationTypenMap.put("monate", "M");
        durationTypenMap.put("woche", "W");
        durationTypenMap.put("wochen", "W");
        durationTypenMap.put("week", "W");
        durationTypenMap.put("weeks", "W");
    }

    public DateParser getDateParser() {
        if(dateParser == null) {
            dateParser = durationMap.get(humanDate.toLowerCase());
            if(dateParser == null) {
                dateParser = getDateParserByRegex();
            }
            if (dateParser == null) {
                dateParser = new PeriodDateParser("P1D");
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
                if("m". equals(type)) {
                    DurationDateParser durationDateParser = new DurationDateParser("PT" + number + "M", ChronoUnit.MINUTES);
                    return durationDateParser;
                }
                if("H". equals(type)) {
                    DurationDateParser durationDateParser = new DurationDateParser("PT" + number + "H", ChronoUnit.HOURS);
                    return durationDateParser;
                }
                if("D". equals(type)) {
                    PeriodDateParser periodDateParser = new PeriodDateParser("P" + number + "D");
                    periodDateParser.setTruncateTo(ChronoUnit.DAYS);
                    return periodDateParser;
                }
                if("M". equals(type)) {
                    PeriodDateParser periodDateParser = new PeriodDateParser("P" + number + "M");
                    periodDateParser.setTruncateTo(ChronoUnit.MONTHS);
                    return periodDateParser;
                }
                if("W". equals(type)) {
                    PeriodDateParser periodDateParser = new PeriodDateParser("P" + number + "W");
                    periodDateParser.setTruncateTo(ChronoUnit.WEEKS);
                    return periodDateParser;
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
