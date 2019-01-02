package com.github.fernthedev.fcommands.spigotclass.interfaces;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public abstract class FCommand implements CommandExecutor {
    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String name, String[] args);

    protected Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }
}
