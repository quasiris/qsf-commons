package com.quasiris.qsf.commons.exception;

/**
 * Thrown when a resource already exist
 */
public class ResourceAlreadyExistException extends QsfException {
    public ResourceAlreadyExistException() {
        super();
    }

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected ResourceAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
