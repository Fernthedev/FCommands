package com.github.fernthedev.fcommands.bungeeclass.commands;


import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fcommands.bungeeclass.FileManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class ReloadConfig extends Command {
    public static FernCommands message;
    //String[] args;
    private static ReloadConfig ourInstance = new ReloadConfig();

    public static ReloadConfig getInstance() {
        return ourInstance;
    }

    ReloadConfig() {
        super("fernc", "fernc.reload", "ferncommand", "ferncommands");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (strings.length != 2) {
            sender.sendMessage(new FernCommands().message("&cNo arguments recieved(All,Config,Ip,Seen)"));
        } else {
            String which = strings[1].toLowerCase();
            if (which.equals("all") || which.equals("ip") || which.equals("config") || which.equals("Seen")) {
                try {
                    FileManager.getInstance().loadFiles(which,false);
                }catch (IOException e) {
                    ProxyServer.getInstance().getLogger().warning("&cUnable to reload files");
                    sender.sendMessage(new FernCommands().message("&cUnable to reload files"));
                }

                ProxyServer.getInstance().getLogger().info("Successfully reloaded files");
                sender.sendMessage(new FernCommands().message("&aSuccessfully reloaded files"));
            } else {
                sender.sendMessage(new FernCommands().message("&cWrong arguments recieved " + "\"" + which + "\"" + " (All,Config,Ip)"));
            }
        }
    }

    public static void onDisable() {
        message = null;
        ourInstance = null;
    }
}


