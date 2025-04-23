package com.newtech.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Optional: Constructor with cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Optional: Constructor with resource name and ID
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
