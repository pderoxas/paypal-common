package com.paypal.common.activities;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 3/6/13
 * Time: 9:40 AM
 * Class to be used as keys in ActivityContext - It forces users to have a public, reusable key rather than open-ended strings.
 */
public class ActivityContextKey implements Serializable{
    private String description;

    public ActivityContextKey(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
