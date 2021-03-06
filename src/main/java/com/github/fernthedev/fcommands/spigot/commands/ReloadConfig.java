package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.FilesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class ReloadConfig implements CommandExecutor {
    private static ReloadConfig ourInstance = new ReloadConfig();

    public static ReloadConfig getInstance() {
        return ourInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please type in an argument");
            sender.sendMessage(message("&a/fern reload (config,all) &3Reloads the config &9permission: &bfernc.reload"));
        }else{
            if(args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("fernc.reload")) {
                    if (args.length == 2) {
                        String which = args[1];
                        if (which.equalsIgnoreCase("config") || which.equalsIgnoreCase("all")) {
                            try {
                                FilesManager.getInstance().reloadConfig(which);
                            } catch (IOException e) {
                                sender.sendMessage(message("&cUnable to reload config (" + which + ")"));
                                FernCommands.getInstance().getLogger().warning(message("&cUnable to reload config (" + which + ")"));
                            } catch (InvalidConfigurationException e) {
                                sender.sendMessage(message("&cUnable to reload config (" + which + ") due to invalid syntax"));
                                FernCommands.getInstance().getLogger().warning(message("&cUnable to reload config (" + which + ") due to invalid syntax"));
                            }
                            sender.sendMessage(message("&aSuccessfully reloaded config " + which));
                        } else {
                            sender.sendMessage(message("&cThat argument (" + which + ") is not available"));
                        }
                    }else{
                        sender.sendMessage(message("&cNo arguments recieved"));
                    }
                }else{
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
