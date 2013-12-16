package com.paypal.common.activities;

/**
 * User: dev
 * Date: 6/20/13
 * Time: 8:43 AM
 * Categories for activity messages.  This will allow us to filter during reporting while maintaining insertion order.
 */
public enum ActivityResultMessageCategory {
    DEFAULT (0),
    INFO (10),
    WARNING (20),
    ERROR (30);

    private int value;

    private ActivityResultMessageCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
