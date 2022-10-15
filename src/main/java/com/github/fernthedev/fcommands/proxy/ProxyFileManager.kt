package com.github.fernthedev.fcommands.proxy

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.config.common.exceptions.ConfigLoadException
import com.github.fernthedev.config.gson.GsonConfig
import com.github.fernthedev.fcommands.proxy.data.ConfigValues
import com.github.fernthedev.fcommands.proxy.data.IPDeleteValues
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues
import com.github.fernthedev.fcommands.proxy.data.SeenValues
import com.github.fernthedev.fernapi.universal.debugLog
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

enum class WhichFile(private val value: String) {
    IP("ip"), CONFIG("config"), SEEN("Seen"), DELETEIP("ipdelete"), ALL("all");

    companion object {
        @JvmStatic
        fun fromString(value: String): WhichFile {
            return when (value.lowercase(Locale.getDefault())) {
                "ip" -> IP
                "config" -> CONFIG
                "seen" -> SEEN
                "ipdelete" -> DELETEIP
                "deleteip" -> DELETEIP
                "all" -> ALL
                else -> ALL
            }
        }
    }

    override fun toString(): String {
        return value
    }
}

@Singleton
class ProxyFileManager @Inject constructor(
    private val methodHandler: MethodInterface<*, *>) {

    val configManager: Config<ConfigValues>
    val seenConfig: Config<SeenValues>
    val ipConfig: Config<IPSaveValues>
    val deleteIPConfig: Config<IPDeleteValues>

    /**
     * Just used to synchronise
     */
    fun configLoad(config: Config<*>?) {
        config!!.syncLoad()
    }

    /**
     * Just used to synchronise
     */
    fun configSave(config: Config<*>) {
        config.syncSave()
    }


    @Synchronized
    @Throws(IOException::class)
    fun loadFiles(file: WhichFile) {
        IllegalAccessError("This is deprecated. Just an error message").printStackTrace()
        //CHECK IF PLUGIN FOLDER EXISTS
        if (!methodHandler.dataFolder.exists()) {
            methodHandler.dataFolder.mkdir()
        }
        val goConfig = file == WhichFile.CONFIG || file == WhichFile.ALL
        //CONFIG
        if (goConfig) {
            configLoad(configManager)
        }


        //SEEN
        val goSeen = file == WhichFile.SEEN || file == WhichFile.ALL
        if (goSeen) {
            configLoad(seenConfig)
            debugLog { "Seen Config was reloaded $file" }
        }

        //IP
        val goIP = file == WhichFile.IP || file == WhichFile.ALL
        if (goIP) {
            configLoad(ipConfig)
            debugLog {  "Ips was reloaded $file"}
        }


        //DeleteIP
        //IP
        val goIPDelete = file == WhichFile.DELETEIP || file == WhichFile.ALL
        if (goIPDelete) {
            configLoad(deleteIPConfig)
            debugLog { "deleteIps was reloaded $file"}
        }
    }

    init {
        try {
            configManager = GsonConfig(ConfigValues(), File(methodHandler.dataFolder, "config.json"))
            configManager.load()
            deleteIPConfig = GsonConfig(IPDeleteValues(), File(methodHandler.dataFolder, "ipdelete.json"))
            deleteIPConfig.load()
            ipConfig = GsonConfig(IPSaveValues(), File(methodHandler.dataFolder, "ipdata.json"))
            ipConfig.load()
            seenConfig = GsonConfig(SeenValues(), File(methodHandler.dataFolder, "seen.json"))
            seenConfig.load()
        } catch (e: ConfigLoadException) {
            throw IllegalStateException(e)
        }
    }

}