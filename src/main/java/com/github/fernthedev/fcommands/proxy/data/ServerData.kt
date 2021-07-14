package com.github.fernthedev.fcommands.proxy.data

import com.github.fernthedev.fernapi.universal.debugLog
import java.io.Serializable
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.*

/**
 * {
 * "name": "test2",
 * "address": "localhost",
 * "port": 25566,
 * timeout": 2
 * }
 */
data class ServerData constructor(
    val name: String,
    val addressPortPair: AddressPortPair,
    private var timeoutMS: Long = 0
) : Serializable {

    fun getTimeoutMS(): Long {
        if (timeoutMS <= 0) timeoutMS = 1
        return timeoutMS
    }


    @Volatile
    @Transient
    var online = false
        private set

    private fun clearPing(status: Boolean) {
        addressPingStatus.remove(addressPortPair)
        online = status
    }

    @Synchronized
    fun ping() {
        if (addressPingStatus.contains(addressPortPair)) {
            debugLog { "Already pinging" }
            return  // Already pinging
        }
        try {
            Socket().use { s ->
                addressPingStatus.add(addressPortPair)
                s.connect(
                    InetSocketAddress(addressPortPair.address, addressPortPair.port),
                    getTimeoutMS().toInt()
                )
                clearPing(true)
                if (!s.isClosed) s.close()
            }
        } catch (e: SocketTimeoutException) {
            clearPing(false)
            debugLog { "${e.message} Timed out Name: $name Port: ${addressPortPair.port} Time: ${getTimeoutMS()}" }
        } catch (e: SocketException) {
            clearPing(false)
            debugLog { "${e.message} Name: $name Port: ${addressPortPair.port} Time: ${getTimeoutMS()}" }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    data class AddressPortPair(
        val address: String,
        val port: Int = 0,
    )

    companion object {
        private val addressPingStatus = Collections.synchronizedList(ArrayList<AddressPortPair>())
    }

    init {
        if (timeoutMS <= 0) timeoutMS = 1
    }
}