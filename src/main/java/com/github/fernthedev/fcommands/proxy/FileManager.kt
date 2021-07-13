package com.github.fernthedev.fcommands.proxy

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.config.common.exceptions.ConfigLoadException
import com.github.fernthedev.config.gson.GsonConfig
import com.github.fernthedev.fcommands.proxy.data.ConfigValues
import com.github.fernthedev.fcommands.proxy.data.IPDeleteValues
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues
import com.github.fernthedev.fcommands.proxy.data.SeenValues
import com.github.fernthedev.fernapi.universal.Universal
import java.io.File
import java.io.IOException
import java.util.*

// TODO: Make file manager instance and not object

class FileManager private constructor() {

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

    companion object {
        @JvmStatic
        lateinit var configManager: Config<ConfigValues>
            private set

        @JvmStatic
        lateinit var seenConfig: Config<SeenValues>
            private set

        @JvmStatic
        lateinit var ipConfig: Config<IPSaveValues>
            private set

        @JvmStatic
        lateinit var deleteIPConfig: Config<IPDeleteValues>
            private set

        /**
         * Just used to synchronise
         */
        @JvmStatic
        fun configLoad(config: Config<*>?) {
            config!!.syncLoad()
        }

        /**
         * Just used to synchronise
         */
        @JvmStatic
        fun configSave(config: Config<*>) {
            config.syncSave()
        }

        /**
         * Synchronise config instances
         * @param config
         * @return
         */
        @Synchronized
        fun <T> getConfig(config: Config<T>): Config<T> {
            return config
        }

        @JvmStatic
        val configValues: ConfigValues
            get() = configManager.configData

        @Synchronized
        @Throws(IOException::class)
        @JvmStatic
        fun loadFiles(file: WhichFile, silent: Boolean) {
            IllegalAccessError("This is deprecated. Just an error message").printStackTrace()
            //CHECK IF PLUGIN FOLDER EXISTS
            if (!Universal.getMethods().dataFolder.exists()) {
                Universal.getMethods().dataFolder.mkdir()
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
                if (!silent) Universal.getMethods().abstractLogger.info("Seen Config was reloaded  $file")
            }

            //IP
            val goIP = file == WhichFile.IP || file == WhichFile.ALL
            if (goIP) {
                configLoad(ipConfig)
                if (!silent) Universal.getMethods().abstractLogger.info("Ips was reloaded  $file")
            }


            //DeleteIP
            //IP
            val goIPDelete = file == WhichFile.DELETEIP || file == WhichFile.ALL
            if (goIPDelete) {
                configLoad(deleteIPConfig)
                if (!silent) Universal.getMethods().abstractLogger.info("deleteIps was reloaded  $file")
            }
        }

        init {
            try {
                configManager = GsonConfig(ConfigValues(), File(Universal.getMethods().dataFolder, "config.json"))
                configManager.load()
                deleteIPConfig = GsonConfig(IPDeleteValues(), File(Universal.getMethods().dataFolder, "ipdelete.json"))
                deleteIPConfig.load()
                ipConfig = GsonConfig(IPSaveValues(), File(Universal.getMethods().dataFolder, "ipdata.json"))
                ipConfig.load()
                seenConfig = GsonConfig(SeenValues(), File(Universal.getMethods().dataFolder, "seen.json"))
                seenConfig.load()
            } catch (e: ConfigLoadException) {
                throw IllegalStateException(e)
            }
        }
    }
}