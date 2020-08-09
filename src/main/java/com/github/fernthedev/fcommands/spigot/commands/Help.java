package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Help implements CommandExecutor {
    private static Help ourInstance = new Help();

    public static Help getInstance() {
        return ourInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(FernCommands.message("&b/fern HookManager &f Gets what compatible plugins are hooked with FernCommands"));
        sender.sendMessage(FernCommands.message("&b/fern reload &f Reloads config"));
        return true;
    }
}
