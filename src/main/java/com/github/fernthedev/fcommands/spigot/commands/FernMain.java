package com.github.fernthedev.fcommands.spigot.commands;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.util.VersionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FernMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(FernCommands.message("&aRunning FernCommands version " + Universal.getPlugin().getPluginData().getVersion() + " (FernAPI: " + VersionUtil.getVersionData().getFernapi_version() + ")"));
            sender.sendMessage(FernCommands.message("&aAuthors: " + FernCommands.getInstance().getDescription().getAuthors()));
        }else{
            String arg1 = args[0];

            switch (arg1.toLowerCase()) {
                case "help" -> Help.getInstance().onCommand(sender, command, s, args);
                case "reload" -> ReloadConfig.getInstance().onCommand(sender, command, s, args);
                case "hookmanager" -> Hooks.getInstance().onCommand(sender, command, s, args);
                default -> {
                    sender.sendMessage(FernCommands.message("&cInvalid argument"));
                    Help.getInstance().onCommand(sender, command, s, args);
                }
            }

            /*

            if(arg1.equalsIgnoreCase("Help")) {
                    Help.getInstance().onCommand(sender,command,s,args);
            }else
                if(arg1.equalsIgnoreCase("reload")) {
                ReloadConfig.getInstance().onCommand(sender, command, s, args);
            }else
                if (arg1.equalsIgnoreCase("HookManager")) {
                        HookManager.getInstance().onCommand(sender, command, s, args);
                    }else {
                sender.sendMessage(FernCommands.message("&cInvalid argument"));
                Help.getInstance().onCommand(sender,command,s,args);
                }*/
        }
        return true;
    }
}
