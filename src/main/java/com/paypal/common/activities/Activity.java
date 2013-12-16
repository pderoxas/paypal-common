package com.paypal.common.activities;

import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 2/28/13
 * Time: 9:41 PM
 * Provides common logic for Validation Activities which can be extended
 */
public abstract class Activity {
    protected Logger logger = Logger.getLogger(this.getClass());
    protected StopWatch stopWatch = new Log4JStopWatch();
    protected ActivityResult activityResult = new ActivityResult();
    private ArrayList<Activity> subActivities = new ArrayList<Activity>();

    /**
     * Each subclass must implement this method which is used to check the validity of the current context
     *
     * @return List&lt;ActivityContextKey&gt;
     */
    protected abstract List<ActivityContextKey> getRequiredContextKeys();


    /**
     * Returns the list sub activities that should be performed as part of this activity
     *
     * @return ArrayList
     */
    public ArrayList<Activity> getSubActivities() {
        return this.subActivities;
    }

    /**
     * Returns the result of this activity
     *
     * @return ActivityResult
     */
    public ActivityResult getActivityResult() {
        return this.activityResult;
    }

    /**
     * Add a result message
     * @param message - the String message to add to the result
     */
    public void addActivityResultMessage(String message) {
        this.activityResult.addMessage(message);
    }

    /**
     * Add a result message
     * @param msgCategory - ActivityResultMessageCategory
     * @param message - the String message to add to the result
     */
    public void addActivityResultMessage(ActivityResultMessageCategory msgCategory, String message) {
        this.activityResult.addMessage(msgCategory, message);
    }

    /**
     * Add a result message at a given index
     * @param message - the String message to add to the result
     * @param atIndex - int
     */
    public void addActivityResultMessage(String message, int atIndex) {
        this.activityResult.addMessage(message, atIndex);
    }

    /**
     * Add a result message at a given index
     * @param message - the String message to add to the result
     * @return void
     */
    public void addActivityResultMessage(ActivityResultMessageCategory msgCategory, String message, int atIndex) {
        this.activityResult.addMessage(msgCategory, message, atIndex);
    }

    /**
     * Sets the status of this activity
     *
     * @return void
     */
    public void setActivityResultStatus(ActivityStatus status) {
        this.activityResult.setStatus(status);
    }

    /**
     * Add a result messages
     *
     * @return void
     */
    public void addActivityResultMessages(List<String> messages) {
        this.activityResult.addMessages(messages);
    }

    /**
     * Add a result messages
     *
     * @return void
     */
    public void addActivityResultMessages(ActivityResultMessageCategory msgCategory, List<String> messages) {
        this.activityResult.addMessages(msgCategory, new ArrayList<String>(messages));
    }

    /**
     * Add an activity to the list sub activities that should be performed as part of this activity
     *
     * @param subActivity - the activity to be added
     * @return boolean
     */
    public boolean addSubActivity(Activity subActivity) {
        if (subActivity.getClass().equals(this.getClass())) {
            logger.warn("Skip adding activity: " + this.getClass().getSimpleName() + " to it's own subActivity list.");
            return false;
        }
        else {
            return this.subActivities.add(subActivity);
        }
    }

    /**
     * Iterate through the sub-activities
     *
     * @param activityContext - The current activityContext
     */
    public void performSubActivities(ActivityContext activityContext) {
        for (Activity subActivity : this.subActivities) {
            try {
                logger.debug("Performing " + subActivity.getClass().getSimpleName() + " sub-activity.");
                subActivity.performActivity(activityContext);

            }
            catch (Exception e) {
                subActivity.setActivityResultStatus(ActivityStatus.UNEXPECTED_SYSTEM_ERROR);
                subActivity.addActivityResultMessage("An unexpected exception occurred while performing sub-activity " + this.getClass().getSimpleName());
                subActivity.addActivityResultMessage(e.getMessage());
                logger.fatal(e);
            }
        }
    }


    /**
     * Perform the logic for the activity.  This abstract method must be overridden!
     *
     * @param activityContext - ActivityContext object containing data required to perform the activity
     * @return ActivityResult
     */
    public abstract ActivityResult performActivity(ActivityContext activityContext) throws Exception;

    /**
     * Return the highest valued status of all the sub-activities.
     *
     * @return ActivityStatus
     */
    public final ActivityStatus getOverallSubActivityStatus() {
        //if there are no sub-activities, return SUCCESS
        if (this.getSubActivities().size() == 0) { return ActivityStatus.SUCCESS; }

        ActivityStatus overAllStatus = ActivityStatus.NOT_RUN;
        //iterate through the sub-activities and return the most severe status
        for (Activity subActivity : this.getSubActivities()) {
            if (subActivity.getActivityResult().getStatus().getValue() > overAllStatus.getValue()) {
                overAllStatus = subActivity.getActivityResult().getStatus();
            }
        }
        return overAllStatus;
    }
}
