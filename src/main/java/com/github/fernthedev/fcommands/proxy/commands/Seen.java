package com.github.fernthedev.fcommands.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.data.SeenPlayerValue;
import com.github.fernthedev.fcommands.proxy.data.SeenValues;
import com.github.fernthedev.fcommands.universal.PluginPreferenceManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.*;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
import com.github.fernthedev.preferences.core.PreferenceManager;
import com.github.fernthedev.preferences.core.command.PreferenceCommand;
import com.github.fernthedev.preferences.core.config.PlayerPreferencesSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandAlias("seen|saw|swho")
@CommandPermission("fernc.seen")
public class Seen extends BaseCommand {


    private static final SimpleDateFormat hour24Format = new SimpleDateFormat("MMM-yyyy dd HH:mm", Locale.US);
    private static final SimpleDateFormat hour12Format = new SimpleDateFormat("MMM-yyyy dd hh:mm aa", Locale.US);

    @Default
    @Description("Looks up the last seen information of a player.")
    @CommandCompletion("* @nothing")
    public void execute(FernCommandIssuer sender, @Flags("other,offline") IFPlayer<?> p) {

        if (!p.isPlayerNull()) {
            Universal.debug("Requesting if " + p.getName() + " is vanished");

            if (!sender.hasVanishPermission()) {
                new VanishProxyCheck(p, (player, isVanished, timedOut) -> {
                    boolean doInfo = !isVanished;

                    if (timedOut || isVanished) {
                        doInfo = false;
                    }


                    if (!doInfo) {
                        sender.sendMessage(new TextMessage("&cThere was an error trying to find this player. Please try again later"));
                    }

                    if (doInfo) {
                        BaseMessage m = new TextMessage();

                        m.addExtra(new TextMessage("&aPlayer &2"));
                        m.addExtra(p.getName());
                        m.addExtra(new TextMessage("&awas found. Player is currently online on server: "));
                        m.addExtra(new TextMessage(p.getCurrentServerName()));

                        m.setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND, "/" + p.getCurrentServerName()));

                        sender.sendMessage(m);
                    }
                }).setTimeout(10, TimeUnit.SECONDS);

//                    ProxyAskPlaceHolder askPlaceHolder = new ProxyAskPlaceHolder(Universal.getMethods().convertPlayerObjectToFPlayer(ProxyServer.getInstance().getPlayer(ptarget)), "%fvanish_isvanished%");
//                    askPlaceHolder.setRunnable(() -> {
////                        getLogger.info("Player " + ProxyServer.getInstance().getPlayer(ptarget).getDisplayName() + " is " + askPlaceHolder.getPlaceHolderResult() + " and is replaced is " + askPlaceHolder.isPlaceHolderReplaced());
////                        getLogger.info("The player was " + askPlaceHolder.getPlaceHolderResult());
//                        if (!askPlaceHolder.isPlaceHolderReplaced() || askPlaceHolder.getPlaceHolderResult() == null) {
//                            sender.sendMessage(new TextMessage("&cThere was an error trying to find this player. Please try again later"));
//                        } else {
//                            if (askPlaceHolder.getPlaceHolderResult().equalsIgnoreCase("vanished")) {
//                                sender.sendMessage(new TextMessage("&cThere was an error trying to find this player. Please try again later"));
//                            } else {
//                                sender.sendMessage(new TextMessage("&aPlayer &2" + ptarget + " &awas found. Player is currently online on server: " + ProxyServer.getInstance().getPlayer(ptarget).getServer().getInfo().getName()));
//                            }
//                        }
//                    });
            } else {
                sender.sendMessage(new TextMessage("&aPlayer &2" + p.getName() + " &awas found. Player is currently online on server: " + p.getCurrentServerName()));
            }




