package com.github.fernthedev.fcommands.spigotclass.placeholderapi;

import com.github.fernthedev.fcommands.Universal.Channels;
import com.github.fernthedev.fcommands.spigotclass.FernCommands;
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
        if (!channel.equals(Channels.PlaceHolderBungeeChannel)) {
            getLogger().info("It is not ferncommands message!");
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String type = in.readUTF(); //TYPE
            in.readUTF(); //SERVER NOT NEEDED
            String subchannel = in.readUTF(); //SUB CHANNEL

            if (type.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase(Channels.getPlaceHolderResult)) {

                getLogger().info("Recieved message on channel " + subchannel);
                getLogger().info("Recieved message on a random channel ");


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(stream);
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF(Channels.PlaceHolderValue);
                getLogger().info("Requested by " + subchannel + " to get a placeholder");
                String placeholderOld = in.readUTF(); //PLACEHOLDER
                getLogger().info("Placeholder is" +placeholderOld);
                String uuid = in.readUTF(); //UUID OF SENDER
                getLogger().info("The uuid of sender is " + uuid);


                getLogger().info("Found that placeholder requested is " + placeholderOld + " for player " + player.getName());

                String placeholder = PlaceholderAPI.setPlaceholders(player, placeholderOld);

                if (placeholder.equalsIgnoreCase(placeholderOld)) {
                    out.writeUTF("NoPlaceHolderFound");
                    getLogger().info("The placeholder could not be found");
                   // out.writeShort("NoPlaceHolderFound".length());
                } else {
                    //out.writeShort(placeholder.length());
                    getLogger().info("Found that " + player.getName() + " has placeholder " + placeholderOld + " value to " + placeholder);
                    out.writeUTF(placeholder);
                }
                out.writeUTF(uuid);

                Bukkit.getServer().sendPluginMessage(FernCommands.getPlugin(FernCommands.class), Channels.PlaceHolderBungeeChannel, stream.toByteArray());
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
