package com.igp.reports.util.database;


public class DatabaseProperties {
    
    private static String DB_DRIVER = "";
    private static String RO_DB_HOST = "";
    private static String RO_DB_USER = "";
    private static String RO_DB_PASS = "";
    private static String RO_DB_PORT = null;
    private static String RO_DB_NAME = "";
    private static String RW_DB_HOST = "";
    private static String RW_DB_USER = "";
    private static String RW_DB_PASS = "";
    private static String RW_DB_PORT = null;
    private static String RW_DB_NAME = "";
    private static String DB_TYPE = "";
    private static Integer RO_CONNECTION_INITIAL_SIZE = null;
    private static Integer RO_CONNECTION_MIN_IDLE = null;
    private static Integer RO_CONNECTION_MAX_IDLE = null;
    private static Integer RO_CONNECTION_MAX_TOTAL = null;
    private static Integer RO_CONNECTION_MAX_WAIT_MILLIS = null;
    private static Integer RW_CONNECTION_INITIAL_SIZE = null;
    private static Integer RW_CONNECTION_MIN_IDLE = null;
    private static Integer RW_CONNECTION_MAX_IDLE = null;
    private static Integer RW_CONNECTION_MAX_TOTAL = null;
    private static Integer RW_CONNECTION_MAX_WAIT_MILLIS = null;

    public static String getDBDriver() {
        if (DB_DRIVER.equals("")) {
            DB_DRIVER = ServerProperties.getPropertyValue("DB_DRIVER");
            if (DB_DRIVER == null || DB_DRIVER.isEmpty()) {
                System.out.println("Please set up Driver for database driver configuration.");
            }
        }
        return DB_DRIVER;
    }

    public static String getDBType() {
        if (DB_TYPE.equals("")) {
            DB_TYPE = ServerProperties.getPropertyValue("DB_TYPE");
            if (DB_TYPE == null || DB_TYPE.isEmpty()) {
                System.out.println("Please set up url for database uri configuration.");
                DB_TYPE = "";
            }
        }
        return DB_TYPE;
    }

    public static String getDBHOST(Boolean readOnly) {
        if (readOnly) {
            if (RO_DB_HOST.equals("")) {
                RO_DB_HOST = ServerProperties.getPropertyValue("RO_DB_HOST");
                if (RO_DB_HOST == null || RO_DB_HOST.isEmpty()) {
                    System.out.println("Please set up url for database host configuration.");
                    RO_DB_HOST = "";
                }
            }
            return RO_DB_HOST;
        } else {
            if (RW_DB_HOST.equals("")) {
                RW_DB_HOST = ServerProperties.getPropertyValue("RW_DB_HOST");
                if (RW_DB_HOST == null || RW_DB_HOST.isEmpty()) {
                    System.out.println("Please set up url for database host configuration.");
                    RW_DB_HOST = "";
                }
            }
            return RW_DB_HOST;
        }
    }

    public static String getDBUser(Boolean readOnly) {
        if (readOnly) {
            if (RO_DB_USER.equals("")) {
                RO_DB_USER = ServerProperties.getPropertyValue("RO_DB_USER");
                if (RO_DB_USER == null || RO_DB_USER.isEmpty()) {
                    System.out.println("Please set up url for database user configuration.");
                    RO_DB_USER = "";
                }
            }
            return RO_DB_USER;
        } else {
            if (RW_DB_USER.equals("")) {
                RW_DB_USER = ServerProperties.getPropertyValue("RW_DB_USER");
                if (RW_DB_USER == null || RW_DB_USER.isEmpty()) {
                    System.out.println("Please set up url for database user configuration.");
                    RW_DB_USER = "";
                }
            }
            return RW_DB_USER;
        }
    }

    public static String getDBPass(Boolean readOnly) {
        if (readOnly) {
            if (RO_DB_PASS.equals("")) {
                RO_DB_PASS = ServerProperties.getPropertyValue("RO_DB_PASS");
                if (RO_DB_PASS == null || RO_DB_PASS.isEmpty()) {
                    System.out.println("Please set up url for database password configuration.");
                    RO_DB_PASS = "";
                }
            }
            return RO_DB_PASS;
        } else {
            if (RW_DB_PASS.equals("")) {
                RW_DB_PASS = ServerProperties.getPropertyValue("RW_DB_PASS");
                if (RW_DB_PASS == null || RW_DB_PASS.isEmpty()) {
                    System.out.println("Please set up url for database password configuration.");
                    RW_DB_PASS = "";
                }
            }
            return RW_DB_PASS;
        }
    }

    public static String getDBName(Boolean readOnly) {
        if (readOnly) {
            if (RO_DB_NAME.equals("")) {
                RO_DB_NAME = ServerProperties.getPropertyValue("RO_DB_NAME");
                if (RO_DB_NAME == null || RO_DB_NAME.isEmpty()) {
                    System.out.println("Please set up url for database name configuration.");
                    RO_DB_NAME = "";
                }
            }
            return RO_DB_NAME;
        } else {
            if (RW_DB_NAME.equals("")) {
                RW_DB_NAME = ServerProperties.getPropertyValue("RW_DB_NAME");
                if (RW_DB_NAME == null || RW_DB_NAME.isEmpty()) {
                    System.out.println("Please set up url for database name configuration.");
                    RW_DB_NAME = "";
                }
            }
            return RW_DB_NAME;
        }
    }

