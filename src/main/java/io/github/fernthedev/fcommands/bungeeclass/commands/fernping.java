package io.github.fernthedev.fcommands.bungeeclass.commands;

import io.github.fernthedev.fcommands.bungeeclass.MessageRunnable;
import io.github.fernthedev.fcommands.bungeeclass.placeholderapi.AskPlaceHolder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class fernping extends Command {
    public fernping() {
        super("fernb","fern.test", "fernbungee");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length == 0) {
                ProxiedPlayer p = (ProxiedPlayer) sender;

                sender.sendMessage(message("&aYour FernPing is &9" + p.getPing()));
                //Player p = BUKKIT.getPlayer("PlayerName");
                // if (p != null) {
                // Do stuff
                //} else {
                // Player is not online
                //}
            /*} else {
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
                if (p.getDisplayName() != null && p.isConnected()) {
                    //is connected
                    sender.sendMessage(message("&c" + p + "'s &aFernPing is" + p.getPing()));
                } else {
                    sender.sendMessage(message("&cUnable to find &9" + p.getName()));
                }
                }*/


            } else {
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
                if (p != null) {
                    //is connected

                    if(p.hasPermission("sv.see")) {
                        sender.sendMessage(message("&c" + p + "'s &aFernPing is " + p.getPing()));
                    } else {
                        AskPlaceHolder askPlaceHolder = new AskPlaceHolder(p, "%fvanish_isvanished%");
                        askPlaceHolder.setRunnable(new MessageRunnable() {
                            @Override
                            public void run() {
                                super.run();
                                if (askPlaceHolder.getPlaceHolderResult().equalsIgnoreCase("Vanished")) {
                                    sender.sendMessage(message("&cUnable to find &9" + args[0]));
                                } else {
                                    sender.sendMessage(message("&c" + p + "'s &aFernPing is " + p.getPing()));
                                }
                            }
                        });
                    }

                } else {
                    sender.sendMessage(message("&cUnable to find &9" + args[0]));
                }

            }
      /*      if (args[0] == "version") {
               sender.sendMessage(message("&aVersion: " + ProxyServer.getInstance().getPluginManager().getPlugin(this).getDescription().getVersion()));
                sender.sendMessage(message("&aAuthor: " + ProxyServer.getInstance().getPluginManager().getPlugin(this).getDescription().getAuthor().toString()));
                sender.sendMessage(message("&aDescription: " + ProxyServer.getInstance().getPluginManager().getPlugin(this).getDescription().getDescription().toString()));

            }*/


        }else {
            sender.sendMessage(message("&cYou are not a player! &a#FernSaysThis"));

        }
    }
    //}

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();

    }
}
