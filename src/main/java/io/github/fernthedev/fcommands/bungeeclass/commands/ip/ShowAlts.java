package io.github.fernthedev.fcommands.bungeeclass.commands.ip;

import io.github.fernthedev.fcommands.Universal.UUIDFetcher;
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
                    List<String> players;
                    List<String> ips = new ArrayList<>();

                    if(ip != null) {
                       players = ipconfig.getStringList(ip);
                        ips.add(ip);
                    } else{
                        players = new ArrayList<>();
                    }

                    if(players.isEmpty()) {
                        players = new ArrayList<>();
                        players.add(UUIDFetcher.getUUID(playername));
                    }

                    for(String ipe : ipconfig.getKeys()) {
                        if (!ipe.equals(ip)) {
                            List<String> playips = ipconfig.getStringList(ipe);
                            if (!playips.isEmpty()) {
                                ips.add(ipe);
                                for (String pluuid : playips) {
                                    pluuid = pluuid.replaceAll("-","");
                                    if (pluuid.equals(uuidPlayer)) {
                                        ips.add(pluuid);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    sender.sendMessage(msg("&aSuccessfully found the player's alts. &bThe list is: "));

                    for(String uuid : players) {
                        FernCommands.getInstance().getLogger().info("This uuid is " + uuid + " getting name now.");
                         String playername2 = UUIDFetcher.getName(uuid);
                         FernCommands.getInstance().getLogger().info("Found that " + uuid + " is player " + playername2);
                        //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                        if(uuid == null || playername2 == null) return;
                        FernCommands.getInstance().getLogger().info("&3-&b" + playername2);
                        FernCommands.getInstance().getLogger().info(sender.getName());
                        sender.sendMessage(msg("&3-&b" + playername2));
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

    /*
    // Get Name from UUID
    public String getNameByUUIDe(String uuid) {
        URL url = null;
        InputStreamReader in = null;
        try {
            url = new URL("https://api.mojang.com/user/profiles/UUID/names".replace("UUID", uuid.replace("-", "")));
            try {
                in = new InputStreamReader(url.openStream());
                return FernCommands.getGson().fromJson(in, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(in != null)
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }*/


    private BaseComponent[] msg(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
