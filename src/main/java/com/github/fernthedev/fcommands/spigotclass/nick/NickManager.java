package com.github.fernthedev.fcommands.spigotclass.nick;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import com.github.fernthedev.fcommands.spigotclass.hooks.HookManager;
import com.github.fernthedev.fcommands.universal.mysql.nick.NickDatabaseInfo;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class NickManager implements Listener {

    static HashMap<String,String> nicknames = new HashMap<>();

    private static NickDatabaseInfo databaseInfo;

    public NickManager() {
        runSqlSync();
    }

    public static void handleNick(String uuid, String playerName, String nick) {
        System.out.println(nick + " " + uuid);
        if (nick != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + nick);
            nicknames.put(uuid, nick);
        } else {
            Player pl = Bukkit.getPlayer(uuid);
            if (pl != null) pl.sendMessage(ChatColor.RED + "Unable to set nick");
        }
    }

    private void runSqlSync() {
        DatabaseListener databaseManager = FernCommands.getDatabaseManager();
        databaseManager.runOnConnect(() -> {
            try {
                databaseInfo = (NickDatabaseInfo) new NickDatabaseInfo().getFromDatabase(databaseManager);

                Queue<RowData> rowDataStack = new LinkedList<>(databaseInfo.getRowDataList());

                while(!rowDataStack.isEmpty()) {
                    RowData rowData = rowDataStack.remove();
                    String uuid = rowData.getColumn("PLAYERUUID").getValue();
                    String nick = rowData.getColumn("NICK").getValue();
                    nicknames.put(uuid,nick);
                }

//                String sql = "SELECT * FROM fern_nicks;";
//                PreparedStatement stmt = DatabaseHandler.getConnection().prepareStatement(sql);
//                ResultSet result = stmt.executeQuery();
//
//                while (result.next()) {
//                    String uUID = result.getString("PLAYERUUID");
//                    String nick = result.getString("NICK");
//                    nicknames.put(uUID,nick);
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

//    @Override
    @Deprecated
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
//            getLogger().info("It is not bungeecord message!");
            return;
        }
//        getLogger().info("It is bungeecord message!NICK");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
//            getLogger().info("Recieved message on a random channel ");
            String type = in.readUTF(); //TYPE

            if (in.available() > 0) {
                in.readUTF(); //SERVER NOT NEEDED
                if (in.available() > 0) {
                    String subchannel = in.readUTF(); //SUB CHANNEL
//                    getLogger().info("Recieved message on channel " + subchannel);
                    if (type.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase("ReloadNickSQL")) {

//                        getLogger().info("Requested by " + subchannel + " to reload nicks.");
                        String playerName = in.readUTF(); //PLAYER NAME
//                        getLogger().info("Player is" + playerName);
                        String uuid = in.readUTF(); //UUID OF SENDER
//                        getLogger().info("The uuid of sender is " + uuid);

                        runSqlSync();
//                        String sql = "SELECT * FROM fern_nicks;";
//
//                        if(DatabaseHandler.getConnection() == null) {
//                            getLogger().warning("SQL connection has not been created.");
//                        }
//
//                        PreparedStatement stmt = DatabaseHandler.getConnection().prepareStatement(sql);
//                        ResultSet result = stmt.executeQuery();

                        String nick = null;

                        for (RowData rowData : databaseInfo.getRowDataList()) {
                            if(rowData.getColumn("PLAYERUUID").getColumnName().equals(uuid)) {
                                nick = rowData.getColumn("NICK").getValue();
                            }
                        }


//                        while (result.next()) {
//                            String uUID = result.getString("PLAYERUUID");
//                            if (uUID.equals(uuid)) {
//                                nick = result.getString("NICK");
//                            }
//                        }

                        if (nick != null) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + nick);
                            nicknames.put(uuid,nick);
                        } else {
                            Player pl = Bukkit.getPlayer(uuid);
                            if (pl != null) pl.sendMessage(ChatColor.RED + "Unable to set nick");
                        }

                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(HookManager.isEssentials())
        Universal.getScheduler().runAsync(() -> {
            try {
                DatabaseListener databaseManager = FernCommands.getDatabaseManager();
                if(databaseManager.isConnected()) {
                    runSqlSync();
    //                String sql = "SELECT * FROM fern_nicks;";
    //                PreparedStatement stmt;
                    try {
    //                    stmt = DatabaseHandler.getConnection().prepareStatement(sql);
    //                    ResultSet result = stmt.executeQuery();

                        String uuid = e.getPlayer().getUniqueId().toString();
                        String playerName = e.getPlayer().getName();

    //                    String nick = null;
    //                    while (result.next()) {
    //                        String uUID = result.getString("PLAYERUUID");
    //                        if (uUID.equals(uuid)) {
    //                            nick = result.getString("NICK");
    //                        }
    //                    }

                        String nick = null;


                        for (RowData rowData : databaseInfo.getRowDataList()) {
                            if(rowData.getColumn("PLAYERUUID").getValue() == null) {
                                continue;
                            }
                            if(rowData.getColumn("PLAYERUUID").getValue().equals(uuid)) {
                                nick = rowData.getColumn("NICK").getValue();
                                break;
                            }
                        }
                        if(nick == null) return;

                        if(!NickManager.nicknames.get(uuid).equals(nick)) {
                            String finalNick = nick;
                            Bukkit.getScheduler().runTask(FernCommands.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + finalNick));
                            NickManager.nicknames.put(uuid, nick);
                        }


                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}

