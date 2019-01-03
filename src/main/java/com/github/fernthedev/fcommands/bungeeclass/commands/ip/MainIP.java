package com.github.fernthedev.fcommands.bungeeclass.commands.ip;

import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fcommands.bungeeclass.FileManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MainIP extends Command {

    public MainIP() {
        super("myip", "fernc.myip", "meip");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            ShowIp.getInstance().execute(sender,args);
        }else{
            if (args[0].equalsIgnoreCase("delete") && FileManager.getConfigValues().isAllowIPDelete()) {
                    DeleteIP.getInstance().execute(sender, args);
            } else {
                if(FileManager.getConfigValues().isAllowIPDelete()) {
                    sender.sendMessage(FernCommands.getInstance().message("&cNo such argument. (delete)"));
                }else{
                    sender.sendMessage(FernCommands.getInstance().message("&cNo such argument."));
                }
            }
        }
    }
}
