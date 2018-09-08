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
import java.io.InputStreamReader;
import java.net.URL;
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
            String uuidPlayer;
            String ip = null;

            if(player != null || !UUIDFetcher.getUUID(args[0]).equals("")) {
                if(player != null) {
                    uuidPlayer = player.getUniqueId().toString();
                    playername = player.getName();
                    ip = player.getAddress().getHostString();
                    ip = ip.replaceAll("\\.", " ");
                }else{
                    playername = UUIDFetcher.getName(UUIDFetcher.getUUID(args[0]));
                    uuidPlayer = UUIDFetcher.getUUID(args[0]);
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

                    if(ip != null) {
                       players =ipconfig.getStringList(ip);
                    } else{
                        players = new ArrayList<>();
                    }

                    if(players.isEmpty()) {
                        players = new ArrayList<>();
                        players.add(playername);
                    }

                    List<String> ips = new ArrayList<>();
                    ips.add(ip);

                    for(String ipe : ipconfig.getKeys()) {
                        if (!ipe.equals(ip)) {
                            List<String> playips = ipconfig.getStringList(ipe);
                            if (!playips.isEmpty() && playips.contains(uuidPlayer)) {
                                for (String pluuid : playips) {
                                    if (!players.contains(pluuid)) {
                                        players.add(pluuid);
                                    }
                                }
                            }
                        }
                    }

                    sender.sendMessage(msg("&aSuccessfully found the player's alts. &bThe list is: "));

                    for(String uuid : players) {
                         String playername2 = UUIDFetcher.getName(uuid);
                        //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                        if(uuid == null || playername2.equalsIgnoreCase("")) return;
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
