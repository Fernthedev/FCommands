package io.github.fernthedev.fcommands.bungeeclass.commands;

import io.github.fernthedev.fcommands.Universal.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.List;

public class NameHistory extends Command {
    public NameHistory() {
        super("nh", "fernc.namehistory", "namehistory","fnh");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 0) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
            String playername;
            String uuidPlayer = UUIDFetcher.getUUID(args[0]);
            if(player != null || uuidPlayer != null) {
                if(player != null) {
                    uuidPlayer = player.getUniqueId().toString();
                    playername = player.getName();
                }else{
                    playername = args[0];
                }
                List<UUIDFetcher.PlayerHistory> names = UUIDFetcher.getNameHistory(uuidPlayer);
                if(names != null) {
                    sender.sendMessage(msg("&b" + playername + "'s names."));

                    for (UUIDFetcher.PlayerHistory playerHistory : names) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String time = format.format(playerHistory.getTime());
                        sender.sendMessage(msg("&b-&3" + playerHistory.getName()));
                        if(playerHistory.getTimeDateInt() != 0)
                        sender.sendMessage(msg("&3  -&b" + time));
                    }
                }else{
                    sender.sendMessage(msg("&cUnable to find player history"));
                }

            }else{
                sender.sendMessage(msg("&cUnable to find player " + args[0]));
            }
        }else{
            sender.sendMessage(msg("&cNo player argument specified"));
        }
    }

    private BaseComponent[] msg(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
