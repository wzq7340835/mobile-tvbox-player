package com.fongmi.android.tv.server

import com.fongmi.android.tv.Constant
import java.net.ServerSocket

object Server {

    private var nano: Nano? = null

    fun start() {
        if (nano != null) return
        try {
            val port = findAvailablePort(Constant.SERVER_PORT)
            nano = Nano(port).apply { start() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        nano?.stop()
        nano = null
    }

    fun isRunning(): Boolean = nano != null

    private fun findAvailablePort(defaultPort: Int): Int {
        return try {
            ServerSocket(defaultPort).use { it.localPort }
        } catch (e: Exception) {
            ServerSocket(0).use { it.localPort }
        }
    }
}