                /*
                getLogger.info("Player " + ProxyServer.getInstance().getPlayer(ptarget).getDisplayName() + " is " + askPlaceHolder.getPlaceHolderResult() + " and is replaced is " + askPlaceHolder.isPlaceHolderReplaced());

                if(!askPlaceHolder.isPlaceHolderReplaced() || askPlaceHolder.getPlaceHolderResult() == null) {
                    sender.sendMessage(new TextMessage("&cThere was an error trying to find this player. Please try again later"));
                }else{
                    if (askPlaceHolder.getPlaceHolderResult().equalsIgnoreCase("vanished") && !sender.hasPermission("sv.see")) {
                        sender.sendMessage(new TextMessage("&cThere was an error trying to find this player. Please try again later"));
                    } else {
                        sender.sendMessage(new TextMessage("&aPlayer &2" + ptarget + " &awas found. Player is currently online on server: " + ProxyServer.getInstance().getPlayer(ptarget).getServer().getInfo().getName()));
                    }
                }*/

        } else {
            UUID uuid = p.getUniqueId(); // UUIDFetcher.getUUID(p.getName()).toString();

            if (uuid == null /* || uuid.isEmpty()*/) {
                sender.sendMessage(ChatColor.RED + "Player doesn't exist. You sure you typed that right?");
            } else {
                Config<SeenValues> seenconfig = FileManager.getSeenConfig();

                FileManager.configLoad(seenconfig);

                SeenPlayerValue seenplist = seenconfig.getConfigData().getPlayers(uuid);

                PlayerPreferencesSingleton pref;

                if (sender instanceof IFPlayer) {
                    IFPlayer<?> player = (IFPlayer<?>) sender;
                    pref = PreferenceManager.getPlayerPref(player.getUuid());
                } else pref = new PlayerPreferencesSingleton(UUID.randomUUID());

                if (seenplist != null) {
                    String server = seenplist.getServer();
//                        String time = seenplist.getString("time").replace("'", "").replace(".",":");

                    String time;

                    SimpleDateFormat format;

                    String hour;

                    if (pref.getPreferenceInfer(PluginPreferenceManager.NAMESPACE, PluginPreferenceManager.hour12Format.getName(), false).getValue()) {
                        format = (SimpleDateFormat) hour12Format.clone();
                        hour = "12";
                    } else {
                        format = (SimpleDateFormat) hour24Format.clone();
                        hour = "24";
                    }

                    TimeZone zone = TimeZone.getTimeZone(pref.getPreferenceInfer(PluginPreferenceManager.NAMESPACE, PluginPreferenceManager.preferredTimezone.getName(), "").getValue());
//                        try {
                    SimpleDateFormat hour24 = (SimpleDateFormat) hour24Format.clone();

                    hour24.setTimeZone(TimeZone.getTimeZone("UTC"));

                    Date date = null;

                    try {
                        date = hour24.parse(hour24.format(seenplist.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    format.setTimeZone(zone);
//                            Date date = hour24.parse(time); ////
                    time = format.format(date);


//                            time = format.format(date); ////


//                        time = time.replace(".", ":");


                    if (server.equals("")) {
                        server = "&cNo Server found";
                    }

                    if (time.equals("")) {
                        time = "&cNo time shown";
                    }

                    TextMessage messageServer = new TextMessage("&bLast Server On: &3" + server);
                    messageServer.setClickData(new ClickData(ClickData.Action.RUN_COMMAND, "/" + server));
                    messageServer.setHoverData(new HoverData(HoverData.Action.SHOW_TEXT, new TextMessage("&aClick to head to server. &2(" + server + ")")));
                    sender.sendMessage(new TextMessage("&aPlayer &2" + p.getName() + " &awas found. Here is the info of player's last login:"));

                    sender.sendMessage(messageServer);

                    BaseMessage hourFormat = new TextMessage("&b&n(%zone% %hour% - hour Format)"
                            .replace("%zone%", zone.getID())
                            .replace("%hour%", hour));

                    hourFormat.setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND, "/" + PreferenceCommand.prefSetCommand(PluginPreferenceManager.NAMESPACE, PluginPreferenceManager.preferredTimezone) + " "));
                    hourFormat.setHoverData(new HoverData(HoverData.Action.SHOW_TEXT, new TextMessage("&6Change default timezone")));

                    sender.sendMessage(new TextMessage("&9Last time on: &b" + time + " ").addExtra(hourFormat));
                } else {
                    sender.sendMessage(new TextMessage("&cPlayer has not played on the server, or info is not found about player."));
                }
            }
        }
    }



    public static void onLeave(IFPlayer<?> player) {
        if (player == null || player.isPlayerNull() || player.getServerInfo() == null) return;

        String server = player.getCurrentServerName();

        Universal.getScheduler().runAsync(() -> {

            Config<SeenValues> seenConfig = FileManager.getSeenConfig();
            FileManager.configLoad(seenConfig);
//            String playerE = player.getDisplayName();
            //String uuid = UUIDManager.get().getUUID(playerE);
//            String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerE).getBytes(StandardCharsets.UTF_8)).toString();


//        Configuration sconfig = LegacyFileManager.getConfig();

            SeenPlayerValue seenPlayerValue = seenConfig.getConfigData().getPlayers(player.getUuid());

            if (seenPlayerValue == null) {
                seenPlayerValue = new SeenPlayerValue(new Date(), server);
                seenConfig.getConfigData().getPlayerValueMap().put(player.getUuid(), seenPlayerValue);
            }
            //Calendar cal = Calendar.getInstance();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy dd HH:mm", Locale.US);

            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            String time = simpleDateFormat.format(new Date());


            seenPlayerValue.setServer(server);
            seenPlayerValue.setTime(new Date());


//            seenPlayerValue.set("time", time.replace(":", ".")); //time.replace(":","."));
//            seenPlayerValue.set("server",server);

            Universal.debug("ferntime1 " + time);

//            for(String s : seenPlayerValue.getKeys()) {
//                getLogger.info(s + " fern");
//            }
//            seenConfig.set(uuid, null);
//            seenConfig.set(uuid, seenPlayerValue);
//            try {
//                ConfigurationProvider.getProvider(YamlConfiguration.class).save(seenConfig, seenfile);
//                //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
//            } catch (IOException ee) {
//                ee.printStackTrace();
//                getLogger.info("Unable to save Seen.yml file");
//            }
            try {
                seenConfig.syncSave();
            } catch (ConfigLoadException e) {
                throw new InvalidCommandArgument(e.getMessage());
            }
        });
    }

//
//    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
//        if (args.length > 1) return null;
//
//        return returnPlayerComplete(args[args.length - 1]);
//    }
//
//    protected static Iterable<String> returnPlayerComplete(String curText) {
//        List<String> list = new ArrayList<>();
//
//        for (IFPlayer<?> player : Universal.getMethods().getPlayers()) {
//            if (player.getName().toLowerCase().contains(curText.toLowerCase())) {
//                list.add(player.getName());
//            }
//        }
//        return list;
//    }



}
