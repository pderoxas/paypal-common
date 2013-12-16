package com.paypal.common.activities;

import com.paypal.common.exceptions.ActivityException;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 2/25/13
 * Time: 11:16 AM
 * ActivityContext is a Map instance to store data associated to an Activity.
 * It is intended to be a loosely defined Map which provides the flexibility of defining various types of data required
 */
public class ActivityContext implements Serializable{
    private static Logger logger = Logger.getLogger(ActivityContext.class);
    private HashMap<ActivityContextKey, Object> context;

    /**
     * Constructor - initializes an empty HashMap object
     */
    public ActivityContext() {
        this.context = new HashMap<ActivityContextKey, Object>();
    }

    /**
     * Constructor - sets the context Map to an incoming Map implementation
     *
     * @param contextMap - a Map implementation object
     */
    public ActivityContext(HashMap<ActivityContextKey, Object> contextMap) {
        this.context = contextMap;
    }

    /**
     * Add key/value pair to the Activity Context and overwrite if existing
     *
     * @param key   - the key for the context data
     * @param value - the value for the context data
     */
    public void addContext(ActivityContextKey key, Object value) {
        this.addContext(key, value, true);
    }

    /**
     * Add key/value pair to the Activity Context
     *
     * @param key   - the key for the context data
     * @param value - the value for the context data
     * @param overwrite - if true, will overwrite existing value. if false, will not overwrite existing value.
     */
    public void addContext(ActivityContextKey key, Object value, boolean overwrite) {
        //if overwrite is true, put the key
        if(overwrite){
            this.context.put(key, value);
        }
        //only put if the key does not exist
        else if(!this.contextExistsAndNotNull(key)){
            this.context.put(key, value);
        }
    }

    /**
     * Removes context by key
     *
     * @param key   - the key for the context data
     */
    public void removeContext(ActivityContextKey key) {
        this.context.remove(key);
    }

    /**
     * Get the value associated to the Activity Context for the passed key
     *
     * @param contextKey - the key for a context value
     * @return Object - the corresponding value.  It will need to be cast to the appropriate type
     */
    public Object get(ActivityContextKey contextKey) {
        if (!contextExists(contextKey)) return null;
        return this.context.get(contextKey);
    }

    /**
     * Get the value associated to the Activity Context for the passed key
     *
     * @param contextKey - the key for a context value
     * @return Object - the corresponding value.  It will need to be cast to the appropriate type
     */
    public Object get(ActivityContextKey contextKey, ActivityResult activityResult) {
        if (!contextExistsAndNotNull(contextKey, activityResult)) return null;
        return this.context.get(contextKey);
    }

    /**
     * Get the value associated to the Activity Context for the passed key
     * Type casting will be done if the value is indeed the same type as passed
     *
     * @param contextKey - the key for a context value
     * @param type       - the expected type of the value
     * @param <T>        - the generic type to be returned
     * @return T - a concrete instance of class T
     * @throws ActivityException
     */
    public <T> T get(ActivityContextKey contextKey, Class<T> type) throws ActivityException {
        Object value = this.get(contextKey);
        if (value == null) {
            return null;
        } else if (value.getClass().equals(type)) {
            return type.cast(value);
        } else {
            throw new ActivityException("Attempted to get value for key: " + contextKey + " of type: " + type.getName() + " but the value is actually of type: " + value.getClass().getName());
        }
    }

    /**
     * Returns whether or not the Activity Context contains a given key
     *
     * @param contextKey - the key for a potential context value
     * @return - boolean
     */
    public boolean contextExists(ActivityContextKey contextKey) {
        return this.context.containsKey(contextKey);
    }

    /**
     * Returns whether or not the Activity Context contains a given key and the value is not null
     *
     * @param contextKey - the key for a potential context value
     * @return - boolean
     */
    public boolean contextExistsAndNotNull(ActivityContextKey contextKey) {
        return (this.contextExists(contextKey) && this.get(contextKey) != null);
    }

    /**
     * Helper function to check if a context key exists.  If not, it will add a message to the activityResult.
     * @param activityResult - the current activityResult instance
     * @param contextKey - the ActivityContextKey to check
     * @return boolean
     */
    public boolean contextExistsAndNotNull(ActivityContextKey contextKey, ActivityResult activityResult){
        if (!this.contextExistsAndNotNull(contextKey)) {
            String msg = "'" + contextKey.getDescription() + "' does not exist in the current context.";
            logger.debug(msg);
            activityResult.addMessage(ActivityResultMessageCategory.ERROR, msg);
            return false;
        }
        return true;
    }
}
