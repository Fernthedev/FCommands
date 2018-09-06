package io.github.fernthedev.fcommands.bungeeclass.commands.ip;

import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import io.github.fernthedev.fcommands.bungeeclass.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;
import java.util.List;

public class ShowIp extends Command {

    private static ShowIp ourInstance = new ShowIp();

    public static ShowIp getInstance() {
        return ourInstance;
    }

    public ShowIp() {
        super("myip", "fernc.myip.see", "me");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;

        Configuration ipconfig = new FileManager().getIpconfig();

        String ip = p.getAddress().getHostString();
        ip = ip.replaceAll("\\.", " ");


        //String uuid = p.getUniqueId().toString();

        boolean ipfileloaded;

        try {
            //ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            new FileManager().loadFiles("ip",true);
            new FileManager().loadFiles("ipdelete",true);
            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
            ipfileloaded = true;
        } catch (IOException e) {
            FernCommands.getInstance().getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }

        List<String> players = ipconfig.getStringList(ip);

        Configuration deleteipconfig = new FileManager().getDeleteipconfig();
        if(deleteipconfig.getSection(ip) != null) {
            if (deleteipconfig.getSection(ip).getBoolean("isToDelete")) {
                p.sendMessage(messager("&aYour ip is going to be deleted soon. It will be deleted in at least one hour."));
            }
        }
        if(ipfileloaded) {
            p.sendMessage(messager("&aYou are currently connected from: " + ip.replaceAll(" ",".")));
            p.sendMessage(messager("&cAny other ip you have connected from will not be shown from this ip."));
            p.sendMessage(messager("&6If you want to delete this information, use the command \"myip delete\""));
            p.sendMessage(messager("&6It would be then deleted atleast 1 hour after the command."));
            p.sendMessage(messager("&bAccounts:"));
            if(!deleteipconfig.getSection(ip).getBoolean("isToDelete") || deleteipconfig.getSection(ip) == null) {
                for (String player : players) {
                    ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(player);
                    if(player1 != null) {
                        String playername = player1.getDisplayName();

                        p.sendMessage(messager("&9-&3" + playername));
                    }
                }
            }else{
                p.sendMessage(messager("&9-&3" + p.getDisplayName()));
            }
        }
    }

    private BaseComponent[] messager(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
