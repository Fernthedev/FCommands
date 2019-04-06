package com.github.fernthedev.fcommands.bungeeclass;

import com.github.fernthedev.fcommands.bungeeclass.files.ConfigManager;
import com.github.fernthedev.fcommands.bungeeclass.files.ConfigValues;
import com.github.fernthedev.fernapi.universal.Universal;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {


    private static String url = ""; //Enter URL w/db name
    private static boolean scheduled;

    private static ConfigValues configValues;

    private static TaskScheduler scheduler;

    //Connection vars
    private static Connection connection; //This is the variable we will use to connect to database

    @SuppressWarnings("unused")
     void setupSchedule() {

    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url,configValues.getUsername(),configValues.getPassword());
            Universal.getMethods().getLogger().info("Connection successful to MySQL");
        }
    }

    @SuppressWarnings("unused")
    private DatabaseHandler(boolean itself) {
            setupSchedule();
    }

    static void setup() {
        ConfigManager configManager = FileManager.getConfigManager();
        configValues = configManager.getConfigValues();


        url = "jdbc:mysql://%host%:%port%/%database%".replaceAll("%host%",configValues.getUrlHost()).replaceAll("%port%",configValues.getPort()).replaceAll("%database%",configValues.getDatabase());

        scheduler = ProxyServer.getInstance().getScheduler();
        new DatabaseHandler(false);

    }


    public static Connection getConnection() {
        return connection;
    }

    public static Statement statement() {
        try {
            if(connection == null) return null;
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TaskScheduler getScheduler() {
        return scheduler;
    }


}
