package io.github.fernthedev.fcommands.spigotclass.placeholderapi;

import io.github.fernthedev.fcommands.spigotclass.FernCommands;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.logging.Logger;

public class HookPlaceHolderAPI implements Listener, PluginMessageListener {


    public HookPlaceHolderAPI() {
        getLogger().info("Initiated PlaceHolderAPI plugin message listener");
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            getLogger().info("It is not bungeecord message!");
            return;
        }else{
            getLogger().info("It is bungeecord message!");
        }
        getLogger().info("Recieved message on a random channel ");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String type = in.readUTF(); //TYPE
            in.readUTF(); //SERVER NOT NEEDED
            String subchannel = in.readUTF(); //SUB CHANNEL
            getLogger().info("Recieved message on channel " + subchannel);
            if (type.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase("GetPlaceHolderAPI")) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(stream);
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF("PlaceHolderValue");
                getLogger().info("Requested by " + subchannel + " to get a placeholder");
                String placeholderOld = in.readUTF(); //PLACEHOLDER
                getLogger().info("Placeholder is" +placeholderOld);
                String uuid = in.readUTF(); //UUID OF SENDER
                getLogger().info("The uuid of sender is " + uuid);


                getLogger().info("Found that placeholder requested is " + placeholderOld + " for player " + player.getName());

                String placeholder = PlaceholderAPI.setPlaceholders(player, placeholderOld);

                if (placeholder.equalsIgnoreCase(placeholderOld)) {
                    out.writeUTF("NoPlaceHolderFound");
                   // out.writeShort("NoPlaceHolderFound".length());
                } else {
                    //out.writeShort(placeholder.length());
                    getLogger().info("Found that " + player.getName() + " has placeholder " + placeholderOld + " value to " + placeholder);
                    out.writeUTF(placeholder);
                }
                out.writeUTF(uuid);

                Bukkit.getServer().sendPluginMessage(FernCommands.getPlugin(FernCommands.class), "BungeeCord", stream.toByteArray());
                // Use the code sample in the 'Response' sections below to read
                // the data.
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    private Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }
}
