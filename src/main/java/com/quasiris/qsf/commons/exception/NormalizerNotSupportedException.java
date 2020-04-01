package com.quasiris.qsf.commons.exception;

/**
 * Thrown if passed normalizer is missing in profiles or not supported
 */
public class NormalizerNotSupportedException extends QsfException {
    public NormalizerNotSupportedException() {
        super();
    }

    public NormalizerNotSupportedException(String message) {
        super(message);
    }

    public NormalizerNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NormalizerNotSupportedException(Throwable cause) {
        super(cause);
    }

    protected NormalizerNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
