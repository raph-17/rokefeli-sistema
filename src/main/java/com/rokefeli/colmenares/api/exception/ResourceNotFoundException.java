package com.rokefeli.colmenares.api.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Object id) {
        super(String.format("%s with id '%s' not found", resource, id));
    }

    public ResourceNotFoundException(String resource) {
        super(String.format("%s not found", resource));
    }
}
