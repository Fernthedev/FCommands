package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fcommands.spigot.nick.NickManager;
import com.github.fernthedev.fcommands.universal.mysql.nick.NickDatabaseInfo;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.TableInfo;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import com.google.gson.Gson;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NickNetworkManager extends PluginMessageHandler {

    public NickNetworkManager() {
        super();
        DatabaseListener databaseManager = UniversalMysql.getDatabaseManager();

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
    }

    /**
     * This is the channel name that will be registered incoming and outgoing
     *
     * @return The channels that will be incoming and outgoing
     */
    @Override
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();
        channels.add(Channels.NICK_CHANNEL);
        return channels;
    }

    private static final NickDatabaseInfo databaseInfo = new NickDatabaseInfo();

    private CompletableFuture<TableInfo<NickDatabaseInfo.NickDatabaseRowInfo>> runSqlSync() {
        DatabaseListener databaseManager = UniversalMysql.getDatabaseManager();

        return databaseInfo.loadFromDB(databaseManager);
    }

    @Override
    public void onMessageReceived(PluginMessageData pluginMessageData, Channel channel) {
        if (Universal.getMethods().getServerType() == ServerType.BUKKIT) {

            Universal.getScheduler().runAsync(() -> {
                String type = pluginMessageData.getProxyChannelType(); //TYPE
                String server = pluginMessageData.getServer(); // Server
                String subChannel = pluginMessageData.getSubChannel(); // Subchannel

                Queue<String> dataList = new LinkedList<>(pluginMessageData.getExtraData());

                if (subChannel.equalsIgnoreCase(Channels.NICK_RELOADNICKSQL)) {
                    String playerName = dataList.remove();
                    String uuid = dataList.remove();

                    try {
                        runSqlSync().get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    String nick = null;
                    NickDatabaseInfo.NickDatabaseRowInfo nickRow = databaseInfo.getRow(uuid);
                    if (nickRow != null) {
                        nick = nickRow.getNick();

                        if (Universal.isDebug())
                            Universal.debug("Found nick {} from uuid {} info: {}", nick, uuid, ChatColor.GOLD + new Gson().toJson(nickRow));
                    }

                    if (nick == null)
                        for (NickDatabaseInfo.NickDatabaseRowInfo rowData : databaseInfo.getRowDataListCopy().values()) {
                            if (rowData.getUuid() != null && rowData.getUuid().toString().equals(uuid)) {
                                nick = rowData.getNick();
                            }
                        }


                    NickManager.handleNick(uuid, playerName, nick);
                }
            });
        }
    }

}
