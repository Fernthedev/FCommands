package io.github.fernthedev.fcommands.spigotclass.nick;

import io.github.fernthedev.fcommands.spigotclass.DatabaseHandler;
import io.github.fernthedev.fcommands.spigotclass.FernCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class NickReload implements Listener, PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            getLogger().info("It is not bungeecord message!");
            return;
        }
        getLogger().info("It is bungeecord message!NICK");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            getLogger().info("Recieved message on a random channel ");
            String type = in.readUTF(); //TYPE

            if (in.available() > 0) {
                in.readUTF(); //SERVER NOT NEEDED
                if (in.available() > 0) {
                    String subchannel = in.readUTF(); //SUB CHANNEL
                    getLogger().info("Recieved message on channel " + subchannel);
                    if (type.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase("ReloadNickSQL")) {

                        getLogger().info("Requested by " + subchannel + " to reload nicks.");
                        String playerName = in.readUTF(); //PLAYER NAME
                        getLogger().info("Player is" + playerName);
                        String uuid = in.readUTF(); //UUID OF SENDER
                        getLogger().info("The uuid of sender is " + uuid);

                        String sql = "SELECT * FROM fern_nicks;";
                        PreparedStatement stmt = DatabaseHandler.getConnection().prepareStatement(sql);
                        ResultSet result = stmt.executeQuery();

                        String nick = null;
                        while (result.next()) {
                            String UUID = result.getString("PLAYERUUID");
                            if (UUID.equals(uuid)) {
                                nick = result.getString("NICK");
                            }
                        }

                        if (nick != null) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + nick);
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
    private Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }
}

