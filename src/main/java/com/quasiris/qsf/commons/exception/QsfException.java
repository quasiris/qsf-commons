package com.quasiris.qsf.commons.exception;

/**
 * Base Qsf Exception
 */
public class QsfException extends Exception {
    public QsfException() {
        super();
    }

    public QsfException(String message) {
        super(message);
    }

    public QsfException(String message, Throwable cause) {
        super(message, cause);
    }

    public QsfException(Throwable cause) {
        super(cause);
    }

    protected QsfException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
