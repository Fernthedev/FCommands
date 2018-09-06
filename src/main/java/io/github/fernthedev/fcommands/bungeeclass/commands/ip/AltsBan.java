package io.github.fernthedev.fcommands.bungeeclass.commands.ip;

import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import io.github.fernthedev.fcommands.bungeeclass.FileManager;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.utils.Punishment;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AltsBan implements Listener {

    @EventHandler
    public void onPunish(PunishmentEvent e) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(e.getPunishment().getUuid());

        if(player == null) {
            FernCommands.getInstance().getLogger().info("There was an issue finding a punished player with the uuid of " + e.getPunishment().getUuid());
            return;
        }

        Configuration ipconfig = new FileManager().getIpconfig();

        String uuidPlayer = player.getUniqueId().toString();

        String ip = player.getAddress().getHostString();
        ip = ip.replaceAll("\\.", " ");


        //String uuid = p.getUniqueId().toString();

        boolean ipfileloaded;

        try {
            new FileManager().loadFiles("ip",true);
            ipfileloaded = true;
        } catch (IOException ee) {
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



            for(String uuid : players) {
                ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                new Punishment(playerListUUID.getName(),playerListUUID.getUniqueId().toString(),e.getPunishment().getReason() + " &6From an alt which is " + ProxyServer.getInstance().getPlayer(e.getPunishment().getUuid()),e.getPunishment().getOperator(),e.getPunishment().getType(),e.getPunishment().getStart(),e.getPunishment().getEnd(),e.getPunishment().getCalculation(),-1);
            }
            }

    }

}
