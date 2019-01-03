package com.github.fernthedev.fcommands.bungeeclass.commands.ip;

import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fcommands.bungeeclass.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowAlts extends Command {
    public ShowAlts() {
        super("accounts", "fernc.accounts.see", "acc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 0) {
            String plArgs = args[0];
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(plArgs);
            String playername;
            String uuidPlayer = UUIDFetcher.getUUID(args[0]);
            String ip = null;

            if(player != null || uuidPlayer != null) {
                if(player != null) {
                    uuidPlayer = player.getUniqueId().toString();
                    playername = player.getName();
                    ip = player.getAddress().getHostString();
                    ip = ip.replaceAll("\\.", " ");
                }else{
                    playername = args[0];
                }

                uuidPlayer = uuidPlayer.replaceAll("-","");

                Configuration ipconfig = new FileManager().getIpconfig();




                //String uuid = p.getUniqueId().toString();

                boolean ipfileloaded;

                try {
                    new FileManager().loadFiles("ip",true);
                    ipfileloaded = true;
                } catch (IOException e) {
                    FernCommands.getInstance().getLogger().warning("Unable to load ips");
                    ipfileloaded = false;
                }

                if(ipfileloaded) {
                    List<String> players = new ArrayList<>();
                    List<String> ips = new ArrayList<>();



                    players.add(uuidPlayer);


                    for(String ipe : ipconfig.getKeys()) {
                        List<String> playips = ipconfig.getStringList(ipe);
                        //print("The players for ip " + ipe + " has uuids " + playips.toString());

                        if (!playips.isEmpty()) {
                            //ips.add(ipe);
                            boolean keepgoing = true;
                            for (String pluuid : playips) {
                                if (keepgoing) {
                                    pluuid = pluuid.replaceAll("-", "");
                                    //print("The ip " + ipe + " has player uuid " + uuidPlayer + " with name " + name);
                                    if (pluuid.equals(uuidPlayer)) {
                                        ips.add(ipe);
                                        keepgoing = false;
                                    }
                                }
                            }
                        }
                    }

                    List<String> checkedPlayers = new ArrayList<>();
                    checkedPlayers.add(uuidPlayer);
                    sender.sendMessage(msg("&aSuccessfully found the player's alts. &bThe list is: "));

                    for(String ipe : ips) {
                        List<String> playips = ipconfig.getStringList(ipe);
                        for (String uuid : playips) {
                            boolean keeprun = true;
                            if (checkedPlayers.contains(uuid)) keeprun = false;

                            if (keeprun) {
                                FernCommands.getInstance().getLogger().info("This uuid is " + uuid + " getting name now.");
                                String playername2 = UUIDFetcher.getName(uuid);
                                FernCommands.getInstance().getLogger().info("Found that " + uuid + " is player " + playername2);
                                //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                                if (uuid == null || playername2 == null) return;
                                FernCommands.getInstance().getLogger().info("&3-&b" + playername2);
                                FernCommands.getInstance().getLogger().info(sender.getName());
                                sender.sendMessage(msg("&3-&b" + playername2));
                                checkedPlayers.add(uuid);
                            }
                        }
                    }

                    sender.sendMessage(msg("&6The ips are from: "));

                    for(String ipString : ips) {
                        sender.sendMessage(msg("&4-&c" + ipString));
                    }


                }else{
                    sender.sendMessage(msg("&cUnable to load ip file."));
                }

            }else{
                sender.sendMessage(msg("&cThe player you provided was not found."));
            }
        }else{
            sender.sendMessage(msg("&cYou provided no player to show alts."));
        }
    }


    private BaseComponent[] msg(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
