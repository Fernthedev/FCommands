package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.hooks.HookManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Hooks implements CommandExecutor {
    private static final Hooks ourInstance = new Hooks();

    public static Hooks getInstance() {
        return ourInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length != 0 && (args[0].equals("HookManager"))) {
            if (sender.hasPermission("fernc.HookManager")) {
                if (HookManager.isVault()) {
                    sender.sendMessage(FernCommands.message("&bVault:&aHooked"));
                    if (FernCommands.getChat().isEnabled()) {
                        sender.sendMessage(FernCommands.message("&b(Vault) Chat:&aHooked"));
                    } else {
                        sender.sendMessage(FernCommands.message("&b(Vault) Chat:&cUnhooked"));
                    }
                    if (FernCommands.getEconomy().isEnabled()) {
                        sender.sendMessage(FernCommands.message("&b(Vault) Economy:&aHooked"));
                    } else {
                        sender.sendMessage(FernCommands.message("&b(Vault) Economy:&cUnhooked"));
                    }
                    if (FernCommands.getPermissions().isEnabled()) {
                        sender.sendMessage(FernCommands.message("&b(Vault) Permissions:&aHooked"));
                    } else {
                        sender.sendMessage(FernCommands.message("&b(Vault) Permissions:&cUnhooked"));
                    }
                } else {
                    sender.sendMessage(FernCommands.message("&bVault:&cUnhooked"));
                }

                if (HookManager.isWorldGuard())
                    sender.sendMessage(FernCommands.message("&bWorldGuard:&aHooked"));
                else
                    sender.sendMessage(FernCommands.message("&bWorldGuard:&cUnhooked"));

                if (HookManager.isNte()) {
                    sender.sendMessage(FernCommands.message("&bNametagEdit:&aHooked"));
                } else {
                    sender.sendMessage(FernCommands.message("&bNametagEdit:&cUnhooked"));
                }
                if (FernCommands.getHookManager().isNCPEnabled()) {
                    sender.sendMessage(FernCommands.message("&bNoCheatPlus:&aHooked"));
                } else {
                    sender.sendMessage(FernCommands.message("&bNoCheatPlus:&cUnhooked"));
                }
            }
        }
        return true;
    }
}
