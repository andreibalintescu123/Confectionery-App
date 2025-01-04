package org.confectionery.Exception;

public class FailedLoginAttempt extends RuntimeException {
    public FailedLoginAttempt(String message) {
        super(message);
    }
}
