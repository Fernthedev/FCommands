package com.github.fernthedev.fcommands.bungee;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.fcommands.proxy.ProxyFileManager;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fcommands.proxy.data.PunishValues;
import com.github.fernthedev.fernapi.universal.Universal;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishMOTD implements Listener {

    @Inject
    private Config<IPSaveValues> ipConfig;

    @Inject
    private Config<ConfigValues> config;
    
    @Inject
    private ProxyFileManager proxyFileManager;

    @EventHandler(priority = EventPriority.HIGH)
    public void pingEvent(ProxyPingEvent eping) {
        TaskScheduler taskScheduler = ProxyServer.getInstance().getScheduler();

        if (!FernCommands.getHookManager().hasAdvancedBan() || !config.getConfigData().getPunishMotd()) return;


        String hostAddress = eping.getConnection().getAddress().getHostString().replaceAll("\\.", " ");

        ServerPing pingResponse = eping.getResponse();

        final boolean[] doneCheck = {false};


        List<UUID> players = ipConfig.getConfigData().getPlayers(hostAddress);

        if (players != null && !players.isEmpty()) {
            taskScheduler.runAsync((Plugin) Universal.getPlugin(), () -> {

                PunishValues punishValues = config.getConfigData().getPunishValues();


                for (UUID checkedPlayer : players) {
                    if (PunishmentManager.get().isBanned(checkedPlayer.toString().replace("-",""))) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
                        Punishment punishment = PunishmentManager.get().getBan(checkedPlayer.toString().replace("-",""));
                        //PERM BAN
                        BaseComponent message;

                        if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.IP_BAN) {
                            message = message(punishValues.getPermBan());
                            pingResponse.setDescriptionComponent(message);
                            eping.setResponse(pingResponse);
                            //PERM IP_BAN
                        } else if (punishment.getType() == PunishmentType.TEMP_BAN || punishment.getType() == PunishmentType.TEMP_IP_BAN) {
                            long time = punishment.getEnd();


                            String hms = sdf.format(time);

                            sdf.format(time - 1);
                            message = message(punishValues.getTempBan().replaceAll("%time%", hms));
                            pingResponse.setDescriptionComponent(message);


                            //TEMP IP_BAN
                            //  }
                        }
                        break;
                    }
                }

                doneCheck[0] = true;
            });
        } else {
            return;
        }

        int waitTime = (int) (players.size() * 0.6);

        if (waitTime > 20) waitTime = 30;

        if (waitTime < 2) waitTime = 2;

        while (!doneCheck[0]) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        eping.setResponse(pingResponse);
    }


    @EventHandler
    public void onLoginIp(PostLoginEvent event) {
        if (config.getConfigData().getCacheIps()) {
            Universal.getScheduler().runAsync(() -> {
                String ip = event.getPlayer().getAddress().getHostString().replaceAll("\\.", " ");
   
                proxyFileManager.configLoad(ipConfig);

                List<UUID> ipplist = ipConfig.getConfigData().getPlayers(ip);

                if (ipplist == null) ipplist = new ArrayList<>();

                if (!ipplist.contains(event.getPlayer().getUniqueId()))
                    ipplist.add(event.getPlayer().getUniqueId());

                ipConfig.getConfigData().getPlayerMap().put(ip, ipplist);
                try {
                    ipConfig.syncSave();
                } catch (ConfigLoadException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
    }

    @SuppressWarnings("unused")
    public BaseComponent[] message(String text, boolean no) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', text)).create();
    }

}
