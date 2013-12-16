package com.paypal.common.dal;

import com.paypal.common.exceptions.ConfigException;
import com.paypal.common.exceptions.DalException;
import com.paypal.common.utils.PropertyManager;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: dev
 * Date: 4/3/13
 * Time: 11:51 AM
 *
 * <p>Singleton class to manage connections using BoneCP Connection Pool library
 *
 * <p>Use enum-based singleton pattern which has key advantages over traditional singleton pattern
 * <li>Easier to write</li>
 * <li>Serialization is guaranteed by the jvm</li>
 * <li>Enum is thread-safe; no need for double checked locking</li>
 */
public enum ConnectionManager {
    INSTANCE;
    private Logger logger = Logger.getLogger(ConnectionManager.class);
    private BoneCP connectionPool = null;
    private static final int PARTITION_COUNT = 1;
    private static final int MIN_CONNECTIONS_PER_PARTITION = 1;
    private static final int MAX_CONNECTIONS_PER_PARTITION = 10;
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int CONNECTION_RETRIES = 3;
    public final static String PROP_DB_DRIVER = "dbDriverName";
    public final static String PROP_DB_CONNECTION_URL = "dbConnectionUrl";
    public final static String PROP_DB_USERNAME = "dbUser";
    public final static String PROP_DB_PASSWORD = "dbPassword";


    private ConnectionManager(){
        initConnectionPool();
    }

    /**
     * Implementations must provide the required parameters to configure the connection pool
     * Some implementations may have these configured in properties files.  Others may choose to pass it in via another mode.
     */
    private void initConnectionPool() {
        try {
            ArrayList<String> missingProperties = new ArrayList<String>();

            String dbDriver = PropertyManager.INSTANCE.getProperty(PROP_DB_DRIVER);
            if(dbDriver == null || dbDriver.isEmpty())
            {
                missingProperties.add(PROP_DB_DRIVER);
            }

            String connectionUrl = PropertyManager.INSTANCE.getProperty(PROP_DB_CONNECTION_URL);
            if(connectionUrl == null || connectionUrl.isEmpty())
            {
                missingProperties.add(PROP_DB_CONNECTION_URL);
            }

            String dbUser = PropertyManager.INSTANCE.getProperty(PROP_DB_USERNAME);
            if(dbUser == null || dbUser.isEmpty())
            {
                missingProperties.add(PROP_DB_USERNAME);
            }

            String dbPassword = PropertyManager.INSTANCE.getProperty(PROP_DB_PASSWORD);
            if(dbPassword == null || dbPassword.isEmpty())
            {
                missingProperties.add(PROP_DB_PASSWORD);
            }

            if(!missingProperties.isEmpty()){
                String msg = "The following database properties are missing from the application properties file: " + missingProperties;
                logger.error(msg);
                throw new ConfigException(msg);
            }

            try {
                //logger.info("Attempting to load database driver: " + dbDriver);
                // Try to load the driver.  If it does not exist on the classpath, throw an exception right away
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                logger.error(e);
                throw e;
            }

            logger.info("Attempting to configure connection pool for url: " + connectionUrl);
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(connectionUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPassword);
            config.setMinConnectionsPerPartition(MIN_CONNECTIONS_PER_PARTITION);    //the minimum number on connections to have available per partition
            config.setMaxConnectionsPerPartition(MAX_CONNECTIONS_PER_PARTITION);    //the maximum number of connections to have available per partition
            config.setPartitionCount(PARTITION_COUNT);
            config.setConnectionTimeoutInMs(CONNECTION_TIMEOUT);
            config.setAcquireRetryAttempts(CONNECTION_RETRIES);

            config.setLazyInit(true);   //No connections will be made before they are needed
            connectionPool = new BoneCP(config); // setup the connection pool
            logger.info("Connection Pool for " + connectionUrl + " is initialized");
            logger.info("Total connections ==> " + connectionPool.getTotalCreatedConnections());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This method must be called only once when the application stops.
     * You don't need to call it every time when you get a connection from the Connection Pool
     */
    public void shutdownConnPool() {
        try {
            if (connectionPool != null) {
                connectionPool.shutdown();
                connectionPool = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This will get a thread-safe connection from the BoneCP connection pool.
     * Synchronization of the method will be done inside BoneCP source
     * @return Connection
     */
    public Connection getConnection() throws DalException {
        try {
            if(connectionPool == null){
                initConnectionPool();
            }
            logger.info("Attempting to get connection to: " + connectionPool.getConfig().getJdbcUrl());
            Connection conn = connectionPool.getConnection();
            logger.info("Total created connections in pool ==> " + connectionPool.getTotalCreatedConnections());
            logger.info("Total free connections in pool ==> " + connectionPool.getTotalFree());
            return conn;
        } catch (SQLException e) {
            logger.error("Unable to get a connection to: " + connectionPool.getConfig().getJdbcUrl());
            logger.error("SQLException: " + e.getMessage());
            logger.error("SQLState: " + e.getSQLState());
            logger.error("ErrorCode: " + e.getErrorCode());
            logger.error(e.getMessage(), e);
            throw new DalException("SQLException encountered in ConnectionManager: " + e.getMessage(), e);
        }
    }
}