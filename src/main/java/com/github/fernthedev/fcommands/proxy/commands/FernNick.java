package com.github.fernthedev.fcommands.proxy.commands;

import co.aikar.commands.*;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fcommands.universal.Channels;
import com.github.fernthedev.fcommands.universal.UniversalMysql;
import com.github.fernthedev.fcommands.universal.mysql.nick.NickDatabaseInfo;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@CommandPermission("fernc.nick")
@CommandAlias("fnick|gnick|nick|fnick")
public class FernNick extends BaseCommand {
    public FernNick() {
        setupTable();
    }

    private static final NickDatabaseInfo databaseInfo = new NickDatabaseInfo();

    private void setupTable() {
        DatabaseListener databaseManager = UniversalMysql.getDatabaseManager();
        try {
            databaseManager.createTable(databaseInfo);
            databaseInfo.loadFromDB(databaseManager).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Description("Change nickname using MySQL")
    @CommandPermission("fernc.nick")
    @Default
    @CommandCompletion("@nothing")
    public void onNick(IFPlayer<?> sender, String newNick) {
        onNick(sender, sender, newNick);
    }

    @Description("Change nickname using MySQL")
    @CommandPermission("fernc.nick.others")
    @Subcommand("other")
    @CommandCompletion("* @nothing")
    public void onNick(CommandIssuer sender, @Flags("other") IFPlayer<?> player, String newNick) {
        DatabaseListener databaseManager = UniversalMysql.getDatabaseManager();


        if (!sender.isPlayer() && player == null) {
            sender.sendError(MessageKeys.PLEASE_SPECIFY_ONE_OF, "{valid}", "player");
            return;
        }

        if (player == null) player = (IFPlayer<?>) sender;

        if (newNick.contains("&") && !sender.hasPermission("fernc.nick.color")) {
            sender.sendError(MessageKeys.PERMISSION_DENIED_PARAMETER);
            return;
        }

        try {
            if (databaseManager.isConnected()) {
                databaseInfo.loadFromDB(databaseManager).get();
                applyNick(player, newNick, databaseManager);
            } else {
                sender.sendMessage(MessageType.ERROR, MessageKeys.ERROR_GENERIC_LOGGED);
                throw new SQLException("Connection is null");
            }
        } catch (SQLException e) {
            sender.sendMessage(MessageType.ERROR, MessageKeys.ERROR_GENERIC_LOGGED);
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void applyNick(IFPlayer<?> player, String newNick, DatabaseListener databaseManager) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String formattedUUID = player.getUuid().toString();

        databaseManager.removeRowIfColumnContainsValue(databaseInfo, "PLAYERUUID", formattedUUID);

        NickDatabaseInfo.NickDatabaseRowInfo rowData = new NickDatabaseInfo.NickDatabaseRowInfo(player.getUuid(), newNick);
        databaseManager.insertIntoTable(databaseInfo, rowData);

        PluginMessageData data = new PluginMessageData(stream, player.getCurrentServerName(), Channels.NICK_RELOADNICKSQL, Channels.NICK_CHANNEL);

        data.addData(player.getName());
        data.addData(player.getUuid().toString());

        APIHandler.getInstance().getMessageHandler().sendPluginData(data);
    }

    @HelpCommand
    public void doHelp(FernCommandIssuer sender, CommandHelp help) {
        help.showHelp();
    }
}
