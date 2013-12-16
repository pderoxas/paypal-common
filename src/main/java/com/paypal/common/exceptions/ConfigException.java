package com.paypal.common.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 2/14/13
 * Time: 9:57 AM
 * Implementation of Exception for Configuration Exceptions
 */
public class ConfigException extends Exception {
    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
