package com.github.fernthedev.fcommands.spigotclass;

import com.github.fernthedev.fcommands.Universal.DatabaseInterface;
import com.github.fernthedev.fernapi.universal.Universal;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static FilesManager fileManager;
    private static Configuration config;

    //DataBase vars.
    private static String username; //Enter in your db username
    private static String password; //Enter your password for the db
    private static String url; //Enter URL w/db name
    private static String port;
    private static String urlHost;
    private static String database;

    private static boolean scheduled;

    private static boolean isSetup = false;

    private static BukkitScheduler scheduler;

    private static boolean connected = false;
    private static List<DatabaseInterface> runAfterConnectMethods = new ArrayList<>();

    public static void runAfterConnect(DatabaseInterface method) {
        if(!connected) {
            runAfterConnectMethods.add(method);
        }else{
            method.runAfterConnect();
        }
    }

    public static boolean isConnected() {
        return connected;
    }

    //Connection vars
    private static Connection connection; //This is the variable we will use to connect to database
    private static Statement statement;

    void setupSchedule() {
        if(scheduler != null && scheduled) {
            scheduler.cancelTasks(FernCommands.getInstance());
            scheduled =false;
        }
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    openConnection();
                    Statement statement = connection.createStatement();
                } catch(ClassNotFoundException | SQLException e) {
                    Universal.getMethods().getLogger().info("The user is " + username + " with password " + password + " with database " + database + " url being " + url);
                    e.printStackTrace();
                }
            }
        };
        scheduled = true;
        runnable.runTaskAsynchronously(FernCommands.getInstance());

        if(!FernCommands.getRunnables().isEmpty())
            for(BukkitRunnable runnable1 : FernCommands.getRunnables()) {
                runnable1.runTaskAsynchronously(FernCommands.getInstance());
            }
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }
        if (!isSetup) {
           Universal.getMethods().getLogger().info("Connecting to MySQL now.");
        }
        connection = DriverManager.getConnection(url, username, password);

        if (!isSetup) {
            connected = true;
            if(!runAfterConnectMethods.isEmpty()) {
                for(DatabaseInterface method : runAfterConnectMethods) {
                    method.runAfterConnect();
                }
            }

           Universal.getMethods().getLogger().info("Connected successfully");
            isSetup = true;
        }
    }

    public DatabaseHandler() {}

    private DatabaseHandler(boolean itself) {
        setupSchedule();
    }

    static void setup() {
        if(!isSetup) {
           Universal.getMethods().getLogger().info("Setting database connection");
        }
        fileManager = FilesManager.getInstance();

        scheduler = Bukkit.getScheduler();
        new DatabaseHandler(false);

        config = FilesManager.config;
        username = fileManager.getValue("DBUsername","root");
        password = fileManager.getValue("DBPass","pass");
        database = fileManager.getValue("DB","database");
        port = fileManager.getValue("DBPort","3306");
        urlHost = fileManager.getValue("DBHost","localhost");

        url = "jdbc:mysql://%host%:%port%/%database%".replaceAll("%host%",urlHost).replaceAll("%port%",port).replaceAll("%database%",database);



    }


    public static Connection getConnection() {
        return connection;
    }

    public static Statement statement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static BukkitScheduler getScheduler() {
        return scheduler;
    }

    public static String getDatabase() {
        return database;
    }


}
