package io.github.fernplayzz.fcommands.spigotclass;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class messaging {

    /**
     * {@link #sendRequest(Player, String)}
     */
    public static void sendRequest(String request) {
        sendRequest(null, request);
    }
    /**
     * Sends a Bungee Request using the Plugin Messaging System
     *
     * @param player The player who will send the plugin message.
     * @param request The request of the plugin message.
     */
    public static void sendRequest(Player player, String request) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        if(player == null)
            player = getRandomPlayer();
        out.writeUTF(request);
        if (player != null)
            player.sendPluginMessage(spigot.getInstance(), "BungeeCord", out.toByteArray());
    }
    /**
     * {@link #sendRequest(String, String...)}
     */
    public static void sendRequest(String message, String... request) throws IOException {
        sendRequest(null, message, request);
    }



    /**
     * Sends a Bungee Request using the Plugin Messaging System
     *
     * @param player The player who will send the plugin message.
     * @param message The message that will be sent.
     * @param request The request of the plugin message.
     */
    public static void sendRequest(Player player, String message, String... request) throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        Arrays.asList(request).forEach(out::writeUTF);

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        msgOut.writeUTF(message);

        out.writeShort(msgBytes.toByteArray().length);
        out.write(msgBytes.toByteArray());
        if(player == null)
            player = getRandomPlayer();

        if (player != null)
            player.sendPluginMessage(spigot.getInstance(), "BungeeCord", out.toByteArray());
    }



    /**
     * @return A random Player (/ the first player in the Player Collection)
     */
    private static Player getRandomPlayer() {
        if (Bukkit.getServer().getOnlinePlayers().size() > 0)
            return Bukkit.getServer().getOnlinePlayers().iterator().next();
        else
            return null;
    }
}
