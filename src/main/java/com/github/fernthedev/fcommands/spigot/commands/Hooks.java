package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.hooks.HookManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class Hooks implements CommandExecutor {

    @Inject
    private HookManager hookManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length != 0 && (args[0].equals("HookManager"))) {
            if (sender.hasPermission("fernc.HookManager")) {
                if (hookManager.isVault()) {
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

                if (hookManager.isWorldGuard())
                    sender.sendMessage(FernCommands.message("&bWorldGuard:&aHooked"));
                else
                    sender.sendMessage(FernCommands.message("&bWorldGuard:&cUnhooked"));

                if (hookManager.isNte()) {
                    sender.sendMessage(FernCommands.message("&bNametagEdit:&aHooked"));
                } else {
                    sender.sendMessage(FernCommands.message("&bNametagEdit:&cUnhooked"));
                }
                if (hookManager.isNCPEnabled()) {
                    sender.sendMessage(FernCommands.message("&bNoCheatPlus:&aHooked"));
                } else {
                    sender.sendMessage(FernCommands.message("&bNoCheatPlus:&cUnhooked"));
                }
            }
        }
        return true;
    }
}
