package com.github.fernthedev.fcommands.spigot.nick;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.hooks.HookManager;
import com.github.fernthedev.fcommands.universal.NickNetworkManager;
import com.github.fernthedev.fcommands.universal.mysql.nick.NickDatabaseInfo;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import com.google.gson.Gson;
import kotlin.Unit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Singleton
public class NickManager implements Listener {

    private FernCommands plugin;
    private HookManager hookManager;
    private final APIHandler apiHandler;

    private static final HashMap<String, String> nicknames = new HashMap<>();

    private static final NickDatabaseInfo databaseInfo = new NickDatabaseInfo();

    @Inject
    public NickManager(NickNetworkManager nickNetworkManager, HookManager hookManager, FernCommands plugin, APIHandler apiHandler) {
        this.plugin = plugin;
        this.hookManager = hookManager;
        this.apiHandler = apiHandler;

        DatabaseListener databaseManager = plugin.getDatabaseManager();
        apiHandler.getLogger().info("Initiating Global Nicks");


        databaseManager.runOnConnect(() -> {
            try {
                databaseManager.createTable(databaseInfo).get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        databaseManager.connect();

        apiHandler.getScheduler().runAsync(() ->
                runSqlSync().handle((t, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    } else apiHandler.getLogger().info("Finished loading Global Nicks");

                    return t;
                }));

        nickNetworkManager.getHandleNick().register(this, nickData -> {
            handleNick(nickData.uuid(), nickData.playerName(), nickData.nick());
            return Unit.INSTANCE;
        });
    }

    public void handleNick(String uuid, String playerName, String nick) {
        apiHandler.debug(() -> nick + " " + uuid);
        if (nick != null) {
            Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + nick);
                return null;
            });
            nicknames.put(uuid, nick);
        } else {
            Player pl = Bukkit.getPlayer(uuid);
            if (pl != null) pl.sendMessage(ChatColor.RED + "Unable to set nick");
        }
    }

    private CompletableFuture<Void> runSqlSync() {
        DatabaseListener databaseManager = plugin.getDatabaseManager();

        return databaseInfo.loadFromDB(databaseManager).thenRun(() -> {
            Queue<NickDatabaseInfo.NickDatabaseRowInfo> rowDataStack = new LinkedList<>(databaseInfo.getRowDataListCopy().values());

            while (!rowDataStack.isEmpty()) {
                NickDatabaseInfo.NickDatabaseRowInfo rowData = rowDataStack.remove();
                String uuid = rowData.getUuid().toString();
                String nick = rowData.getNick();
                nicknames.put(uuid, nick);
            }
        });


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (hookManager.isEssentials())
            apiHandler.getScheduler().runAsync(() -> {
                try {
                    DatabaseListener databaseManager = plugin.getDatabaseManager();
                    if (databaseManager.isConnected()) {
                        runSqlSync().get();
                        String uuid = e.getPlayer().getUniqueId().toString();
                        String playerName = e.getPlayer().getName();

                        String nick = null;

                        NickDatabaseInfo.NickDatabaseRowInfo nickRow = databaseInfo.getRow(uuid);
                        if (nickRow != null) {
                            nick = nickRow.getNick();

                            if (apiHandler.getDebug())
                                apiHandler.debug("Found nick {} from uuid {} info: {}", nick, uuid, ChatColor.GOLD + new Gson().toJson(nickRow));
                        }

                        if (nick == null)
                            for (NickDatabaseInfo.NickDatabaseRowInfo rowData : databaseInfo.getRowDataListCopy().values()) {
                                if (rowData.getUuid() != null && rowData.getUuid().toString().equals(uuid)) {
                                    nick = rowData.getNick();
                                    break;
                                }
                            }

                        if (nick == null) return;

                        if (!NickManager.nicknames.get(uuid).equals(nick)) {
                            String finalNick = nick;
                            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + finalNick));
                            NickManager.nicknames.put(uuid, nick);
                        }


                    }
                } catch (SQLException | ExecutionException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    interruptedException.printStackTrace();
                }
            });
    }
}

