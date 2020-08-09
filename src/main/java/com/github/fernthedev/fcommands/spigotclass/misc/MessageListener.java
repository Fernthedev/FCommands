package com.github.fernthedev.fcommands.spigotclass.misc;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

public class MessageListener implements PluginMessageListener {

    private List<PluginMessageListener> listeners = new ArrayList<>();

    public MessageListener() {
        FernCommands.getInstance().getLogger().info("Message Listener created");
    }

    public void addListener(PluginMessageListener pluginMessageListener) {
        listeners.add(pluginMessageListener);
    }

    public void removeListener(PluginMessageListener pluginMessageListener) {
        listeners.remove(pluginMessageListener);
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
       //FernCommands.getInstance().getLogger().info("Received a message. Calling all other events at channel " + channel);
     for(PluginMessageListener listener: listeners) {
         //FernCommands.getInstance().getLogger().info("Launched plugin Messaging event for " + listener);

         listener.onPluginMessageReceived(channel,player,message);
     }
    }
}
