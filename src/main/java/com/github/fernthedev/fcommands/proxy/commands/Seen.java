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
import com.github.fernthedev.preferences.api.PreferenceManager;
import com.github.fernthedev.preferences.api.command.PreferenceCommandUtil;
import com.github.fernthedev.preferences.api.config.PlayerPreferencesSingleton;

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
            Universal.debug(() -> "Requesting if " + p.getName() + " is vanished");

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
            } else {
                sender.sendMessage(new TextMessage("&aPlayer &2" + p.getName() + " &awas found. Player is currently online on server: " + p.getCurrentServerName()));
            }


        } else {
            UUID uuid = p.getUniqueId();

            if (uuid == null) {
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
                    time = format.format(date);

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

                    hourFormat.setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND, "/" + PreferenceCommandUtil.prefSetCommand(PluginPreferenceManager.NAMESPACE, PluginPreferenceManager.preferredTimezone) + " "));
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

            SeenPlayerValue seenPlayerValue = seenConfig.getConfigData().getPlayers(player.getUuid());

            if (seenPlayerValue == null) {
                seenPlayerValue = new SeenPlayerValue(new Date(), server);
                seenConfig.getConfigData().getPlayerValueMap().put(player.getUuid(), seenPlayerValue);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy dd HH:mm", Locale.US);

            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            String time = simpleDateFormat.format(new Date());


            seenPlayerValue.setServer(server);
            seenPlayerValue.setTime(new Date());

            Universal.debug(() -> "ferntime1 " + time);
            try {
                seenConfig.syncSave();
            } catch (ConfigLoadException e) {
                throw new InvalidCommandArgument(e.getMessage());
            }
        });
    }

}
