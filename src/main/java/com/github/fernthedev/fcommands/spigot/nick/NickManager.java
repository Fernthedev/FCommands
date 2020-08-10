package com.github.fernthedev.fcommands.spigot.nick;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.hooks.HookManager;
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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class NickManager implements Listener {

    static HashMap<String,String> nicknames = new HashMap<>();

    private static NickDatabaseInfo databaseInfo;

    public NickManager() {
        Universal.getScheduler().runAsync(this::runSqlSync);
    }

    public static void handleNick(String uuid, String playerName, String nick) {
        System.out.println(nick + " " + uuid);
        if (nick != null) {
            Bukkit.getScheduler().callSyncMethod(FernCommands.getInstance(), () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + nick);
                return null;
            });
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(HookManager.isEssentials())
            Universal.getScheduler().runAsync(() -> {
            try {
                DatabaseListener databaseManager = FernCommands.getDatabaseManager();
                if(databaseManager.isConnected()) {
                    runSqlSync();
                    try {
                        String uuid = e.getPlayer().getUniqueId().toString();
                        String playerName = e.getPlayer().getName();

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

