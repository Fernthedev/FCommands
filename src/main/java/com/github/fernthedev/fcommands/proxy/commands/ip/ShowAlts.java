package com.github.fernthedev.fcommands.proxy.commands.ip;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fcommands.proxy.ProxyFileManager;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.handlers.IScheduler;
import com.github.fernthedev.fernapi.universal.util.ListUtil;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import org.apache.commons.lang3.time.StopWatch;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandAlias("accounts|acc")
@Description("Show alts of a player")
@CommandPermission("fernc.accounts")
public class ShowAlts extends BaseCommand {

    @Inject
    private ProxyFileManager proxyFileManager;

    @Inject
    private IScheduler<?, ?> scheduler;

    @Inject
    private UUIDFetcher uuidFetcher;

    @Description("Show alts of a player")
    @Default
    @CommandCompletion("@players")
    public void showAlts(FernCommandIssuer sender, @Flags("other,offline") IFPlayer<?> player) {
        UUID uuidPlayerCheck = player.getUuid();

        if (uuidPlayerCheck == null) {
            sender.sendMessage(TextMessage.fromColor("&cUnable to find player."));
            return;
        }

        scheduler.runAsync(() -> {
            StopWatch stopWatch = StopWatch.createStarted();
            IPAlgorithms.IPUUIDLists scannedList = IPAlgorithms.scan(uuidPlayerCheck, proxyFileManager);
            stopWatch.stop();

            sender.sendMessage(TextMessage.fromColor("&aSuccessfully found the player's alts. (Took &3" + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms&a) &bThe list is: "));

            APIHandler.debug("Found IPs " + scannedList.getIps());

            List<String> names = new ArrayList<>();

            for (UUID uuid : scannedList.getUuids()) {
                String playerName = uuidFetcher.getName(uuid);
                APIHandler.debug("Found that " + uuid + " is player " + playerName);
                if (playerName == null) continue;


                names.add(playerName);
            }

            for (String name : ListUtil.sortAz(names)) {
                sender.sendMessage(TextMessage.fromColor("&3-&b" + name));
            }

            sender.sendMessage(TextMessage.fromColor("&6The ips are from: "));

            for (String ipString : ListUtil.sortAz(new ArrayList<>(scannedList.getIps()))) {
                sender.sendMessage(TextMessage.fromColor("&4-&c" + ipString));
            }
        });


    }

}
