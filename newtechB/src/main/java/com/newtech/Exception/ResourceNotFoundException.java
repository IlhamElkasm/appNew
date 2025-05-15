package com.newtech.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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


    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class resourcenotfoundexception extends RuntimeException {

        public resourcenotfoundexception(String message) {
            super(message);
        }
    }
}
