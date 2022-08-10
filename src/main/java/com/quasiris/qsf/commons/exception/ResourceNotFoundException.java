package com.quasiris.qsf.commons.exception;

import java.io.IOException;

/**
 * Thrown when a resource was not found
 */
public class ResourceNotFoundException extends IOException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
