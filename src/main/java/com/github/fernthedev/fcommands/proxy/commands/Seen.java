package com.github.fernthedev.fcommands.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.fcommands.proxy.ProxyEvents;
import com.github.fernthedev.fcommands.proxy.ProxyFileManager;
import com.github.fernthedev.fcommands.proxy.data.SeenPlayerValue;
import com.github.fernthedev.fcommands.proxy.data.SeenValues;
import com.github.fernthedev.fcommands.universal.PluginPreferenceManager;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.*;
import com.github.fernthedev.preferences.api.PreferenceManager;
import com.github.fernthedev.preferences.api.command.PreferenceCommandUtil;
import com.github.fernthedev.preferences.api.config.PlayerPreferencesSingleton;
import kotlin.Unit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

@CommandAlias("seen|saw|swho")
@CommandPermission("fernc.seen")
@Singleton
public class Seen extends BaseCommand {

    private final SimpleDateFormat hour24Format = new SimpleDateFormat("MMM-yyyy dd HH:mm", Locale.US);
    private final SimpleDateFormat hour12Format = new SimpleDateFormat("MMM-yyyy dd hh:mm aa", Locale.US);

    @Inject
    private Config<SeenValues> seenConfig;

    @Inject
    private ProxyFileManager proxyFileManager;

    @Inject
    private APIHandler apiHandler;

    @Inject
    public Seen(ProxyEvents proxyEvents) {
        proxyEvents.getOnLeave().register(this, ifPlayer -> {
            onLeave(ifPlayer);
            return Unit.INSTANCE;
        });
    }

    private void sendFoundUser(FernCommandIssuer sender, IFPlayer<?> p) {
        apiHandler.debug(() -> "Requesting if " + p.getName() + " is vanished");

        if (sender.hasVanishPermission()) {
            sender.sendMessage(new TextMessage("&aPlayer &2" + p.getName() + " &awas found. Player is currently online on server: " + p.getCurrentServerName()));
            return;
        }

        BaseMessage m = new TextMessage();

        m.addExtra(new TextMessage("&aPlayer &2"));
        m.addExtra(p.getName());
        m.addExtra(new TextMessage("&awas found. Player is currently online on server: "));
        m.addExtra(new TextMessage(p.getCurrentServerName()));

        m.setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND, "/" + p.getCurrentServerName()));

        sender.sendMessage(m);
    }

    private void sendLastSeen(FernCommandIssuer sender, IFPlayer<?> p) {
        UUID uuid = p.getUniqueId();

        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "Player doesn't exist. You sure you typed that right?");
            return;
        }

        proxyFileManager.configLoad(seenConfig);

        SeenPlayerValue seenPlayerValue = seenConfig.getConfigData().getPlayers(uuid);

        PlayerPreferencesSingleton pref;

        if (sender instanceof IFPlayer<?> player) {
            pref = PreferenceManager.getPlayerPref(player.getUuid());
        } else
            pref = new PlayerPreferencesSingleton(UUID.randomUUID());

        if (seenPlayerValue == null) {
            sender.sendMessage(new TextMessage("&cPlayer has not played on the server, or info is not found about player."));
            return;
        }

        String server = seenPlayerValue.getServer();


        // Parse time from config
        Date date = seenPlayerValue.getTime();

        // Get user's preference
        TimeZone preferredTimeZone = TimeZone.getTimeZone(pref.getPreferenceInfer(PluginPreferenceManager.NAMESPACE, PluginPreferenceManager.PREFERRED_TIMEZONE.getName(), "").getValue());

        SimpleDateFormat preferredDateFormat;
        String preferredHourFormat;

        if (pref.<Boolean>getPreferenceInfer(PluginPreferenceManager.NAMESPACE, PluginPreferenceManager.HOUR_12_FORMAT.getName()).getValue()) {
            preferredDateFormat = (SimpleDateFormat) hour12Format.clone();
            preferredHourFormat = "12";
        } else {
            preferredDateFormat = (SimpleDateFormat) hour24Format.clone();
            preferredHourFormat = "24";
        }


        preferredDateFormat.setTimeZone(preferredTimeZone);
        String time = preferredDateFormat.format(date);

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
                .replace("%zone%", preferredTimeZone.getID())
                .replace("%hour%", preferredHourFormat));

        hourFormat
                .setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND, "/" + PreferenceCommandUtil.prefSetCommand(PluginPreferenceManager.NAMESPACE, PluginPreferenceManager.PREFERRED_TIMEZONE)))
                .setHoverData(new HoverData(HoverData.Action.SHOW_TEXT, new TextMessage("&6Change default timezone")));

        sender.sendMessage(new TextMessage("&9Last time on: &b" + time + " ").addExtra(hourFormat));
    }

    @Default
    @Description("Looks up the last seen information of a player.")
    @CommandCompletion("* @nothing")
    public void execute(FernCommandIssuer sender, @Flags("other,offline") IFPlayer<?> p)  {
        if (!p.isPlayerNull()) {
            sendFoundUser(sender, p);
        } else {
            sendLastSeen(sender, p);
        }
    }

    public void onLeave(IFPlayer<?> player) {
        if (player == null || player.isPlayerNull() || player.getServerInfo() == null) return;

        String server = player.getCurrentServerName();

        apiHandler.getScheduler().runAsync(() -> {
            proxyFileManager.configLoad(seenConfig);

            SeenPlayerValue seenPlayerValue = seenConfig.getConfigData().getPlayers(player.getUuid());

            if (seenPlayerValue == null) {
                seenPlayerValue = new SeenPlayerValue(new Date(), server);
                seenConfig.getConfigData().getPlayerValueMap().put(player.getUuid(), seenPlayerValue);
            } else {
                seenPlayerValue.setServer(server);
                seenPlayerValue.setTime(new Date());
            }

            try {
                seenConfig.syncSave();
            } catch (ConfigLoadException e) {
                throw new InvalidCommandArgument(e.getMessage());
            }
        });
    }

}
