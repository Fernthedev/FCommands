package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fcommands.universal.mysql.nick.NickDatabaseInfo;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.database.TableInfo;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.handlers.IScheduler;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import com.google.gson.Gson;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Singleton
public class NickNetworkManager extends PluginMessageHandler {

    public record NickData(String uuid, String playerName, String nick) {}

    @Getter
    private final EventCallback<NickData> handleNick = new EventCallback<>();

    @Inject
    private MethodInterface<?, ?> methodHandler;
    @Inject
    private IScheduler<?,?> scheduler;

    @Inject
    private APIHandler apiHandler;

    private static Object l = null;

    public NickNetworkManager() {
        super();

        if (l != null)
            throw new IllegalArgumentException("Already created nick network manager");

        l = new Object();

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
        if (methodHandler.getServerType() == ServerType.BUKKIT) {

            scheduler.runAsync(() -> {
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

                        if (apiHandler.getDebug())
                            apiHandler.debug("Found nick {} from uuid {} info: {}", nick, uuid, ChatColor.GOLD + new Gson().toJson(nickRow));
                    }

                    if (nick == null)
                        for (NickDatabaseInfo.NickDatabaseRowInfo rowData : databaseInfo.getRowDataListCopy().values()) {
                            if (rowData.getUuid() != null && rowData.getUuid().toString().equals(uuid)) {
                                nick = rowData.getNick();
                            }
                        }


                    handleNick.invoke(new NickData(uuid, playerName, nick));
                }
            });
        }
    }

}
