package com.github.fernthedev.fcommands.proxy.commands.ip;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.util.ListUtil;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import com.github.fernthedev.fernutils.thread.ThreadUtils;
import com.github.fernthedev.fernutils.thread.multiple.TaskInfoList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.time.StopWatch;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@CommandAlias("accounts|acc")
@Description("Show alts of a player")
@CommandPermission("fernc.accounts")
public class ShowAlts extends BaseCommand {


    @Description("Show alts of a player")
    @Default
    @CommandCompletion("@players")
    public void showAlts(FernCommandIssuer sender, @Flags("other,offline") IFPlayer<?> player) {
        UUID uuidPlayerCheck = player.isPlayerNull() ? UUIDFetcher.getUUID(player.getName()) : player.getUuid();

        if (uuidPlayerCheck == null) {
            sender.sendMessage(TextMessage.fromColor("&cUnable to find player."));
            return;
        }

        Universal.getScheduler().runAsync(() -> {
            StopWatch stopWatch = StopWatch.createStarted();
            IPUUIDLists scannedList = scan(uuidPlayerCheck);
            stopWatch.stop();

            sender.sendMessage(TextMessage.fromColor("&aSuccessfully found the player's alts. (Took &3" + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms&a) &bThe list is: "));

            Universal.debug("Found IPs " + scannedList.ips);

            List<String> names = new ArrayList<>();

            for (UUID uuid : scannedList.uuids) {
                String playerName = UUIDFetcher.getName(uuid);
                Universal.debug("Found that " + uuid + " is player " + playerName);
                //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                if (playerName == null) continue;


                names.add(playerName);
            }

            for (String name : ListUtil.sortAz(names)) {
                sender.sendMessage(TextMessage.fromColor("&3-&b" + name));
            }

            sender.sendMessage(TextMessage.fromColor("&6The ips are from: "));

            for (String ipString : ListUtil.sortAz(new ArrayList<>(scannedList.ips))) {
                sender.sendMessage(TextMessage.fromColor("&4-&c" + ipString));
            }
        });


    }


    private IPUUIDLists scan(UUID uuid) {
        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();
        FileManager.configLoad(ipConfig);

        Set<String> ips = getIPsThatMatch(uuid, new HashSet<>());
        Set<UUID> uuids = getPlayers(ips, new HashSet<>());

        int oldIPSize = -1; // Set to -1 to force to do at least 1 recursive scan
        int oldUUIDSize = -1;


        while (oldIPSize != ips.size() || oldUUIDSize != uuids.size()) {
            Universal.debug("Scanning old size " + oldIPSize + ":" + oldUUIDSize + " now it is " + ips.size() + ":" + uuids.size());
            Universal.debug("List is currently: " + ips + ":" + uuids);
            oldIPSize = ips.size();
            oldUUIDSize = uuids.size();
            try {
                TaskInfoList uuidThreads = ThreadUtils.runForLoopAsync(uuids, uuid1 -> {
                    Universal.debug("Scanning for IPs of " + uuid1);
                    ips.addAll(getIPsThatMatch(uuid1, ips));
                });

                uuidThreads.runThreads(Executors.newCachedThreadPool());
                uuidThreads.awaitFinish(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            Universal.debug("Scanning for UUIDs of ip " + ips);
            uuids.addAll(getPlayers(ips, uuids));
            Universal.debug("Finished scanning old size " + oldIPSize + ":" + oldUUIDSize + " now it is " + ips.size() + ":" + uuids.size());
            Universal.debug("Result is: " + ips + ":" + uuids);
        }

        return new IPUUIDLists(ips, uuids);
    }

    private Set<String> getIPsThatMatch(UUID uuid, Set<String> ignoredIPs) {

        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();

        Set<String> ips = new HashSet<>();


        for (String ipKey : ipConfig.getConfigData().getPlayerMap().keySet()) {

            if (ignoredIPs.contains(ipKey)) continue;

            List<UUID> playerUUIDListFromIP = ipConfig.getConfigData().getPlayers(ipKey);

            if (playerUUIDListFromIP != null && !playerUUIDListFromIP.isEmpty()) {
                boolean keepgoing = true;
                for (UUID playerUUID : playerUUIDListFromIP) {
                    if (keepgoing && playerUUID.equals(uuid)) {

                        ips.add(ipKey);
                        keepgoing = false;

                    }
                }
            }
        }

//        ips.addAll(ignoredIPs);

        return ips;
    }

    private Set<UUID> getPlayers(Set<String> ips, Set<UUID> ignoredUUIDs) {
        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();

        Set<UUID> playersFound = new HashSet<>();

        for (String ipFromList : ips) {

            List<UUID> uuidPlayersFromIP = ipConfig.getConfigData().getPlayers(ipFromList);

            Universal.debug("Checking for ip " + ipFromList + " the uuids " + uuidPlayersFromIP);

            if (uuidPlayersFromIP != null) {
                for (UUID uuid : uuidPlayersFromIP) {

                    if (ignoredUUIDs.contains(uuid)) continue;

                    playersFound.add(uuid);
                }
            }
        }

//        playersFound.addAll(ignoredUUIDs);

        return playersFound;
    }

    @AllArgsConstructor
    @Getter
    @Data
    private static class IPUUIDLists {
        private final Set<String> ips;
        private final Set<UUID> uuids;
    }
}
