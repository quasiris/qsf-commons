package com.quasiris.qsf.commons.util;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class DateUtilTest {

    @Test
    public void getDateDifferentTimezoneFormat() throws Exception {
        Date date1 = DateUtil.getDate("2020-08-06T22:18:26.528+0000");
        Date date2 = DateUtil.getDate("2020-08-06T22:18:26.528+00:00");
        assertEquals(date1, date2);
    }

    @Test
    public void getDateWithoutTime() throws Exception {
        Date date = DateUtil.getDate("2020-08-06");
        assertNotNull(date);
    }

    @Test
    public void testNow() throws Exception {
        String date = DateUtil.now();
        assertNotNull(date);
    }
    @Test
    public void parseIsoDate() throws Exception {
        String date = DateUtil.now();
        assertNotNull(date);
    }

    @Test
    public void getDateAsString() throws Exception {
        Date date = DateUtil.getDate("2021-01-02T23:00:00Z");
        String dateString = DateUtil.getDate(date);
        assertNotNull(dateString);
    }
    @Test
    public void getDateAsStringNull() throws Exception {
        Date date = null;
        String dateString = DateUtil.getDate(date);
        assertNull(dateString);
    }
}