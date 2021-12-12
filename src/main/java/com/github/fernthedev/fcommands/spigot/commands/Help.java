package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Help implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        sender.sendMessage(FernCommands.message("&b/fern HookManager &f Gets what compatible plugins are hooked with FernCommands"));
        sender.sendMessage(FernCommands.message("&b/fern reload &f Reloads config"));
        return true;
    }
}
