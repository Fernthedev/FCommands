package io.github.fernthedev.fcommands.spigotclass.commands;

import io.github.fernthedev.fcommands.spigotclass.spigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class help implements CommandExecutor {
    private static help ourInstance = new help();

    public static help getInstance() {
        return ourInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(spigot.message("&b/fern hooks &f Gets what compatible plugins are hooked with FernCommands"));
        sender.sendMessage(spigot.message("&b/fern reload &f Reloads config"));
        return true;
    }
}
