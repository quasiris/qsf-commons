package com.quasiris.qsf.commons.util;

import java.time.Instant;

/**
 * Created by tbl on 23.05.19.
 */
@Deprecated // TODO discuss this
public class QsfInstant {

    private static Instant now = null;


    /**
     * @return returns the current instant.
     */
    public static Instant now() {
        if(now == null) {
            return Instant.now();
        }
        return now;
    }

    /**
     * Set a specific instant for unit testing.
     * @param now the specific instant.
     */
    public static void setNow(Instant now) {
        QsfInstant.now = now;
    }




}
