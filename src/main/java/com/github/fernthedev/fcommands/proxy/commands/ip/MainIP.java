package com.github.fernthedev.fcommands.proxy.commands.ip;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.ProxyFileManager;
import com.github.fernthedev.fcommands.proxy.data.IPDeleteValues;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fcommands.proxy.data.ip.IPDeletePlayerValue;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import lombok.SneakyThrows;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CommandAlias("myip|meip")
@CommandPermission("fernc.myip")
public class MainIP extends BaseCommand {
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @Inject
    private ProxyFileManager proxyFileManager;

    @Inject
    private Config<IPSaveValues> ipConfig;

    @Inject
    private Config<IPDeleteValues> deleteIPConfig;

    @Inject
    private UUIDFetcher uuidFetcher;

    @Inject
    private MethodInterface<?, ?> methodInterface;

    public void loadTasks() {
        proxyFileManager.configLoad(ipConfig);
        proxyFileManager.configLoad(deleteIPConfig);

        deleteIPConfig.getConfigData().getPlayerDataValues().forEach((key, value) -> {
            String dateString = "";
            Timer timer = new Timer();

            APIHandler.debug(() -> "The date string is " + dateString);
            Date deleteDate = value.getDeleteDate();

            ////////////////////////////////////////////////////////////
            if (deleteDate.before(new Date())) {

                deleteIp(key);

            } else {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        deleteIp(key);
                    }
                }, deleteDate);
            }
        });
    }

    @Default
    public void showIPs(IFPlayer<?> issuer) {
        String ip = issuer.getAddress().getHostString().replace("\\.", " ");


        proxyFileManager.configSave(ipConfig);
        proxyFileManager.configSave(deleteIPConfig);

        List<UUID> players = ipConfig.getConfigData().getPlayers(ip);

        if (players == null)
            return;

        IPDeletePlayerValue deletePlayerValue = deleteIPConfig.getConfigData().getPlayerValue(ip);

        if (deletePlayerValue != null && deletePlayerValue.isHidden()) {
            issuer.sendMessage(TextMessage.fromColor("&7Your ip is going to be deleted soon. It will be deleted in at least one hour."));
        }

        issuer.sendMessage(TextMessage.fromColor("&aYou are currently connected from: " + ip.replace(" ", ".")));
        issuer.sendMessage(TextMessage.fromColor("&cAny other ip you have connected from will not be shown from this ip."));
        issuer.sendMessage(TextMessage.fromColor("&6If you want to delete this information, use the command \"/myip delete\""));
        issuer.sendMessage(TextMessage.fromColor("&6It would be then deleted at least 1 hour after the command."));
        issuer.sendMessage(TextMessage.fromColor("&bAccounts:"));
        if (deletePlayerValue == null || !deletePlayerValue.isHidden()) {
            for (UUID player : players) {
                String player1 = uuidFetcher.getName(player.toString());
                if (player1 != null) {

                    issuer.sendMessage(TextMessage.fromColor("&9-&3" + player1));
                }
            }
        } else {
            issuer.sendMessage(TextMessage.fromColor("&9-&3" + issuer.getName()));
        }

    }
    

    /**
     * Called when executing the command
     *
     * @param sender The source
     */
    @SneakyThrows
    @Subcommand("delete")
    @CommandPermission("fernc.myip.delete")
    public void deleteIP(IFPlayer<?> sender) {
        String ipe = sender.getAddress().getHostString();
        final String ip = ipe.replace("\\.", " ");

        proxyFileManager.configLoad(ipConfig);
        proxyFileManager.configLoad(deleteIPConfig);

        IPDeletePlayerValue value = deleteIPConfig.getConfigData().getPlayerValue(ip);


        if (value != null && value.isHidden()) {
            sender.sendMessage(TextMessage.fromColor("&aYou have already asked to delete your ip"));
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR, 1); // adds one hour
        //cal.getTime(); // returns new date object, one hour in the future

        Date hideDeleteDate = cal.getTime();

        Calendar cal2 = Calendar.getInstance(); // creates calendar
        cal2.setTime(new Date()); // sets calendar time/date
        cal2.add(Calendar.WEEK_OF_MONTH, 1); // adds 7 days

        Date deleteDate = cal2.getTime(); // returns new date object, one hour in the future

        value = new IPDeletePlayerValue(hideDeleteDate, deleteDate);
        value.setRequester(sender.getUuid());

        deleteIPConfig.getConfigData().addPlayer(ip, value);

        proxyFileManager.configSave(deleteIPConfig);

        Timer timer = new Timer();

        String hiddenDeleteDateFormatted = dateFormat.format(hideDeleteDate);
        String deleteDateFormatted = dateFormat.format(deleteDate);

        methodInterface.getAbstractLogger().info("The time to hide delete is {}", hiddenDeleteDateFormatted);

        methodInterface.getAbstractLogger().info("The time to delete is {}", deleteDateFormatted);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                deleteIp(ip);
            }
        }, TimeUnit.DAYS.toMillis(7));

        sender.sendMessage(TextMessage.fromColor("&aSuccessfully requested delete. It will be deleted in at least an hour"));
    }

    @SneakyThrows
    private void deleteIp(String ip) {
        ipConfig.syncLoad();
        deleteIPConfig.syncLoad();

        IPDeletePlayerValue p = deleteIPConfig.getConfigData().getPlayerValue(ip);

        deleteIPConfig.getConfigData().removePlayerValue(ip);
        ipConfig.getConfigData().removePlayer(ip);

        proxyFileManager.configSave(ipConfig);
        proxyFileManager.configSave(deleteIPConfig);

        if (p != null)
            APIHandler.debug("Deleted ip on all files " + ip + " requested by " + p.getRequester());
    }


}
