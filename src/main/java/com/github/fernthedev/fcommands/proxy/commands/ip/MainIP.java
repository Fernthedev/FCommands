package com.github.fernthedev.fcommands.proxy.commands.ip;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.data.IPDeleteValues;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fcommands.proxy.data.ip.IPDeletePlayerValue;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CommandAlias("myip|meip")
@CommandPermission("fernc.myip")
public class MainIP extends BaseCommand {
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";


    public static void loadTasks() {
        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();
        Config<IPDeleteValues> deleteIPConfig = FileManager.getDeleteIPConfig();

        FileManager.configLoad(ipConfig);
        FileManager.configLoad(deleteIPConfig);

        deleteIPConfig.getConfigData().getPlayerDataValues().forEach((key, value) -> {
            String dateString = "";
            Timer timer = new Timer();

            Date deleteDate;

            Universal.debug(() -> "The date string is " + dateString);
            deleteDate = value.getDeleteDate();

            ////////////////////////////////////////////////////////////
            if (deleteDate != null) {
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
            }
        });
    }

    @Default
    public void showIPs(IFPlayer<?> issuer) {

        Config<IPSaveValues> ipconfig = FileManager.getIpConfig();
        Config<IPDeleteValues> deleteipconfig = FileManager.getDeleteIPConfig();

        String ip = issuer.getAddress().getHostString();
        ip = ip.replaceAll("\\.", " ");


        //String uuid = p.getUniqueId().toString();

        //ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
        FileManager.configSave(ipconfig);
        FileManager.configSave(deleteipconfig);
        //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);

        List<UUID> players = ipconfig.getConfigData().getPlayers(ip);

        IPDeletePlayerValue deletePlayerValue = deleteipconfig.getConfigData().getPlayerValue(ip);

        if (deletePlayerValue != null && deletePlayerValue.isHidden()) {
            issuer.sendMessage(TextMessage.fromColor("&7Your ip is going to be deleted soon. It will be deleted in at least one hour."));
        }

        issuer.sendMessage(TextMessage.fromColor("&aYou are currently connected from: " + ip.replaceAll(" ", ".")));
        issuer.sendMessage(TextMessage.fromColor("&cAny other ip you have connected from will not be shown from this ip."));
        issuer.sendMessage(TextMessage.fromColor("&6If you want to delete this information, use the command \"/myip delete\""));
        issuer.sendMessage(TextMessage.fromColor("&6It would be then deleted at least 1 hour after the command."));
        issuer.sendMessage(TextMessage.fromColor("&bAccounts:"));
        if (deletePlayerValue == null || !deletePlayerValue.isHidden()) {
            for (UUID player : players) {
                String player1 = UUIDFetcher.getName(player.toString());
                //ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(player);
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
    @Subcommand("delete")
    @CommandPermission("fernc.myip.delete")
    public void deleteIP(IFPlayer<?> sender) {
        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();
        Config<IPDeleteValues> deleteIPConfig = FileManager.getDeleteIPConfig();

        String ipe = sender.getAddress().getHostString();
        final String ip = ipe.replaceAll("\\.", " ");

        FileManager.configLoad(ipConfig);
        FileManager.configLoad(deleteIPConfig);

        IPDeletePlayerValue value = deleteIPConfig.getConfigData().getPlayerValue(ip);


        if (value != null && value.isHidden()) {
            sender.sendMessage(TextMessage.fromColor("&aYou have already asked to delete your ip"));
        } else {

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

            save(deleteIPConfig);

            Timer timer = new Timer();

            String setTimee = dateFormat.format(hideDeleteDate);
            Universal.getMethods().getAbstractLogger().info("The time to hide delete is " + setTimee);
            setTimee = dateFormat.format(deleteDate);
            Universal.getMethods().getAbstractLogger().info("The time to delete is " + setTimee);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    deleteIp(ip);
                }
            }, TimeUnit.DAYS.toMillis(7));

            sender.sendMessage(TextMessage.fromColor("&aSuccessfully requested delete. It will be deleted in at least an hour"));
        }
    }

    @SneakyThrows
    private static void deleteIp(String ip) {
        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();
        Config<IPDeleteValues> deleteIPConfig = FileManager.getDeleteIPConfig();

        ipConfig.syncLoad();
        deleteIPConfig.syncLoad();

        IPDeletePlayerValue p = deleteIPConfig.getConfigData().getPlayerValue(ip);

        deleteIPConfig.getConfigData().removePlayerValue(ip);
        ipConfig.getConfigData().removePlayer(ip);

        save(ipConfig, deleteIPConfig);

        if (p != null)
            Universal.debug("Deleted ip on all files " + ip + " requested by " + p.getRequester());
    }

    private static void save(Config<?> config, Config<?>... configs) {
        FileManager.configSave(config);
        for (Config<?> c : configs) {
            FileManager.configSave(c);
        }
    }
}
