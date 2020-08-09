package com.github.fernthedev.fcommands.spigot.misc;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Messaging {

    private Messaging() {

    }

    /**
     * {@link #sendRequest(Player, String)}
     */
    public static void sendRequest(String request) {
        sendRequest(null, request);
    }
    /**
     * Sends a BUNGEE Request using the Plugin Messaging System
     *
     * @param player The player who will send the plugin message.
     * @param request The request of the plugin message.
     */
    public static void sendRequest(Player player, String request) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        if(player == null)
            player = getRandomPlayer();
        try {
            out.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (player != null)
            player.sendPluginMessage(FernCommands.getInstance(), "BungeeCord", stream.toByteArray());
    }
    /**
     * {@link #sendRequest(String, String...)}
     */
    @SuppressWarnings("JavaDoc")
    public static void sendRequest(String message, String... request) throws IOException {
        sendRequest(null, message, request);
    }



    /**
     * Sends a BUNGEE Request using the Plugin Messaging System
     *
     * @param player The player who will send the plugin message.
     * @param message The message that will be sent.
     * @param request The request of the plugin message.
     */
    public static void sendRequest(Player player, String message, String... request) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);


        for (String s : request) {
            out.writeUTF(s);
        }


        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        msgOut.writeUTF(message);

        out.writeShort(msgBytes.toByteArray().length);
        out.write(msgBytes.toByteArray());
        if(player == null)
            player = getRandomPlayer();

        if (player != null)
            player.sendPluginMessage(FernCommands.getInstance(), "BungeeCord", stream.toByteArray());
    }



    /**
     * @return A random Player (/ the first player in the Player Collection)
     */
    private static Player getRandomPlayer() {
        if (!Bukkit.getServer().getOnlinePlayers().isEmpty())
            return Bukkit.getServer().getOnlinePlayers().iterator().next();
        else
            return null;
    }
}
