package io.github.fernthedev.fcommands.bungeeclass;

import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.manager.TimeManager;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

public class punishmotd implements Listener {
    private static final File pluginFolder = ProxyServer.getInstance().getPluginsFolder();
   // private static Configuration IpDataConfig = new bungee().getIpDataConfig();
    private static Configuration configuration = new bungee().getConfiguration();
    private static ConfigurationProvider configp = new bungee().getConfigp();
    //public static Plugin plugin;
    private final ProxyServer getProxy = ProxyServer.getInstance();
    private File ipfile;

    @SuppressWarnings({"deprecation", "InfiniteLoopStatement"})
    @EventHandler(priority = EventPriority.HIGH)
    public void motdcheck(ProxyPingEvent eping) {
        Boolean ipfileloaded;
        Configuration ipconfig = new bungee().getIpconfig();
        ServerPing pingResponse = eping.getResponse();
        String hostAddress = eping.getConnection().getAddress().getHostString();
        hostAddress = hostAddress.replaceAll("\\.", " ");
        List<String> players = ipconfig.getStringList(hostAddress);
        try {
            //ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            new fileconfig().loadFiles("ip");
            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
            ipfileloaded = true;
        } catch (IOException e) {
            getProxy.getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }
        if(ipfileloaded) {
           // for (String key : players) {
                //getProxy.getLogger().info("just the key below fern");
               // getProxy.getLogger().info(key);
          //  }
            if (players.isEmpty()) {
                getProxy.getLogger().warning("Unable to find player, new ip?" + hostAddress);
                //getProxy.getLogger().info("detected players: " + players);
               // getProxy.getLogger().info("192.168.2.11's players " + ipconfig.getStringList("192 168 2 11"));
            } else {
                if(players.toArray().length == 1) {
                    getProxy.getLogger().info("Pinged by " + hostAddress + " and uuid is " + players.toString());
                } else if(players.toArray().length >= 1) {
                    getProxy.getLogger().info("Pinged by " + hostAddress + " and uuids are " + players.toString());
                }
                for (String key : players) {
                    //getProxy.getLogger().info("Pinged by " + hostAddress + " and uuid is " + key);
                    if (PunishmentManager.get().isBanned(key)) {
                        PunishmentManager.get().getBan(key);
                        //PERM BAN
                        String messagee;
                        if (PunishmentManager.get().getBan(key).getType() == PunishmentType.BAN) {
                            getProxy.getLogger().info("Player pinged, and is permanently banned" + key);
                            messagee = message("&c&lYOU HAVE BEEN PERMANENTLY BANNED");
                            pingResponse.setDescription(messagee);
                            eping.setResponse(pingResponse);
                            //PERM IP_BAN
                        } else if (PunishmentManager.get().getBan(key).getType() == PunishmentType.IP_BAN) {
                            messagee = message("&c&lYOUR IP HAS BEEN PERMANENTLY BANNED");
                            pingResponse.setDescription(messagee);
                            eping.setResponse(pingResponse);
                            //TEMP BAN
                        } else if (PunishmentManager.get().getBan(key).getType() == PunishmentType.TEMP_BAN) {
                            long time = PunishmentManager.get().getBan(key).getEnd();
                            //String hms = String.format("%02d:%02d:%02d",
                            //        TimeUnit.MILLISECONDS.toHours(time),
                             //       TimeUnit.MILLISECONDS.toMinutes(time) -
                             //               TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), // The change is in this line
                            //        TimeUnit.MILLISECONDS.toSeconds(time) -
                            //                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));


                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
                            String hms = sdf.format(time);
                            for(sdf.format(time - 1); ;) {
                                messagee = message("&c&lYOU HAVE BEEN BANNED UNTIL " + hms);
                                pingResponse.setDescription(messagee);
                                String favicon = message("&a&lRemaining " + PunishmentManager.get().getBan(key).getDuration(false));
                                pingResponse.setFavicon(favicon);
                                eping.setResponse(pingResponse);
                                //TEMP IP_BAN
                            }
                        } else if (PunishmentManager.get().getBan(key).getType() == PunishmentType.TEMP_IP_BAN) {
                            long time = TimeManager.getTime() + PunishmentManager.get().getBan(key).getEnd();
                          //  String hms = String.format("%02d:%02d:%02d",
                             //       TimeUnit.MILLISECONDS.toHours(time),
                            //        TimeUnit.MILLISECONDS.toMinutes(time) -
                            ///                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                            //        TimeUnit.MILLISECONDS.toSeconds(time) -
                            //                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
                            String hms = sdf.format(time);
                            messagee =message("&c&lYOUR IP HAS BEEN BANNED UNTIL " + hms);
                            String favicon = message("&a&lRemaining " + PunishmentManager.get().getBan(key).getDuration(false));
                            pingResponse.setDescription(messagee);
                            pingResponse.setFavicon(favicon);
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

    @SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
    public void configip() {
        Logger log = getProxy.getLogger();
        File ipFile = new File(pluginFolder, "IPData.yml");
        //new bungee().loadIps();
        if (!pluginFolder.exists()) {
            try {
                pluginFolder.mkdir();
            } catch (Exception ex) {
                log.info("could not create folder");
            }
        }
        if (!ipFile.exists()) {
            try {
                ipFile.createNewFile();
            } catch (Exception ex) {
                log.info("could not create file");
            }
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    @EventHandler
    public void onLoginIp(PostLoginEvent event) {
        Logger log = getProxy.getLogger();
        String player = event.getPlayer().getUUID();
        ipfile = bungee.getIpfile();
        //String player = playerp.toString();
        String ip = event.getPlayer().getAddress().getHostString().replaceAll("\\.", " ");
        //ip.replaceAll("\\.", " ");
        log.info("proxy list fern" + ip + " " + player);
//     #   HashMap<String, String> ListIp = new HashMap<String, String>();
        //ArrayList<String> ListIp = (ArrayList<String>) IpDataConfig.getList(ip);
        /*for(Object s : IpDataConfig.getList(ip)) {
        if(IpDataConfig.getList(ip).contains(player)) {
            ListIp.put(ip,player);
        }
        }*/
        Configuration ipconfig = new bungee().getIpconfig();
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).load(new FileInputStream(ipfile));
       } catch (IOException e) {
            getProxy.getLogger().warning("unable to load config");
        }
        //String[] ipplayer = {player};
        //Map<String,String> ipplayer = new HashMap();
        List<String> ipplist = ipconfig.getStringList(ip);
       // ipplist.add(player);
        if(!ipconfig.getStringList(ip).contains(player)){
            //File ipfile = new bungee().getIpFile();
          // #  ListIp.put(ip, player);
        //    log.info("list fern" + ListIp);
            //IpDataConfig.set(IpDataConfig,Arrays.asList(ipplayer));
            Configuration config = new bungee().getConfig();
            //Configuration IpDataConfig = new bungee().getIpDataConfig();
            //IpDataConfig.set("ips",ipplayer);
                getProxy.getLogger().info("Saving new player " + player + " to ip " + ip);
                //ipconfig.set(ip, ipplayer);
                //ipconfig.getStringList(ip).add(player);
            try {
                ipconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            } catch (IOException e) {
                getProxy.getLogger().warning("unable to load file, saving anyways");
            }
            //ipplist.add(player);
            //ipconfig.getSection(ip).getKeys().addAll(Collections.singleton(player));
            ipplist.add(player);
            ipconfig.set(ip,ipplist);
            //ipconfig.getStringList(ip).add(player);
                //config.set(ip,ipplayer);
                try {
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig,ipfile);
                    //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("Saving new ip for: " + ip + " " + player);
                if(!ipconfig.getSection(ip).getKeys().contains(player)) {
                    getProxy.getLogger().warning("Unable to save ip");
                }
                //new bungee().saveIps();
                //new bungee().SaveIp(ipconfig);

                //config.set(ip, ListIp);
                //new bungee().saveIps();
           // }
        }
    }

    private String message(String text) {
        return text.replace("&","§");
    }



}
