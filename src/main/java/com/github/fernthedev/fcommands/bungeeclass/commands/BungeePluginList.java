package com.github.fernthedev.fcommands.bungeeclass.commands;

import com.github.fernthedev.fernapi.server.bungee.FernCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePluginList extends FernCommand {


    /**
     * Construct a new command.
     *
     */
    public BungeePluginList() {
        super("bplugins", "bungee.plugins", "bungeeplugins");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BaseComponent baseComponent = message("&aPlugins:");

        for (Plugin plugin : ProxyServer.getInstance().getPluginManager().getPlugins()) {
            baseComponent.addExtra(message(" &7"));
            baseComponent.addExtra(message(plugin.getDescription().getName()));
            baseComponent.addExtra(message(" &8{" + plugin.getDescription().getAuthor() + "}; "));
        }

        sender.sendMessage(baseComponent);
    }
}
