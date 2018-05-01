package io.github.fernthedev.fcommands.spigotclass.commands;

import io.github.fernthedev.fcommands.spigotclass.spigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class fernmain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(spigot.message("&aRunning FernComamnds version" + spigot.getInstance().getDescription().getVersion()));
            sender.sendMessage(spigot.message("&aAuthors: " + spigot.getInstance().getDescription().getAuthors()));
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
                sender.sendMessage(spigot.message("&cInvalid argument"));
                help.getInstance().onCommand(sender,command,s,args);
                }
        }
        return true;
    }
}
