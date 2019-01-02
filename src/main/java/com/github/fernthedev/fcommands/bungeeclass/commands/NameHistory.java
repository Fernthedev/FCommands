package com.github.fernthedev.fcommands.bungeeclass.commands;

import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NameHistory extends Command {
    public NameHistory() {
        super("nh", "fernc.namehistory", "namehistory","fnh");


    }

    private static HashMap<ProxiedPlayer,Long> cooldowns = new HashMap<>();

    public static final int DEFAULT_COOLDOWN = 60;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 0) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
            ProxiedPlayer senderPlayer = (ProxiedPlayer) sender;
            String playername;
            String uuidPlayer = UUIDFetcher.getUUID(args[0]);
            if(player != null || uuidPlayer != null) {
                if (player != null) {
                    uuidPlayer = player.getUniqueId().toString();
                    playername = player.getName();
                } else {
                    playername = args[0];
                }

                long secondsLeft = System.currentTimeMillis() - getCooldown(senderPlayer);

                if((TimeUnit.MILLISECONDS.toSeconds(secondsLeft) <= DEFAULT_COOLDOWN)) {
                    long timer = TimeUnit.MILLISECONDS.toSeconds(secondsLeft) - DEFAULT_COOLDOWN;
                    sender.sendMessage(msg("You cant use that commands for another " + timer*-1 + " seconds!"));
                    return;
                }


                if (!sender.hasPermission("fernc.namehistory.bypass")) {
                    FernCommands.getInstance().getLogger().info("Player does not have cooldown bypass");
                    setCooldown(senderPlayer,System.currentTimeMillis());

                }else FernCommands.getInstance().getLogger().info("Player has cooldown bypass, skipping.");





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

    public void setCooldown(ProxiedPlayer player, long time){
        if(time < 1) {
            cooldowns.remove(player);
        } else {
            cooldowns.put(player, time);
        }
    }

    public long getCooldown(ProxiedPlayer player){
        return cooldowns.getOrDefault(player, (long) 0);
    }

    private BaseComponent[] msg(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
