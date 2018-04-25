package io.github.fernthedev.fcommands.bungeeclass;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import javax.security.auth.login.Configuration;
import java.io.IOException;

public class reloadconfig extends Command {
    public bungee message;
    Configuration reload;
    //String[] args;

    reloadconfig() {
        super("fernc","fernc.reload","ferncommand","ferncommands");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (strings.length == 0) {
            sender.sendMessage(new bungee().message("&9Hello there. FernCommands are running"));

        } else {
            if (strings[0].equalsIgnoreCase("reload")) {
                    if (!(strings.length == 2)) {
                        sender.sendMessage(new bungee().message("&cNo arguments recieved(All,Config,Ip,Seen)"));
                    }else{
                        strings[1] = strings[1].toLowerCase();
                        if (strings[1].equals("all") ||
                                strings[1].equals("ip") ||
                                strings[1].equals("config") ||
                        strings[1].equals("seen")) {
                            try {
                                new bungee().loadFiles(strings[1]);
                            } catch (IOException e) {
                                ProxyServer.getInstance().getLogger().warning("&cUnable to reload files");
                                sender.sendMessage(new bungee().message("&cUnable to reload files"));
                            }
                            ProxyServer.getInstance().getLogger().info("Successfully reloaded files");
                            sender.sendMessage(new bungee().message("&aSuccessfully reloaded files"));
                        }else{
                            sender.sendMessage(new bungee().message("&cWrong arguments recieved " + "\"" + strings[1] + "\"" + " (All,Config,Ip)"));
                        }
                }

            }else{
                sender.sendMessage(new bungee().message("&cWrong arguments recieved "+ "\"" + strings[0] + "\"" + " is not valid. (reload)" ));
            }
        }
    }


}
