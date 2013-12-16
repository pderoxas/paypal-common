package com.paypal.common.utils;

import com.paypal.common.exceptions.ConfigException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * User: dev
 * Date: 3/1/13
 * Time: 4:01 PM
 *
 * <p>Utility methods related to handling Properties files
 *
 * <p>Use enum-based singleton pattern which has key advantages over traditional singleton pattern
 * <li>Easier to write</li>
 * <li>Serialization is guaranteed by the jvm</li>
 * <li>Enum is thread-safe; no need for double checked locking</li>
 *
 */
public enum PropertyManager {
    INSTANCE;
    private static Logger logger = Logger.getLogger(PropertyManager.class);
    private static Properties properties = null;
    public static final String DEFAULT_PROP_FILE_PATH = "paypal.properties.xml";  //by default, look on classpath for the file

    /**
     * Load the default properties file, "common-oflows.properties.xml"
     * @throws ConfigException
     */
    public void loadProperties() throws ConfigException {
        loadCustomProperties(DEFAULT_PROP_FILE_PATH);
    }

    /**
     * Initializes the properties file with an existing Properties instance.
     * Used for unit testing to be able to mock properties or use custom properties.
     */
    @VisibleForTesting
    public void loadCustomProperties(Properties props) throws ConfigException {
        properties = props;
    }

    /**
     * Initializes the PropertyManager instance with a given file.
     * This is public to allow loading of various properties file for testing purposes.
     */
    public void loadCustomProperties(String propertiesFileName) throws ConfigException {
        logger.debug("Attempting to load properties file: " + propertiesFileName);
        try {
            logger.debug("Loading properties file for the first time.");
            InputStream inputStream;

            // Allow the definition of the properties via command line (-DpropertiesFilename=/path/to/file")
            if (System.getProperty(propertiesFileName) != null) {
                logger.debug("Loading properties using System property: " + System.getProperty(propertiesFileName));
                try{
                    inputStream = new FileInputStream(System.getProperty(propertiesFileName));
                }
                catch (FileNotFoundException e){
                    throw new ConfigException("The properties file defined by System property (" + propertiesFileName + ") could not be found at " + System.getProperty(propertiesFileName));
                }
            }
            else {
                logger.debug("Loading properties from classpath");
                inputStream = PropertyManager.class.getClassLoader().getResourceAsStream(propertiesFileName);
                if (inputStream == null) {
                    throw new ConfigException("Properties file, " + propertiesFileName + ", could not be found on the classpath.");
                }
            }
            try {
                properties = new Properties();
                if (FilenameUtils.getExtension(propertiesFileName).equalsIgnoreCase("xml")) {
                    properties.loadFromXML(inputStream);
                }
                else {
                    //assume its a text based properties file
                    properties.load(inputStream);
                }
            }
            finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        catch (InvalidPropertiesFormatException e) {
            throw new ConfigException("InvalidPropertiesFormatException: Problem loading properties file: " + propertiesFileName + ". " + e.getMessage());
        }
        catch (IOException e) {
            throw new ConfigException("IOException: Problem opening properties file: " + propertiesFileName + ". " + e.getMessage());
        }
    }

    public String getProperty(String propertyName) {
        if(properties == null) {
            try {
                loadProperties();
            }
            catch (ConfigException e) {
                logger.error(e.getMessage());
            }
        }
        return properties.getProperty(propertyName);
    }

    /**
     * Allows us to override a loaded property if needed as in the case with unit testing
     * @param propertyName
     * @param propertyValue
     */
    public void setProperty(String propertyName, String propertyValue) {
        if(properties == null) {
            try {
                loadProperties();
            }
            catch (ConfigException e) {
                logger.error(e.getMessage());
            }
        }
        properties.setProperty(propertyName, propertyValue);
    }

    public String getProperty(String propertyName, String defaultValue) {
        if(properties == null) {
            try {
                loadProperties();
            }
            catch (ConfigException e) {
                logger.error(e.getMessage());
            }
        }
        return properties.getProperty(propertyName, defaultValue);
    }

    public boolean getBooleanProperty(String propertyName, boolean defaultValue) {
        if(properties == null) {
            try {
                loadProperties();
            }
            catch (ConfigException e) {
                logger.error(e.getMessage());
            }
        }
        String propValue = properties.getProperty(propertyName);
        if(propValue == null || propValue.isEmpty()){
            return defaultValue;
        }
        return Boolean.parseBoolean(propValue);
    }

    public boolean isLoaded(){
        return properties != null;
    }
}
