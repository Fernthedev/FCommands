package com.github.fernthedev.fcommands.proxy.data

import com.github.fernthedev.fcommands.proxy.data.ServerData.AddressPortPair
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo


data class ConfigValues(
    var useMotd: Boolean = false,
    var serverChecks: MutableList<ServerData> = ArrayList(),
    var cacheIps: Boolean = false,
    var allowIPDelete: Boolean = false,
    var allowIPShow: Boolean = false,
    var showAltsCommand: Boolean = false,
    var allowNameHistory: Boolean = true,
    var altsBan: Boolean = false,
    var globalNicks: Boolean = false,
    var showPing: Boolean = false,
    var allowSeenCommand: Boolean = false,
    var punishMotd: Boolean = false,
    var offlineServerMotd: String = "&cSERVER UNDER MAINTENANCE!",
    var punishValues: PunishValues = PunishValues(),
    var databaseConnect: Boolean = false,
    var databaseAuthInfo: DatabaseAuthInfo = DatabaseAuthInfo(
        "root",
        "pass",
        "3306",
        "localhost",
        "database"
    )
) {

    init {
        if (serverChecks.isEmpty()) {
            serverChecks.add(ServerData("localhost", AddressPortPair("localhost", 25566), 5000))
        }
    }
}