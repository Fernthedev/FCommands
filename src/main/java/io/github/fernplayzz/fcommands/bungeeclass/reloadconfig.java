package io.github.fernplayzz.fcommands.bungeeclass;


import io.github.fernplayzz.fcommands.bungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import javax.security.auth.login.Configuration;

public class reloadconfig extends Command {
    Configuration reload;

    public reloadconfig() {
        super("fernc","fernc.reload","ferncommand","ferncommands");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (strings[0] == "reload") {
            if (strings[1] == null) {
                sender.sendMessage(message("&cNo arguments recieved(All,Config,Ip)"));
            }
            strings[1] = strings[1].toLowerCase();
            if (strings[1] == "all" ||
                    strings[1] == "ip" ||
                    strings[1] == "config") {
                new bungee().loadFiles(strings[1]);

            }
        }else{
            sender.sendMessage(message("&cWrong arguments recieved" + "\"" + strings[1] + "\"" + " (All,Config,Ip"));
        }
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
