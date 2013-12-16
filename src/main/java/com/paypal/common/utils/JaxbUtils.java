package com.paypal.common.utils;

import com.paypal.common.exceptions.ConfigException;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Utility class related to JaxB
 * User: dev
 * Date: 4/29/13
 * Time: 1:24 PM
 */
public class JaxbUtils {
    static Logger logger = Logger.getLogger(JaxbUtils.class);
    /**
     * This method return an instance of CsvFileDefinition given a path to the configuration file.
     * @param inputStream - an inputstream to the file
     * @return Object of type T
     */

    //    public <T> T get(ActivityContextKey contextKey, Class<T> type) throws ActivityException {
    public static <T> T unmarsal(InputStream inputStream, Class<T> type) throws ConfigException {
        T obj;
        try {
            if(inputStream == null){
                StringBuilder msg = new StringBuilder("The InputStream parameter is null");
                logger.error(msg);
                throw new FileNotFoundException(msg.toString());
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            obj = (T) unmarshaller.unmarshal(inputStream);
        }

        catch (JAXBException e) {
            StringBuilder msg = new StringBuilder("JAXBException occurred while attempting to unmarshal the xml file");
            msg.append(e.getMessage());
            logger.error(e.getMessage(), e);
            throw new ConfigException(msg.toString(), e);
        }
        catch(Exception e){
            StringBuilder msg = new StringBuilder("Exception occurred while attempting to unmarshal the xml file");
            msg.append(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ConfigException(msg.toString(), e);
        }
        return obj;
    }

    /**
     * This method return an instance of CsvFileDefinition given a path to the configuration file.
     * @param filePath - the path to the xml file (configuration file)
     * @return Object of type T
     */

    //    public <T> T get(ActivityContextKey contextKey, Class<T> type) throws ActivityException {
    public static <T> T unmarsal(String filePath, Class<T> type) throws ConfigException {
        T obj;
        try {
            File xmlFile = new File(filePath);
            if(!xmlFile.exists()){
                StringBuilder msg = new StringBuilder("The expected xml file does not exist at the configured location: ").append(filePath);
                logger.error(msg);
                throw new FileNotFoundException(msg.toString());
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            obj = (T) unmarshaller.unmarshal(xmlFile);
        }
        catch (FileNotFoundException e) {
            StringBuilder msg = new StringBuilder("Unable to find file at: ").append(filePath);
            msg.append(e.getMessage());
            logger.error(e.getMessage(), e);
            throw new ConfigException(msg.toString(), e);
        }
        catch (JAXBException e) {
            StringBuilder msg = new StringBuilder("JAXBException occurred while attempting to unmarshal the xml file: ").append(filePath);
            msg.append(e.getMessage());
            logger.error(e.getMessage(), e);
            throw new ConfigException(msg.toString(), e);
        }
        catch(Exception e){
            StringBuilder msg = new StringBuilder("Exception occurred while attempting to unmarshal the xml file: ").append(filePath);
            msg.append(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ConfigException(msg.toString(), e);
        }
        return obj;
    }
}
