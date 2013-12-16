package com.paypal.common.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 2/20/13
 * Time: 4:23 PM
 * Implementation of Exception for Parser Exceptions
 */
public class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }


}
