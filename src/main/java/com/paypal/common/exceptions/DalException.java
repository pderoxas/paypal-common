package com.paypal.common.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 2/14/13
 * Time: 9:57 AM
 * Implementation of Exception for Configuration Exceptions
 */
public class DalException extends Exception {
    public DalException(String message) {
        super(message);
    }

    public DalException(String message, Throwable cause) {
        super(message, cause);
    }
}
