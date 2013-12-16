package com.paypal.common.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 2/25/13
 * Time: 11:43 AM
 * Implementation of Exception specific to Activity-related classes
 */
public class ActivityException extends Exception {
    public ActivityException(String message) {
        super(message);
    }

    public ActivityException(String message, Throwable cause) {
        super(message, cause);
    }
}
