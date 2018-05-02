package io.github.fernthedev.fcommands.spigotclass.commands;

import io.github.fernthedev.fcommands.spigotclass.spigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class hooks implements CommandExecutor {
    private static hooks ourInstance = new hooks();

    public static hooks getInstance() {
        return ourInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length != 0 && (args[0].equals("hooks"))) {
        if(sender.hasPermission("fernc.hooks")) {
                if (spigot.getInstance().isVaultEnabled()) {
                    sender.sendMessage(spigot.message("&bVault:&aHooked"));
                    if (spigot.getChat().isEnabled()) {
                        sender.sendMessage(spigot.message("&b(Vault) Chat:&aHooked"));
                    } else {
                        sender.sendMessage(spigot.message("&b(Vault) Chat:&cUnhooked"));
                    }
                    if (spigot.getEconomy().isEnabled()) {
                        sender.sendMessage(spigot.message("&b(Vault) Economy:&aHooked"));
                    } else {
                        sender.sendMessage(spigot.message("&b(Vault) Economy:&cUnhooked"));
                    }
                    if (spigot.getPermissions().isEnabled()) {
                        sender.sendMessage(spigot.message("&b(Vault) Permissions:&aHooked"));
                    } else {
                        sender.sendMessage(spigot.message("&b(Vault) Permissions:&cUnhooked"));
                    }
                } else {
                    sender.sendMessage(spigot.message("&bVault:&cUnhooked"));
                }

                if (spigot.getInstance().isNTEEnabled()) {
                    sender.sendMessage(spigot.message("&bNametagEdit:&aHooked"));
                } else {
                    sender.sendMessage(spigot.message("&bNametagEdit:&cUnhooked"));
                }
                if (spigot.getInstance().isNCPEnabled()) {
                    sender.sendMessage(spigot.message("&bNoCheatPlus:&aHooked"));
                } else {
                    sender.sendMessage(spigot.message("&bNoCheatPlus:&cUnhooked"));
                }
            }
        }
        return true;
    }
}
