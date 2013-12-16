package com.paypal.common.activities;

import java.util.*;

/**
 * User: dev
 * Date: 2/15/13
 * Time: 12:06 AM
 * POJO (plain old java object) that is returned by all implementations of the Activity interface.
 */
public class ActivityResult {
    private ActivityStatus status;
    private final List<ActivityResultMessage> resultMessages;

    /**
     * Initialize the status to NOT_RUN
     * Initialize an empty ArrayList for messages
     */
    public ActivityResult() {
        this.status = ActivityStatus.NOT_RUN;
        this.resultMessages = new ArrayList<ActivityResultMessage>();
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    /**
     * Returns all messages regardless of category
     * @return List<String>
     */
    public List<String> getMessages() {
        ArrayList<String> msgs = new ArrayList<String>();
        for(ActivityResultMessage resultMsg : resultMessages){
            msgs.add(resultMsg.getMessage());
        }
        return msgs;
    }

    /**
     * Return messages of a specified category
     * @param msgCategory
     * @return
     */
    public List<String> getMessages(ActivityResultMessageCategory msgCategory) {
        ArrayList<String> msgs = new ArrayList<String>();
        for(ActivityResultMessage resultMsg : resultMessages){
            if(resultMsg.getCategory().equals(msgCategory)){
                msgs.add(resultMsg.getMessage());
            }
        }
        return msgs;
    }

    /**
     * Return messages where category is at least the specified category (inclusive)
     * For example, if minMsgCategory is INFO, messages of INFO, WARNING, ERROR will be returned
     * @param minMsgCategory - the minimum message category
     * @return List<String>
     */
    public List<String> getMessagesMinCategory(ActivityResultMessageCategory minMsgCategory) {
        ArrayList<String> msgs = new ArrayList<String>();
        for(ActivityResultMessage resultMsg : resultMessages){
            if(resultMsg.getCategory().getValue() >= minMsgCategory.getValue()){
                msgs.add(resultMsg.getMessage());
            }
        }
        return msgs;
    }

    /**
     * Add message using default category
     * @param msg
     */
    public void addMessage(String msg) {
        this.resultMessages.add(new ActivityResultMessage(msg));
    }

    /**
     * Add message using specified category
     * @param msg
     */
    public void addMessage(ActivityResultMessageCategory msgCategory, String msg) {
        this.resultMessages.add(new ActivityResultMessage(msgCategory, msg));
    }

    /**
     * Add message using default category at specified index
     * @param msg
     */
    public void addMessage(String msg, int atIndex) {
        this.resultMessages.add(atIndex, new ActivityResultMessage(msg));
    }

    /**
     * Add message using specified category at specified index
     * @param msg
     */
    public void addMessage(ActivityResultMessageCategory msgCategory, String msg, int atIndex) {
        this.resultMessages.add(atIndex, new ActivityResultMessage(msgCategory, msg));
    }

    /**
     * Method to maintain backwards compatibility - maintains original order
     * @param msgs
     */
    public void addMessages(List<String> msgs) {
        for(String msg : msgs){
            addMessage(msg);
        }
    }

    /**
     * Add existing of ActivityResultMessage list to result messages list
     * @param msgs
     */
    public void addCategorizedMessages(List<ActivityResultMessage> msgs) {
        this.resultMessages.addAll(msgs);
    }

    /**
     * Add a list of strings to the results messages using the same message category for all messages
     * @param msgCategory
     * @param msgs
     */
    public void addMessages(ActivityResultMessageCategory msgCategory, List<String> msgs) {
        for(String msg : msgs){
            addMessage(msgCategory, msg);
        }
    }

    /**
     * Clear all messages from the list
     */
    public void clearMessages() {
        this.resultMessages.clear();
    }
}


