package com.github.fernthedev.fcommands.spigotclass.commands;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
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
        sender.sendMessage(FernCommands.message("&b/fern HookManager &f Gets what compatible plugins are hooked with FernCommands"));
        sender.sendMessage(FernCommands.message("&b/fern reload &f Reloads config"));
        return true;
    }
}
