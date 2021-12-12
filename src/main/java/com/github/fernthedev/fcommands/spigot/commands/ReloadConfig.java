package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.SpigotFileManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Logger;

public class ReloadConfig implements CommandExecutor {

    @Inject
    private SpigotFileManager spigotFileManager;

    @Inject
    private Plugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        Logger logger = plugin.getLogger();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please type in an argument");
            sender.sendMessage(message("&a/fern reload (config,all) &3Reloads the config &9permission: &bfernc.reload"));
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("fernc.reload")) {
                    if (args.length == 2) {
                        String which = args[1];
                        if (which.equalsIgnoreCase("config") || which.equalsIgnoreCase("all")) {
                            try {
                                spigotFileManager.reloadConfig(which);
                            } catch (IOException e) {
                                sender.sendMessage(message("&cUnable to reload config (" + which + ")"));
                                logger.warning(message("&cUnable to reload config (" + which + ")"));
                            } catch (InvalidConfigurationException e) {
                                sender.sendMessage(message("&cUnable to reload config (" + which + ") due to invalid syntax"));
                                logger.warning(message("&cUnable to reload config (" + which + ") due to invalid syntax"));
                            }
                            sender.sendMessage(message("&aSuccessfully reloaded config " + which));
                        } else {
                            sender.sendMessage(message("&cThat argument (" + which + ") is not available"));
                        }
                    } else {
                        sender.sendMessage(message("&cNo arguments recieved"));
                    }
                } else {
                    sender.sendMessage(message("&cYou do not have permission to use that NameColor since a &3fern &csaid so"));
                }
            }
        }
        return true;
    }

    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);

    }


}
