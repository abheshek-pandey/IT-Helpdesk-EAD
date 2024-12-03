package com.ithelpdesk.factory;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.dao.MySQLDatabase;
import com.ithelpdesk.util.ConfigLoader;

public class DatabaseFactory {
    public static DatabaseInterface getDatabase() {
        String environment = ConfigLoader.getConfig("db.environment").toLowerCase(); // local, aws_rds, aws_docdb
        String dbType = ConfigLoader.getConfig("db.type").toLowerCase(); // mysql, mongodb

        switch (environment) {
            case "local":
                if ("mysql".equals(dbType)) {
                    return new MySQLDatabase(
                        ConfigLoader.getConfig("mysql.local.url"),
                        ConfigLoader.getConfig("mysql.local.username"),
                        ConfigLoader.getConfig("mysql.local.password")
                    );
                } 
                break;

            case "aws_rds":
                if ("mysql".equals(dbType)) {
                    return new MySQLDatabase(
                        ConfigLoader.getConfig("mysql.aws.url"),
                        ConfigLoader.getConfig("mysql.aws.username"),
                        ConfigLoader.getConfig("mysql.aws.password")
                    );
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported environment: " + environment);
        }

        throw new IllegalArgumentException("Unsupported database configuration: " + environment + " / " + dbType);
    }
}
