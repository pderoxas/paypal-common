package com.paypal.common.activities;

/**
 * User: dev
 * Date: 6/20/13
 * Time: 8:27 AM
 * POJO to encapsulate categorized messages in ActivityStatus.  It will allow us to filter on category for reporting
 * purposes while at the same time maintain insertion order.
 */
public class ActivityResultMessage {
    private ActivityResultMessageCategory category;
    private String message;

    public ActivityResultMessage(ActivityResultMessageCategory category, String message) {
        this.category = category;
        this.message = message;
    }

    public ActivityResultMessage(String message) {
        this.category = ActivityResultMessageCategory.DEFAULT; //default category to DEFAULT if not specified
        this.message = message;
    }

    public ActivityResultMessageCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityResultMessageCategory category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
