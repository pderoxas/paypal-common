package com.paypal.common.utils;

import java.io.File;
import java.util.List;

/**
 * Utility class for common file related methods
 * User: dev
 * Date: 5/22/13
 * Time: 4:27 PM
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * Recursively deletes files of a specified type withing a given folder.
     * @param directory - The root directory to start at
     * @param excludedDirectories - A list of any directory names to exclude
     * @param fileExt - The file extension type of files to delete
     */
    public static void deleteFiles(String directory, String fileExt, List<String> excludedDirectories){
        if(excludedDirectories.contains(directory)) return;    //skip any of the excludedDirectories specified
        File dir = new File(directory);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile() && file.getName().endsWith("." + fileExt)) {
                file.delete();
            } else if(file.isDirectory()) {
                deleteFiles(file.getAbsolutePath(), fileExt, excludedDirectories);
            }
        }
    }
}
