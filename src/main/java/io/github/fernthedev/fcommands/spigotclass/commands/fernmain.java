package io.github.fernthedev.fcommands.spigotclass.commands;

import io.github.fernthedev.fcommands.spigotclass.FernCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class fernmain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(FernCommands.message("&aRunning FernComamnds version" + FernCommands.getInstance().getDescription().getVersion()));
            sender.sendMessage(FernCommands.message("&aAuthors: " + FernCommands.getInstance().getDescription().getAuthors()));
        }else{
            String arg1 = args[0];
            if(arg1.equalsIgnoreCase("help")) {
                help.getInstance().onCommand(sender,command,s,args);
            }else
                if(arg1.equalsIgnoreCase("relaod")) {
                reloadconfig.getInstance().onCommand(sender, command, s, args);
            }else
                if (arg1.equalsIgnoreCase("hooks")) {
                        hooks.getInstance().onCommand(sender, command, s, args);
                    }else {
                sender.sendMessage(FernCommands.message("&cInvalid argument"));
                help.getInstance().onCommand(sender,command,s,args);
                }
        }
        return true;
    }
}
