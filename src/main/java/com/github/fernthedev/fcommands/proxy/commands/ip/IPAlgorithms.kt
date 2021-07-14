package com.github.fernthedev.fcommands.proxy.commands.ip

import com.github.fernthedev.fcommands.proxy.FileManager.Companion.configLoad
import com.github.fernthedev.fcommands.proxy.FileManager.Companion.ipConfig
import com.github.fernthedev.fernapi.universal.debugLog
import com.github.fernthedev.fernutils.thread.ThreadUtils
import lombok.AllArgsConstructor
import lombok.Data
import lombok.Getter
import java.util.*
import java.util.function.Consumer

object IPAlgorithms {

    @JvmStatic
    fun scan(uuid: UUID): IPUUIDLists {
        val ipConfig = ipConfig
        configLoad(ipConfig)

        val ips = getIPsThatMatch(uuid, HashSet())
        val uuids = getPlayers(ips, HashSet())

        var oldIPSize = -1 // Set to -1 to force to do at least 1 recursive scan
        var oldUUIDSize = -1

        val scanUUID = Consumer { uuid1: UUID ->
            debugLog { "Scanning for IPs of $uuid1" }
            ips.addAll(getIPsThatMatch(uuid1, ips))
        }

        while (oldIPSize != ips.size || oldUUIDSize != uuids.size) {
            debugLog {  "Scanning old size $oldIPSize:$oldUUIDSize now it is ${ips.size}:${uuids.size}" }
            debugLog {"List is currently: $ips:$uuids" }

            oldIPSize = ips.size
            oldUUIDSize = uuids.size
            try {
                if (uuids.size > 2) {
                    val uuidThreads = ThreadUtils.runForLoopAsync(uuids, scanUUID)
                    uuidThreads.runThreads(ThreadUtils.ThreadExecutors.CACHED_THREADS.executorService)
                    uuidThreads.awaitFinish(5)
                } else {
                    uuids.forEach(scanUUID)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                e.printStackTrace()
            }
            debugLog { "Scanning for UUIDs of ip $ips" }
            uuids.addAll(getPlayers(ips, uuids))
            debugLog { "Scanning old size $oldIPSize:$oldUUIDSize now it is ${ips.size}:${uuids.size}" }
            debugLog { "Result is: $ips:$uuids" }
        }
        return IPUUIDLists(ips, uuids)
    }

    /**
     * Get IPs of the UUID specified
     */
    fun getIPsThatMatch(uuid: UUID, ignoredIPs: Set<String>): MutableSet<String> {
        val ipConfig = ipConfig
        val ips: MutableSet<String> = HashSet()
        iterateIps@ for (ipKey in ipConfig.configData.playerMap.keys) {
            if (ignoredIPs.contains(ipKey)) continue@iterateIps

            val playerUUIDListFromIP = ipConfig.configData.getPlayers(ipKey)

            if (playerUUIDListFromIP != null && playerUUIDListFromIP.isNotEmpty()) {

                findIp@ for (playerUUID in playerUUIDListFromIP) {
                    if (playerUUID == uuid) {
                        ips.add(ipKey)
                        break@findIp
                    }
                }
            }
        }
        return ips
    }

    /**
     * Get players from the IP list
     *
     * @param ignoredUUIDs UUIDs that we already know are part of the list
     */
    fun getPlayers(ips: Set<String>, ignoredUUIDs: Set<UUID>): MutableSet<UUID> {
        val ipConfig = ipConfig
        val playersFound: MutableSet<UUID> = HashSet()
        for (ipFromList in ips) {
            val uuidPlayersFromIP = ipConfig.configData.getPlayers(ipFromList)

            debugLog { "Checking for ip $ipFromList the uuids $uuidPlayersFromIP" }

            if (uuidPlayersFromIP != null) {
                for (uuid in uuidPlayersFromIP) {
                    if (ignoredUUIDs.contains(uuid)) continue
                    playersFound.add(uuid)
                }
            }
        }

        return playersFound
    }

    @AllArgsConstructor
    @Getter
    @Data
    data class IPUUIDLists(
        val ips: Set<String>,
        val uuids: Set<UUID>
    )
}