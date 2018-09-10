package io.github.fernthedev.fcommands.bungeeclass.commands;

import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.util.logging.Logger;

public abstract class FCommand extends Command {

    public FCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    BaseComponent[] msg(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }

    Logger logger() {
        return FernCommands.getInstance().getLogger();
    }

    void sendMessage(CommandSender player, String message) {
        player.sendMessage(msg(message));
    }
}
