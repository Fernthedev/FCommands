package com.github.fernthedev.fcommands.bungee;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.fcommands.proxy.FileManager;
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
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishMOTD implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void MOTDCheck(ProxyPingEvent eping) {
        TaskScheduler taskScheduler = ProxyServer.getInstance().getScheduler();

        if (!FernCommands.getHookManager().hasAdvancedBan() || !FileManager.getConfigValues().getPunishMotd()) return;


        String hostAddress = eping.getConnection().getAddress().getHostString().replaceAll("\\.", " ");

        ServerPing pingResponse = eping.getResponse();

        final boolean[] doneCheck = {false};


        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();

        //ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
        //        FileManager.configLoad(ipConfig);
        //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);


        List<UUID> players = ipConfig.getConfigData().getPlayers(hostAddress);

        if (players != null && !players.isEmpty()) {
            taskScheduler.runAsync(FernCommands.getInstance(), () -> {


                // for (String key : players) {
                //getProxy.getLogger().info("just the key below fern");
                // getProxy.getLogger().info(key);
                //  }
//                FernCommands.getInstance().getLogger().info("Pinged by " + hostAddress + " and uuid is " + players.toString() + " the player names are " + playerNames.toString());

                PunishValues punishValues = FileManager.getConfigValues().getPunishValues();


                for (UUID checkedPlayer : players) {
                    if (PunishmentManager.get().isBanned(checkedPlayer.toString().replaceAll("-",""))) {
                        //getProxy.getLogger().info("Pinged by " + hostAddress + " and uuid is " + checkedPlayer);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
                        Punishment punishment = PunishmentManager.get().getBan(checkedPlayer.toString().replaceAll("-",""));
                        //PERM BAN
                        BaseComponent message;

                        if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.IP_BAN) {
//                                FernCommands.getInstance().getLogger().info("Player pinged, and is permanently banned" + checkedPlayer);
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
        //eping.getConnection().
        //ProxiedPlayer Playername = ProxyServer.getInstance().getPlayer(uuid);
        // ProxyServer.getInstance().getLogger().info("This is who pinged ur server: " + uuid + " and the name is: " + Playername + " and also the adress: " + hostAddress);


    }


    @EventHandler
    public void onLoginIp(PostLoginEvent event) {
        if (FileManager.getConfigValues().getCacheIps()) {
            Universal.getScheduler().runAsync(() -> {
//            log.info("Player " + event.getPlayer() + " has joined.");
//            log.info(event.getPlayer().getAddress().getHostString().replaceAll("\\.", " ") + " is the ip of player");

                String ip = event.getPlayer().getAddress().getHostString().replaceAll("\\.", " ");
//            log.info("proxy list fern" + ip + " " + player);


                Config<IPSaveValues> ipconfig = FileManager.getIpConfig();
                FileManager.configLoad(ipconfig);

                List<UUID> ipplist = ipconfig.getConfigData().getPlayers(ip);

                if (ipplist == null) ipplist = new ArrayList<>();

                if (!ipplist.contains(event.getPlayer().getUniqueId()))
                    ipplist.add(event.getPlayer().getUniqueId());

                ipconfig.getConfigData().getPlayerMap().put(ip, ipplist);
                try {
                    ipconfig.syncSave();
                } catch (ConfigLoadException e) {
                    e.printStackTrace();
                }
//                log.info("Saving new ip for: " + ip + " " + player);
            /*
            if(!ipconfig.getSection(ip).getKeys().contains(player)) {
                log.warning("Unable to save ip");
            }*/

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
