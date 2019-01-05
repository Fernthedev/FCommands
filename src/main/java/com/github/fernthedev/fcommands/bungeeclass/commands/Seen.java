package com.github.fernthedev.fcommands.bungeeclass.commands;

import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fcommands.bungeeclass.FileManager;
import com.github.fernthedev.fcommands.bungeeclass.MessageRunnable;
import com.github.fernthedev.fcommands.bungeeclass.placeholderapi.AskPlaceHolder;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Logger;

public class Seen extends Command implements Listener {
    private static File seenfile = FernCommands.getInstance().getSeenfile();
    private static Logger getLogger = FernCommands.getInstance().getLogger();
    private static FernCommands bungeee = new FernCommands();



    public Seen() {
        super("Seen", "fernc.Seen", "saw", "swho");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        try {
            FileManager.getInstance().loadFiles("Seen", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration seenconfig = new FileManager().getSeenconfig();
        if (strings.length == 0) {
            sender.sendMessage(new FernCommands().message("&cPlease use the command as followed: /Seen <player>"));

        } else {
            String ptarget = strings[0];

            //String UUID = UUIDManager.get().getUUID(ptarget);
            UUID UUIDE = UUID.nameUUIDFromBytes(("OfflinePlayer:" + ptarget).getBytes(StandardCharsets.UTF_8));
            String UUID = UUIDE.toString();
            //String UUID = ProxyServer.getInstance().getPlayer(ptarget).getUniqueId().toString();
            if (ProxyServer.getInstance().getPlayer(ptarget) != null) {
                getLogger.info("Requesting if " + ProxyServer.getInstance().getPlayer(ptarget).getDisplayName() + " is vanished");

                if(!sender.hasPermission("sv.see")) {
                    AskPlaceHolder askPlaceHolder = new AskPlaceHolder(ProxyServer.getInstance().getPlayer(ptarget), "%fvanish_isvanished%");
                    askPlaceHolder.setRunnable(new MessageRunnable() {
                        @Override
                        public void run() {
                            super.run();
                            getLogger.info("Player " + ProxyServer.getInstance().getPlayer(ptarget).getDisplayName() + " is " + askPlaceHolder.getPlaceHolderResult() + " and is replaced is " + askPlaceHolder.isPlaceHolderReplaced());
                            getLogger.info("The player was " + askPlaceHolder.getPlaceHolderResult());
                            if (!askPlaceHolder.isPlaceHolderReplaced() || askPlaceHolder.getPlaceHolderResult() == null) {
                                sender.sendMessage(bungeee.message("&cThere was an error trying to find this player. Please try again later"));
                            } else {
                                if (askPlaceHolder.getPlaceHolderResult().equalsIgnoreCase("vanished")) {
                                    sender.sendMessage(bungeee.message("&cThere was an error trying to find this player. Please try again later"));
                                } else {
                                    sender.sendMessage(bungeee.message("&aPlayer &2" + ptarget + " &awas found. Player is currently online on server: " + ProxyServer.getInstance().getPlayer(ptarget).getServer().getInfo().getName()));
                                }
                            }
                        }
                    });
                }else{
                    sender.sendMessage(bungeee.message("&aPlayer &2" + ptarget + " &awas found. Player is currently online on server: " + ProxyServer.getInstance().getPlayer(ptarget).getServer().getInfo().getName()));
                }




                /*
                getLogger.info("Player " + ProxyServer.getInstance().getPlayer(ptarget).getDisplayName() + " is " + askPlaceHolder.getPlaceHolderResult() + " and is replaced is " + askPlaceHolder.isPlaceHolderReplaced());

                if(!askPlaceHolder.isPlaceHolderReplaced() || askPlaceHolder.getPlaceHolderResult() == null) {
                    sender.sendMessage(bungeee.message("&cThere was an error trying to find this player. Please try again later"));
                }else{
                    if (askPlaceHolder.getPlaceHolderResult().equalsIgnoreCase("vanished") && !sender.hasPermission("sv.see")) {
                        sender.sendMessage(bungeee.message("&cThere was an error trying to find this player. Please try again later"));
                    } else {
                        sender.sendMessage(bungeee.message("&aPlayer &2" + ptarget + " &awas found. Player is currently online on server: " + ProxyServer.getInstance().getPlayer(ptarget).getServer().getInfo().getName()));
                    }
                }*/

            } else {
                if (UUID.isEmpty()) {
                    sender.sendMessage(bungeee.message("&cPlayer doesn't exist. You sure you typed that right?"));
                } else {

                    Configuration seenplist = seenconfig.getSection(UUID);
                    if (seenplist != null) {
                        String server = seenplist.getString("server");
                        String time = seenplist.getString("time");
                        time = time.replace(".", ":");
                        if (server.equals("")) {
                            server = "&cNo Server found";
                        }
                        if (time.equals("")) {
                            time = "&cNo time shown";
                        }
                        TextComponent messageserver = new TextComponent(bungeee.message("&bLast Server On: &3" + server));
                        messageserver.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + server));
                        messageserver.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bungeee.message("&aClick to head to server. &2(" + server + ")")));
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
        if(e.getPlayer().getServer() == null) return;
        try {
            FileManager.getInstance().loadFiles("Seen",true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Configuration seenConfig = new FileManager().getSeenconfig();
        ProxiedPlayer player = e.getPlayer();
        String playerE = player.getDisplayName();
        //String uuid = UUIDManager.get().getUUID(playerE);
        String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerE).getBytes(StandardCharsets.UTF_8)).toString();


//        Configuration sconfig = new FileManager().getConfig();
        try {
            FileManager.getInstance().loadFiles("Seen",true);
        } catch (IOException e1) {
            getLogger.warning("unable to load Seen file");
        }

        Configuration seenplist = seenConfig.getSection(uuid);
        //Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy dd hh:mm aa",Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String time = simpleDateFormat.format(new Date());

        String server = e.getPlayer().getServer().getInfo().getName();

        seenplist.set("time",time.replace(":","."));
        seenplist.set("server",server);

        getLogger.info("ferntime1 " + time);

        for(String ee : seenplist.getKeys()) {
            getLogger.info(ee + " fern");
        }
        seenConfig.set(uuid,null);
        seenConfig.set(uuid,seenplist);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(seenConfig,seenfile);
            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
        } catch (IOException ee) {
            ee.printStackTrace();
            getLogger.info("Unable to save Seen.yml file");
        }
    }


    public static void onDisable() {
        seenfile = null;
        getLogger = null;
        bungeee = null;
    }

}