    public static String getDBPort(Boolean readOnly) {
        if (readOnly) {
            if (RO_DB_PORT == null || RO_DB_PORT.isEmpty()) {
                RO_DB_PORT = ServerProperties.getPropertyValue("RO_DB_PORT");
                if (RO_DB_PORT == null) {
                    System.out.println("Please set up url for database port configuration.");
                    RO_DB_PORT = null;
                }
            }
            return RO_DB_PORT;
        } else {
            if (RW_DB_PORT == null || RW_DB_PORT.isEmpty()) {
                RW_DB_PORT = ServerProperties.getPropertyValue("RW_DB_PORT");
                if (RW_DB_PORT == null) {
                    System.out.println("Please set up url for database port configuration.");
                    RW_DB_PORT = null;
                }
            }
            return RW_DB_PORT;
        }
    }

    public static Integer getConnectionInitialSize(Boolean readOnly) {
        if (readOnly) {
            if (RO_CONNECTION_INITIAL_SIZE == null) {
                RO_CONNECTION_INITIAL_SIZE = Integer.parseInt(ServerProperties.getPropertyValue("RO_CONNECTION_INITIAL_SIZE"));
                if (RO_CONNECTION_INITIAL_SIZE == null) {
                    RO_CONNECTION_INITIAL_SIZE = 3;
                }
            }
            return RO_CONNECTION_INITIAL_SIZE;
        } else {
            if (RW_CONNECTION_INITIAL_SIZE == null) {
                RW_CONNECTION_INITIAL_SIZE = Integer.parseInt(ServerProperties.getPropertyValue("RW_CONNECTION_INITIAL_SIZE"));
                if (RW_CONNECTION_INITIAL_SIZE == null) {
                    RW_CONNECTION_INITIAL_SIZE = 3;
                }
            }
            return RW_CONNECTION_INITIAL_SIZE;
        }
    }

    public static Integer getConnectionMinIdle(Boolean readOnly) {
        if (readOnly) {
            if (RO_CONNECTION_MIN_IDLE == null) {
                RO_CONNECTION_MIN_IDLE = Integer.parseInt(ServerProperties.getPropertyValue("RO_CONNECTION_MIN_IDLE"));
                if (RO_CONNECTION_MIN_IDLE == null) {
                    RO_CONNECTION_MIN_IDLE = 1;
                }
            }
            return RO_CONNECTION_MIN_IDLE;
        } else {
            if (RW_CONNECTION_MIN_IDLE == null) {
                RW_CONNECTION_MIN_IDLE = Integer.parseInt(ServerProperties.getPropertyValue("RW_CONNECTION_MIN_IDLE"));
                if (RW_CONNECTION_MIN_IDLE == null) {
                    RW_CONNECTION_MIN_IDLE = 1;
                }
            }
            return RW_CONNECTION_MIN_IDLE;
        }
    }

    public static Integer getConnectionMaxIdle(Boolean readOnly) {
        if (readOnly) {
            if (RO_CONNECTION_MAX_IDLE == null) {
                RO_CONNECTION_MAX_IDLE = Integer.parseInt(ServerProperties.getPropertyValue("RO_CONNECTION_MAX_IDLE"));
                if (RO_CONNECTION_MAX_IDLE == null) {
                    RO_CONNECTION_MAX_IDLE = 5;
                }
            }
            return RO_CONNECTION_MAX_IDLE;
        } else {
            if (RW_CONNECTION_MAX_IDLE == null) {
                RW_CONNECTION_MAX_IDLE = Integer.parseInt(ServerProperties.getPropertyValue("RW_CONNECTION_MAX_IDLE"));
                if (RW_CONNECTION_MAX_IDLE == null) {
                    RW_CONNECTION_MAX_IDLE = 1;
                }
            }
            return RW_CONNECTION_MAX_IDLE;
        }
    }

    public static Integer getConnectionMaxTotal(Boolean readOnly) {
        if (readOnly) {
            if (RO_CONNECTION_MAX_TOTAL == null) {
                RO_CONNECTION_MAX_TOTAL = Integer.parseInt(ServerProperties.getPropertyValue("RO_CONNECTION_MAX_TOTAL"));
                if (RO_CONNECTION_MAX_TOTAL == null) {
                    RO_CONNECTION_MAX_TOTAL = 5;
                }
            }
            return RO_CONNECTION_MAX_TOTAL;
        } else {
            if (RW_CONNECTION_MAX_TOTAL == null) {
                RW_CONNECTION_MAX_TOTAL = Integer.parseInt(ServerProperties.getPropertyValue("RW_CONNECTION_MAX_TOTAL"));
                if (RW_CONNECTION_MAX_TOTAL == null) {
                    RW_CONNECTION_MAX_TOTAL = 5;
                }
            }
            return RW_CONNECTION_MAX_TOTAL;
        }
    }

    public static Integer getConnectionMaxWaitMillis(Boolean readOnly) {
        if (readOnly) {
            if (RO_CONNECTION_MAX_WAIT_MILLIS == null) {
                RO_CONNECTION_MAX_WAIT_MILLIS = Integer.parseInt(ServerProperties.getPropertyValue("RO_CONNECTION_MAX_WAIT_MILLIS"));
                if (RO_CONNECTION_MAX_WAIT_MILLIS == null) {
                    RO_CONNECTION_MAX_WAIT_MILLIS = 1000;
                }
            }
            return RO_CONNECTION_MAX_WAIT_MILLIS;
        } else {
            if (RW_CONNECTION_MAX_WAIT_MILLIS == null) {
                RW_CONNECTION_MAX_WAIT_MILLIS = Integer.parseInt(ServerProperties.getPropertyValue("RW_CONNECTION_MAX_WAIT_MILLIS"));
                if (RW_CONNECTION_MAX_WAIT_MILLIS == null) {
                    RW_CONNECTION_MAX_WAIT_MILLIS = 1000;
                }
            }
            return RW_CONNECTION_MAX_WAIT_MILLIS;
        }
    }
}
