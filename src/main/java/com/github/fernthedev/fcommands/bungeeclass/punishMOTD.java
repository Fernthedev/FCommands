package com.github.fernthedev.fcommands.bungeeclass;

import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.manager.TimeManager;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class punishMOTD implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void motdcheck(ProxyPingEvent eping) {

        boolean ipfileloaded;

        Configuration ipconfig = new FileManager().getIpconfig();
        ServerPing pingResponse = eping.getResponse();
        String hostAddress = eping.getConnection().getAddress().getHostString();
        hostAddress = hostAddress.replaceAll("\\.", " ");

        try {
            //ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            new FileManager().loadFiles("ip",true);
            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
            ipfileloaded = true;
        } catch (IOException e) {
            FernCommands.getInstance().getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }

        List<String> players = ipconfig.getStringList(hostAddress);

        if(ipfileloaded) {
           // for (String key : players) {
                //getProxy.getLogger().info("just the key below fern");
               // getProxy.getLogger().info(key);
          //  }
            if (players.isEmpty()) {
                FernCommands.getInstance().getLogger().warning("Unable to find player, new ip?" + hostAddress);
                //getProxy.getLogger().info("detected players: " + players);
               // getProxy.getLogger().info("192.168.2.11's players " + ipconfig.getStringList("192 168 2 11"));
            } else {
                    List<String> playernames = new ArrayList<>();
                    for(String uuid : players) {
                        uuid = uuid.replaceAll("-","");
                        String name = UUIDFetcher.getName(uuid);
                        playernames.add(name);
                    }
                    FernCommands.getInstance().getLogger().info("Pinged by " + hostAddress + " and uuid is " + players.toString() + " the player names are " + playernames.toString());
                for (String checkedPlayer : players) {
                    if (hooks.getInstance().hasAdvancedBan() && PunishmentManager.get().isBanned(checkedPlayer)) {
                        //getProxy.getLogger().info("Pinged by " + hostAddress + " and uuid is " + checkedPlayer);

                            PunishmentManager.get().getBan(checkedPlayer);
                            //PERM BAN
                            BaseComponent message;

                            if (PunishmentManager.get().getBan(checkedPlayer).getType() == PunishmentType.BAN) {
                                FernCommands.getInstance().getLogger().info("Player pinged, and is permanently banned" + checkedPlayer);
                                message = message("&c&lYOU HAVE BEEN PERMANENTLY BANNED");
                                pingResponse.setDescriptionComponent(message);
                                eping.setResponse(pingResponse);
                                //PERM IP_BAN
                            } else if (PunishmentManager.get().getBan(checkedPlayer).getType() == PunishmentType.IP_BAN) {
                                message = message("&c&lYOUR IP HAS BEEN PERMANENTLY BANNED");
                                pingResponse.setDescriptionComponent(message);
                                eping.setResponse(pingResponse);
                                //TEMP BAN
                            } else if (PunishmentManager.get().getBan(checkedPlayer).getType() == PunishmentType.TEMP_BAN) {
                                long time = PunishmentManager.get().getBan(checkedPlayer).getEnd();



                                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
                                String hms = sdf.format(time);

                                sdf.format(time - 1);
                                message = message("&c&lYOU HAVE BEEN BANNED UNTIL " + hms);
                                pingResponse.setDescriptionComponent(message);

                                eping.setResponse(pingResponse);
                                //TEMP IP_BAN
                                //  }
                            } else if (PunishmentManager.get().getBan(checkedPlayer).getType() == PunishmentType.TEMP_IP_BAN) {
                                long time = TimeManager.getTime() + PunishmentManager.get().getBan(checkedPlayer).getEnd();

                                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
                                String hms = sdf.format(time);
                                message = message("&c&lYOUR IP HAS BEEN BANNED UNTIL " + hms);


                                pingResponse.setDescriptionComponent(message);


                                eping.setResponse(pingResponse);

                            }
                        }
                }
            }
        }


        //eping.getConnection().
        //ProxiedPlayer Playername = ProxyServer.getInstance().getPlayer(uuid);
       // ProxyServer.getInstance().getLogger().info("This is who pinged ur server: " + uuid + " and the name is: " + Playername + " and also the adress: " + hostAddress);


    }


    @EventHandler
    public void onLoginIp(PostLoginEvent event) {
        Logger log = FernCommands.getInstance().getLogger();
        String player = event.getPlayer().getUniqueId().toString();
        File ipfile = FernCommands.getIpfile();
        log.info("Player " + event.getPlayer() + " has joined.");
        log.info(event.getPlayer().getAddress().getHostString().replaceAll("\\.", " ") + " is the ip of player");

        String ip = event.getPlayer().getAddress().getHostString().replaceAll("\\.", " ");
        log.info("proxy list fern" + ip + " " + player);


        try {
            FileManager.getInstance().loadFile(ipfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration ipconfig = new FileManager().getIpconfig();

        List<String> ipplist = ipconfig.getStringList(ip);
        if(!ipconfig.getStringList(ip).contains(player)){
                log.info("Saving new player " + player + " to ip " + ip);
            try {
                ipconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            } catch (IOException e) {
                log.warning("unable to load file, saving anyways");
            }
            ipplist.add(player);
            ipconfig.set(ip,ipplist);
                try {
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig, ipfile);
                    //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("Saving new ip for: " + ip + " " + player);
                /*
                if(!ipconfig.getSection(ip).getKeys().contains(player)) {
                    log.warning("Unable to save ip");
                }*/
        }
    }

    public BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }

    @SuppressWarnings("unused")
    public BaseComponent[] message(String text,boolean no) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }

}
