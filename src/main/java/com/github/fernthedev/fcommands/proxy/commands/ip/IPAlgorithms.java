package com.github.fernthedev.fcommands.proxy.commands.ip;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernutils.thread.ThreadUtils;
import com.github.fernthedev.fernutils.thread.multiple.TaskInfoList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;

public class IPAlgorithms {



    public static IPUUIDLists scan(UUID uuid) {
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

    public static Set<String> getIPsThatMatch(UUID uuid, Set<String> ignoredIPs) {

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

    public static Set<UUID> getPlayers(Set<String> ips, Set<UUID> ignoredUUIDs) {
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
    public static class IPUUIDLists {
        private final Set<String> ips;
        private final Set<UUID> uuids;
    }

}
