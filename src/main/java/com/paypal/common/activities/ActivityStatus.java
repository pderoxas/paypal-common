package com.paypal.common.activities;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 2/15/13
 * Time: 10:58 AM
 * Enumeration of possible execution status
 */
public enum ActivityStatus {
    NOT_RUN(0),
    IN_PROCESS(10),
    SKIPPED(11),
    FILE_NOT_FOUND(12),
    DIRECTORY_NOT_FOUND(13),
    SUCCESS(20),
    VALIDATION_ERROR(40),
    UNEXPECTED_SYSTEM_ERROR(50);

    private int value;

    private ActivityStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
