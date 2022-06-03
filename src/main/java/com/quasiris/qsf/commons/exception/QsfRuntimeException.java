package com.quasiris.qsf.commons.exception;

/**
 * Base Qsf Runtime Exception
 */
public class QsfRuntimeException extends RuntimeException {
    public QsfRuntimeException() {
        super();
    }

    public QsfRuntimeException(String message) {
        super(message);
    }

    public QsfRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QsfRuntimeException(Throwable cause) {
        super(cause);
    }

    protected QsfRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
