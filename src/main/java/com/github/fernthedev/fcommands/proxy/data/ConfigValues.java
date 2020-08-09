package com.github.fernthedev.fcommands.proxy.data;

import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ConfigValues {

    public ConfigValues() {
        if(serverChecks.isEmpty()) {
            serverChecks.add(new ServerData("localhost", new ServerData.AddressPortPair("localhost", 25566), 5000));
        }
//        if(databaseAuthInfo == null) {
//            databaseAuthInfo = new DatabaseAuthInfo(
//                    "root",
//                    "pass",
//                    "3306",
//                    "localhost",
//                    "database");
//        }
    }

//    private String motd = "&bThis is the default ferncommand motd \\n which supports linebreaks.";
    private boolean useMotd = false;

    private List<ServerData> serverChecks = new ArrayList<>();

    private boolean cacheIps = false;
    private boolean allowIPDelete = false;
    private boolean allowIPShow = false;
    private boolean showAltsCommand = false;

    private boolean allowNameHistory = true;

    private boolean globalNicks = false;
    private boolean showPing = false;
    private boolean allowSeenCommand = false;
    private boolean punishMotd = false;
    private String offlineServerMotd = "&cSERVER UNDER MAINTENANCE!";
    private PunishValues punishValues = new PunishValues();

    private boolean databaseConnect = false;
    private DatabaseAuthInfo databaseAuthInfo = new DatabaseAuthInfo(
            "root",
            "pass",
            "3306",
            "localhost",
            "database");

//    //DataBase vars.
//    private String username = "root"; //Enter in your db username
//    private String password = "pass"; //Enter your password for the db
//    private String port = "3306";
//    private String urlHost = "localhost";
//    private String database = "database";

}
