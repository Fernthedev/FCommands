package com.github.fernthedev.fcommands.spigot.misc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MessageListener implements PluginMessageListener {

    private final List<PluginMessageListener> listeners = new ArrayList<>();

    @Inject
    public MessageListener(Plugin plugin) {
        plugin.getLogger().info("Message Listener created");
    }

    public void addListener(PluginMessageListener pluginMessageListener) {
        listeners.add(pluginMessageListener);
    }

    public void removeListener(PluginMessageListener pluginMessageListener) {
        listeners.remove(pluginMessageListener);
    }


    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        for (PluginMessageListener listener : listeners) {
            listener.onPluginMessageReceived(channel, player, message);
        }
    }
}
