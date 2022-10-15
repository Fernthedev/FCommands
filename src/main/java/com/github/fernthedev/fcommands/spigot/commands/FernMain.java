package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fernapi.universal.util.VersionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class FernMain implements CommandExecutor {

    @Inject
    private FernCommands fernCommands;

    @Inject
    private Help help;

    @Inject
    private ReloadConfig reloadConfig;

    @Inject
    private Hooks hooks;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(FernCommands.message("&aRunning FernCommands version " + fernCommands.getPluginData().getVersion() + " (FernAPI: " + VersionUtil.getVersionData().getFernapi_version() + ")"));
            sender.sendMessage(FernCommands.message("&aAuthors: " + fernCommands.getDescription().getAuthors()));
        } else {
            String arg1 = args[0];

            switch (arg1.toLowerCase()) {
                case "help" -> help.onCommand(sender, command, s, args);
                case "reload" -> reloadConfig.onCommand(sender, command, s, args);
                case "hookmanager" -> hooks.onCommand(sender, command, s, args);
                default -> {
                    sender.sendMessage(FernCommands.message("&cInvalid argument"));
                    help.onCommand(sender, command, s, args);
                }
            }
        }
        return true;
    }
}
