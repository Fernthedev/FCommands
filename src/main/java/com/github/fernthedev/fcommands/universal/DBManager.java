package com.github.fernthedev.fcommands.universal;


import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.mysql.AikarFernDatabase;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;


public class DBManager extends DatabaseListener {
    public DBManager(String username, String password, String port, String URLHost, String database) {
        super(AikarFernDatabase.createHikariDatabase(APIHandler.getInstance().getPlugin(), new DatabaseAuthInfo(username, password, port, URLHost, database)));
    }

    /**
     * This is called after you attempt a connection
     *
     * @param connected Returns true if successful
     * @see DatabaseListener#connect()
     */
    @Override
    public void onConnectAttempt(boolean connected) {
        if(connected) {
            APIHandler.getInstance().getMethods().getAbstractLogger().info("Connected successfully");
        }else{
            APIHandler.getInstance().getMethods().getAbstractLogger().warn("Unable to connect successfully");
        }
    }
}
