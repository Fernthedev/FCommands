package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;

public class UniversalMysql {

    private static DatabaseListener databaseManager;

    private UniversalMysql() {
    }

    public static DatabaseListener getDatabaseManager() {
        return UniversalMysql.databaseManager;
    }

    public static void setDatabaseManager(DatabaseListener databaseManager) {
        UniversalMysql.databaseManager = databaseManager;
    }
}
