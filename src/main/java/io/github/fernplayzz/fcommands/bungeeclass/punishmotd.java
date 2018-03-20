package io.github.fernplayzz.fcommands.bungeeclass;

import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public class punishmotd implements Listener {
    ProxyServer getProxy = ProxyServer.getInstance();
    @SuppressWarnings("deprecation")
    @EventHandler
    public void motdcheck(ProxyPingEvent eping) {
        ServerPing pingResponse = eping.getResponse();
        InetSocketAddress hostAddress = eping.getConnection().getAddress();
        String playeruuid = eping.getConnection().getUUID();
        getProxy.getLogger().info("Pinged by " + hostAddress + " and uuid is " + playeruuid);
        while(PunishmentManager.get().isBanned(playeruuid)) {
            PunishmentManager.get().getBan(playeruuid);
            //PERM BAN
            if(PunishmentManager.get().getBan(playeruuid).getType() == PunishmentType.BAN) {
                pingResponse.setDescription(ChatColor.RED.BOLD + "YOU HAVE BEEN PERMANENTLY BANNED");
                eping.setResponse(pingResponse);
                //PERM IP_BAN
            }else if(PunishmentManager.get().getBan(playeruuid).getType() == PunishmentType.IP_BAN) {
                pingResponse.setDescription(ChatColor.RED.BOLD + "YOUR IP HAS BEEN PERMANENTLY BANNED");
                eping.setResponse(pingResponse);
                //TEMP BAN
            }else if(PunishmentManager.get().getBan(playeruuid).getType() == PunishmentType.TEMP_BAN) {
                pingResponse.setDescription(ChatColor.RED.BOLD + "YOU HAVE BEEN BANNED UNTIL " + PunishmentManager.get().getBan(playeruuid).getEnd());
                pingResponse.setFavicon(ChatColor.GREEN + "Remaining " + PunishmentManager.get().getBan(playeruuid).getDuration(false));
                    //TEMP IP_BAN
                }else if(PunishmentManager.get().getBan(playeruuid).getType() == PunishmentType.TEMP_IP_BAN) {
                    pingResponse.setDescription(ChatColor.RED.BOLD + "YOUR IP HAS BEEN BANNED UNTIL " + PunishmentManager.get().getBan(playeruuid).getEnd());
                    pingResponse.setFavicon(ChatColor.GREEN + "Remaining " + PunishmentManager.get().getBan(playeruuid).getDuration(false));
                    eping.setResponse(pingResponse);

            }

        }
        //eping.getConnection().
        //ProxiedPlayer Playername = ProxyServer.getInstance().getPlayer(uuid);
       // ProxyServer.getInstance().getLogger().info("This is who pinged ur server: " + uuid + " and the name is: " + Playername + " and also the adress: " + hostAddress);


    }
}
