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

            switch(arg1.toLowerCase()) {
                case "help":
                    help.getInstance().onCommand(sender,command,s,args);
                    break;
                case "reload":
                    reloadconfig.getInstance().onCommand(sender, command, s, args);
                    break;
                case "hooks":
                    hooks.getInstance().onCommand(sender, command, s, args);
                    break;
                default:
                    sender.sendMessage(FernCommands.message("&cInvalid argument"));
                    help.getInstance().onCommand(sender,command,s,args);
                    break;
            }

            /*

            if(arg1.equalsIgnoreCase("help")) {
                    help.getInstance().onCommand(sender,command,s,args);
            }else
                if(arg1.equalsIgnoreCase("reload")) {
                reloadconfig.getInstance().onCommand(sender, command, s, args);
            }else
                if (arg1.equalsIgnoreCase("hooks")) {
                        hooks.getInstance().onCommand(sender, command, s, args);
                    }else {
                sender.sendMessage(FernCommands.message("&cInvalid argument"));
                help.getInstance().onCommand(sender,command,s,args);
                }*/
        }
        return true;
    }
}
