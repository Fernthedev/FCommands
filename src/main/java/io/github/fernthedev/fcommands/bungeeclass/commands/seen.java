package io.github.fernthedev.fcommands.bungeeclass.commands;

import io.github.fernthedev.fcommands.bungeeclass.bungee;
import io.github.fernthedev.fcommands.bungeeclass.fileconfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class seen extends Command implements Listener {
    Configuration reload;
    Configuration seenconfig = new bungee().getSeenconfig();
    private static final File pluginFolder = ProxyServer.getInstance().getPluginsFolder();
    private static Configuration seenDataConfig = new bungee().getSeendataConfig();
    private static Configuration configuration = new bungee().getConfiguration();
    private static ConfigurationProvider configp = new bungee().getConfigp();
    //public static Plugin plugin;
    private final ProxyServer getProxy = ProxyServer.getInstance();
    private File seenfile = bungee.getInstance().getSeenfile();
    private Logger getLogger = bungee.getInstance().getLogger();
    private bungee bungeee = new bungee();



    public seen() {
        super("seen", "fernc.seen", "saw", "swho");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        Configuration seenconfig = new bungee().getSeenconfig();
        if (strings.length == 0) {
            sender.sendMessage(new bungee().message("&cPlease use the namecolor as followed: /seen <player>"));

        } else {
            String ptarget = strings[0];
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).load(new FileInputStream(seenfile));
            } catch (IOException e) {
                getLogger.warning("unable to load seen config");
            }
            //String UUID = UUIDManager.get().getUUID(ptarget);
            UUID UUIDE = UUID.nameUUIDFromBytes(("OfflinePlayer:" + ptarget).getBytes(StandardCharsets.UTF_8));
            String UUID = UUIDE.toString();
            //String UUID = ProxyServer.getInstance().getPlayer(ptarget).getUniqueId().toString();
            if (ProxyServer.getInstance().getPlayer(ptarget) != null) {
                sender.sendMessage(bungeee.message("&aPlayer &2" + ptarget + " &awas found. Player is currently online on server: " + ProxyServer.getInstance().getPlayer(ptarget).getServer().getInfo().getName()));
            }else{
            if (UUID.isEmpty() || UUID == null) {
                sender.sendMessage(bungeee.message("&cPlayer doesn't exist. You sure you typed that right?"));
            }else{

                    List<String> seenplist = seenconfig.getStringList(UUID);
                    if (!seenplist.isEmpty()) {
                        String server = seenplist.get(1);
                        String time = seenplist.get(0);
                        time = time.replace(".", ":");
                        if (server == null) {
                            server = "&cNo Server found";
                        }
                        if (time == null) {
                            time = "&cNo time shown";
                        }
                        TextComponent messageserver = new TextComponent(bungeee.message("&bLast Server On: &3" + server));
                        messageserver.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/" + server));
                        messageserver.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,bungeee.message("&aClick to head to server. &2(" + server + ")")));
                        sender.sendMessage(bungeee.message("&aPlayer &2" + ptarget + " &awas found. Here is the info of player's last login:"));
                        sender.sendMessage(messageserver);
                        sender.sendMessage(bungeee.message("&9Last time on: &b" + time + "(UTC 12-hour Format)"));
                    } else {
                        sender.sendMessage(bungeee.message("&cPlayer has not played on the server, or info is not found about player."));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        Configuration seenconfig = new bungee().getSeenconfig();
        ProxiedPlayer player = e.getPlayer();
        String playere = player.getDisplayName();
        //String UUID = UUIDManager.get().getUUID(playere);
        UUID UUIDE = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playere).getBytes(StandardCharsets.UTF_8));
        String UUID = UUIDE.toString();
        List<String> seenplist = new ArrayList<>();
        Configuration sconfig = new bungee().getConfig();
        try {
            fileconfig.getInstance().loadFiles("seen");
        } catch (IOException e1) {
            getLogger.warning("unable to load seen file");
        }
        Calendar cal = Calendar.getInstance();
        String time= new SimpleDateFormat("MMM dd hh:mm aa").format(new Date());
        String server = e.getPlayer().getServer().getInfo().getName();
        //String time2 = new SimpleDateFormat("MM.dd HH:mm").format(date);
        //seenplist.add(time);
        seenplist.add("" + time.replace(":",".") + "");
        seenplist.add(server);
        //seenplist.add(e.getPlayer().getServer().getInfo().getName());
        //getLogger.info("ferntime1 " + time);
        //getLogger.info("ferntime2 " + time2);
        for(String ee : seenplist) {
            getLogger.info(ee + " fern");
        }
        seenconfig.set(UUID,null);
        seenconfig.set(UUID,seenplist);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(seenconfig,seenfile);
            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
        } catch (IOException ee) {
            ee.printStackTrace();
            getLogger.info("Unable to save seen.yml file");
        }
    }




}
