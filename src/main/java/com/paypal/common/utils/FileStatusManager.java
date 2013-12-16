package com.paypal.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * The purpose of this class is to provide a mechanism to manage the processing
 * status of a particular file.  The basic workflow is as follows:
 * 1) Check if a target file has a corresponding ".common-stat" file.
 *      A) If an ".common-stat" file DOES exists, this indicates that the target file has been successfully
 *         processed in some way in the past.  If the last modified date of the target file is more recent
 *         than the ".common-stat" last modified date, process the target file. (see step 2)
 *      B) If an ".common-stat" file DOES NOT exist, process the target file. (see step 2)
 * 2) If applicable, process the target file.
 *      A) If processed successfully, create a ".common-stat" file. (Or update the last modified date if one exists)
 *         The naming convention for the ".common-stat" file will be
 *         [Target File Name].common-stat - ex. myFile.xls -> myFile.xls.common-stat
 *      B) If processing fails, do not create/overwrite the ".common-stat" file. This will indicate
 *         that the file can be reprocessed.
 * <p/>
 * User: dev
 * Date: 5/22/13
 * Time: 10:31 AM
 */
public class FileStatusManager {
    private static Logger logger = Logger.getLogger(FileStatusManager.class);
    public static final String STAT_FILE_EXT = "common-stat";

    /**
     * Possible status for a target file
     * ELIGIBLE_TO_PROCESS      - indicates that the target file is eligible for processing
     * NOT_ELIGIBLE_TO_PROCESS  - indicates that the target file is not eligible for processing
     */
    public enum FileStatus {
        ELIGIBLE_TO_PROCESS,
        NOT_ELIGIBLE_TO_PROCESS,
    }

    /**
     * Will create or overwrite the .common-stat file for the target file
     *
     * @param targetFile - The file we want to perform some activity on
     * @return boolean
     */
    public static boolean createStatFile(File targetFile) throws IOException {
        try {
            if(targetFile == null){
                logger.info("common-stat file was not created because targetFile is null");
                return false;
            }
            if(!targetFile.exists()){
                logger.info("common-stat file was not created because targetFile does not exist: " + targetFile.getAbsolutePath());
                return false;
            }
            if(!targetFile.isFile()){
                logger.info("common-stat file was not created because targetFile is not a file: " + targetFile.getAbsolutePath());
                return false;
            }
            // create File object
            String statFileName = targetFile.getName() + "." + STAT_FILE_EXT;
            File statFile = new File(FilenameUtils.concat(targetFile.getParent(), statFileName));
            if (statFile.exists()) {
                return statFile.setLastModified(System.currentTimeMillis());
            }
            else {
                return statFile.createNewFile();
            }
        }
        catch (IOException e) {
            logger.error("Error while creating common-stat file." + e);
            throw e;
        }
    }

    /**
     * Will delete the .common-stat file for the target file if it exists
     *
     * @param targetFile - The file we want to perform some activity on
     * @return boolean
     */
    public static boolean deleteStatFile(File targetFile) {
        // create File object
        String statFileName = targetFile.getName() + "." + STAT_FILE_EXT;
        File statFile = new File(FilenameUtils.concat(targetFile.getParent(), statFileName));
        return !statFile.exists() || statFile.delete();
    }


    /**
     * This method will return a given file's status based on its last modified date and/or
     * the existence of an .common-stat file and it's last modified date
     *
     * @param targetFile - The file we want to perform some activity on
     * @return FileStatus
     */
    public static FileStatus getFileStatus(File targetFile) throws Exception {
        try {
            //Check if an existing .common-stat file exists as a sibling to the target file
            //Return ELIGIBLE_TO_PROCESS IF...
            //1) the common-stat file does not exist
            //2) the target file is "newer" than the common-stat file
            String statFileName = targetFile.getName() + "." + STAT_FILE_EXT;
            String dirName = targetFile.getParent();
            String statFilePath = FilenameUtils.concat(dirName, statFileName);
            File statFile = new File(statFilePath);

            if (!statFile.isFile()) {
                logger.debug("statFile does not exist");
                return FileStatus.ELIGIBLE_TO_PROCESS;
            }

            logger.debug("targetFile lastModified: " + targetFile.lastModified());
            logger.debug("statFile lastModified: " + statFile.lastModified());
            if (targetFile.lastModified() - statFile.lastModified() > 0) {
                return FileStatus.ELIGIBLE_TO_PROCESS;
            }

            //otherwise, the file is not eligible for processing
            return FileStatus.NOT_ELIGIBLE_TO_PROCESS;
        }
        catch (Exception e) {
            StringBuilder msg = new StringBuilder();
            if(targetFile != null){
                msg.append("Error while getting status of ").append(targetFile.getName()).append(" - ");
            } else {
                msg.append("targetFile parameter is null - ");
            }
            msg.append(e.getMessage());
            logger.error(msg);
            throw new Exception(msg.toString(), e);
        }
    }
}
