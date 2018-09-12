package io.github.fernthedev.fcommands.bungeeclass;

import io.github.fernthedev.fcommands.Universal.Universal;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    //DataBase vars.
    private static String username; //Enter in your db username
    private static String password; //Enter your password for the db
    private static String url; //Enter URL w/db name
    private static String port;
    private static String urlHost;
    private static String database;

    private static boolean scheduled;

    private static TaskScheduler scheduler;

    //Connection vars
    private static Connection connection; //This is the variable we will use to connect to database

    @SuppressWarnings("unused")
     void setupSchedule() {
        if(scheduler != null && scheduled) {
            scheduler.cancel(FernCommands.getInstance());
            scheduled =false;
        }
        Runnable runnable = () -> {
            try {
                openConnection();
                Statement statement = connection.createStatement();
            } catch(ClassNotFoundException | SQLException e) {
                Universal.getMethods().getLogger().info("The user is " + username + " with password " + password + " with database " + database + " url being " + url);
                e.printStackTrace();
            }
        };
        scheduled = true;
        ProxyServer.getInstance().getScheduler().runAsync(FernCommands.getInstance(),runnable);

        if(!FernCommands.getRunnables().isEmpty())
        for(Runnable runnable1 : FernCommands.getRunnables()) {
            ProxyServer.getInstance().getScheduler().runAsync(FernCommands.getInstance(),runnable1);
        }
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
            connection = DriverManager.getConnection(url,username,password);
            Universal.getMethods().getLogger().info("Connection successful to MySQL");
        }
    }

    @SuppressWarnings("unused")
    private DatabaseHandler(boolean itself) {
            setupSchedule();
    }

    static void setup() {
        FileManager fileManager = new FileManager();


        username = fileManager.getValue("DBUsername","root");
        Universal.getMethods().getLogger().info("Set root in config." + username);

        password = fileManager.getValue("DBPass","pass");
        Universal.getMethods().getLogger().info("Set pass in config." + password);


        database = fileManager.getValue("DB","database");
        Universal.getMethods().getLogger().info("Set database in config." + database);

        port = fileManager.getValue("DBPort","3306");
        Universal.getMethods().getLogger().info("Set port in config." + port);

        urlHost = fileManager.getValue("DBHost","localhost");
        Universal.getMethods().getLogger().info("Set host in config." + urlHost);


        url = "jdbc:mysql://%host%:%port%/%database%".replaceAll("%host%",urlHost).replaceAll("%port%",port).replaceAll("%database%",database);

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
