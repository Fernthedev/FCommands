package com.github.fernthedev.fcommands.spigotclass.commands;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FernMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(FernCommands.message("&aRunning FernComamnds version" + FernCommands.getInstance().getDescription().getVersion()));
            sender.sendMessage(FernCommands.message("&aAuthors: " + FernCommands.getInstance().getDescription().getAuthors()));
        }else{
            String arg1 = args[0];

            switch(arg1.toLowerCase()) {
                case "help":
                    Help.getInstance().onCommand(sender,command,s,args);
                    break;
                case "reload":
                    ReloadConfig.getInstance().onCommand(sender, command, s, args);
                    break;
                case "hookmanager":
                    Hooks.getInstance().onCommand(sender, command, s, args);
                    break;
                default:
                    sender.sendMessage(FernCommands.message("&cInvalid argument"));
                    Help.getInstance().onCommand(sender,command,s,args);
                    break;
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
