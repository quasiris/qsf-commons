package com.quasiris.qsf.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A util for converting dates.
 */
public class DateUtil {


    public static final String ELASTIC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    // 2019-09-16T20:08:28.275+0000
    public static final String ELASTIC_DATE_PATTERN_MICROSECONDS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * Creates a instant by a date string.
     * @param date the date as string.
     * @return the created instant.
     */
    public static Instant getInstantByString(String date) {
        OffsetDateTime ld = OffsetDateTime.parse(date);

        return ld.toInstant();
    }

    /**
     * Creates a instant by a elastic date string.
     * @param date the date as string.
     * @return the created instant.
     */
    public static Instant getInstantByElasticDate(String date) {
        OffsetDateTime ld = OffsetDateTime.parse(date, DateTimeFormatter.ofPattern(ELASTIC_DATE_PATTERN));
        return ld.toInstant();
    }

    public static Date getDateByElasticDateMicroseconds(String date) throws ParseException {
        return getDateByPattern(date, ELASTIC_DATE_PATTERN_MICROSECONDS);
    }

    public static Date getDateByElasticDate(String date) throws ParseException {
        return getDateByPattern(date, ELASTIC_DATE_PATTERN);
    }

    public static Date getDateByPattern(String date, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(date);
    }

    private static Map<Integer, String> patterns = new HashMap<>();
    static {
        patterns.put("2020-08-06".length(), "yyyy-MM-dd");
        patterns.put("2020-08-06T22:18:26+0000".length(), "yyyy-MM-dd'T'HH:mm:ssZ");
        patterns.put("2020-08-06T22:18:26.528+0000".length(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        patterns.put("2020-08-06T22:18:26.528+00:00".length(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    public static Date getDate(String date) throws ParseException {

        String pattern = patterns.get(date.length());
        if(pattern == null) {
            // try to parse it as a iso date
            return Date.from(Instant.parse(date));
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(date);
    }

    public static String getDate(Date date) {
        return getDate(date, ELASTIC_DATE_PATTERN_MICROSECONDS);
    }
    public static String getDate(Date date, String pattern) {
        if(date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static boolean isDateEqual(String date1, String date2) throws ParseException {
        Date d1 = getDate(date1);
        Date d2 = getDate(date2);
        return isDateEqual(d1, d2);
    }

    public static boolean isDateEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }
    public static String now() {
        return getDate(new Date());
    }

    // 0000-01-01
    public static Date min() {
        return new Date(-62167222408L);
    }

    // 9999-12-31
    public static Date max() {
        return new Date(253402210800000L);
    }

}
