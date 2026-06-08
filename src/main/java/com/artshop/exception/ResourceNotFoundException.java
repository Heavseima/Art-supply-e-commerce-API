package com.artshop.exception;

/**
 * Thrown when a requested domain resource cannot be located.
 * Mapped to HTTP 404 by {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object identifier) {
        super("%s not found: %s".formatted(resource, identifier));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
