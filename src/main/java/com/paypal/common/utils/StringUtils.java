package com.paypal.common.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 3/1/13
 * Time: 4:43 PM
 * Common string utilities
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    /**
     * Constructs a file path from the individual parts using the File system separator char
     *
     * @param parts - the individual components that make up the file path
     * @return String - the file path
     */
    public static String constructFilePath(String[] parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part).append(File.separator);
        }
        return FilenameUtils.normalizeNoEndSeparator(sb.toString());
    }

    /**
     * Returns a given set of file path parts from the end (tail)
     *
     * @param filePath - the original file path
     * @return String - the truncated file path
     */
    public static String getFilePathTail(String filePath, int numberOfSegments) {
        if(StringUtils.isBlank(filePath)) return filePath;
        String[] segments = filePath.split(File.separator);

        if(segments.length <= numberOfSegments) return filePath;

        StringBuilder newFilePath = new StringBuilder();
        int start = segments.length - numberOfSegments;
        for(int i = start; i < segments.length; i++){
            newFilePath.append(File.separator).append(segments[i]);
        }
        return FilenameUtils.normalizeNoEndSeparator(newFilePath.toString());
    }

    /**
     * Helper method to check if string value is susceptible to sql injection
     * It will check that the value only contains alphanumerics, -, and _. No spaces.
     * Externalized to it's own method to allow for ease of unit testing and/or modification of regex exp if needed.
     * @param value
     * @return
     */
    public static boolean isAlphaNumeric(String value){
        if (value == null) return true;
        return value.matches("^[a-zA-Z0-9-_]+$");
    }
}
