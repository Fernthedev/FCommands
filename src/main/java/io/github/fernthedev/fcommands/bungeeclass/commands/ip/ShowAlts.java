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
            if(ProxyServer.getInstance().getPlayer(plArgs) != null) {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(plArgs);

                Configuration ipconfig = new FileManager().getIpconfig();

                String uuidPlayer = player.getUniqueId().toString();

                String ip = player.getAddress().getHostString();
                ip = ip.replaceAll("\\.", " ");


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
                    List<String> players = ipconfig.getStringList(ip);

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
                        ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                        sender.sendMessage(msg("&3-&b" + playerListUUID.getName()));
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
