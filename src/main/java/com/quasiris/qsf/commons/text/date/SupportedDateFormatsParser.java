package com.quasiris.qsf.commons.text.date;

import com.quasiris.qsf.commons.util.DateUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportedDateFormatsParser {

    private static final Map<Integer, List<Format>> supportedFormats = new HashMap<>();

    private static final ZoneId zoneId = ZoneId.of("Europe/Berlin");
    private static final ZoneId utcZoneId = ZoneId.of("UTC");
    private static final DateTimeFormatter elasticsearchFormatter = DateTimeFormatter.ofPattern(DateUtil.ELASTIC_DATE_PATTERN_MICROSECONDS)
            .withZone(utcZoneId);

    private static final DateTimeFormatter defaultFormatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
            .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
            .optionalStart().appendOffset("+HH", "+00").optionalEnd()
            .optionalStart().appendPattern("XX").optionalEnd()
            .toFormatter()
            .withZone(zoneId);

    static {

        List<Format> allFormats = new ArrayList<>(List.of(
                new Format() {{
                    size = "2020-08-06".length();
                    replaceable = false;
                    formatter = new DateTimeFormatterBuilder()
                            .appendPattern("yyyy-MM-dd")
                            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
                            .toFormatter()
                            .withZone(zoneId);
                }}
        ));


        for (Format format : allFormats) {
            List<Format> formats = supportedFormats.computeIfAbsent(format.size, k -> new ArrayList<>());
            formats.add(format);
        }
    }

    static class Format {
        public int size;
        public DateTimeFormatter formatter;
        public boolean replaceable = true;
    }


    private final String inputDate;
    private String outputDate;

    public SupportedDateFormatsParser(String inputDate) {
        this.inputDate = inputDate;
    }

    public boolean parse() {
        List<Format> formats = supportedFormats.get(inputDate.length());
        if (formats == null) {
            try {
                Instant instant = defaultFormatter.parse(inputDate, Instant::from);
                outputDate = createElasticsearchDate(instant);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        for (Format format : formats) {
            try {
                Instant instant = format.formatter.parse(inputDate, Instant::from);
                if (format.replaceable) {
                    outputDate = createElasticsearchDate(instant);
                } else {
                    outputDate = inputDate;
                }
                return true;
            } catch (Exception ignore) {
            }
        }


        return false;
    }

    private String createElasticsearchDate(Instant instant) {
        return elasticsearchFormatter.format(instant);
    }

    public String getOutputDate() {
        return outputDate;
    }
}
