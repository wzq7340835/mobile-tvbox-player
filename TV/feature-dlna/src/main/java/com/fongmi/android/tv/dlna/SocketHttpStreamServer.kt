package com.fongmi.android.tv.dlna

import org.jupnp.transport.spi.StreamServer
import org.jupnp.transport.spi.StreamServerConfiguration
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket

class SocketHttpStreamServer(
    private val configuration: StreamServerConfiguration
) : StreamServer<StreamServerConfiguration> {

    private var serverSocket: ServerSocket? = null
    @Volatile
    private var running = false

    override fun getConfiguration(): StreamServerConfiguration = configuration
    override fun getPort(): Int = serverSocket?.localPort ?: 0

    override fun init(port: Int, ttl: Int) {
        serverSocket = ServerSocket(port)
        running = true
        Thread {
            while (running) {
                try {
                    val socket = serverSocket?.accept() ?: break
                    handleRequest(socket)
                } catch (e: Exception) {
                    if (running) e.printStackTrace()
                }
            }
        }.start()
    }

    override fun stop() {
        running = false
        serverSocket?.close()
        serverSocket = null
    }

    private fun handleRequest(socket: Socket) {
        try {
            val input = socket.getInputStream()
            val output = socket.getOutputStream()
            val request = readRequest(input)
            val response = buildHttpResponse("OK")
            output.write(response.toByteArray())
            output.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            socket.close()
        }
    }

    private fun readRequest(input: InputStream): String {
        val buffer = ByteArrayOutputStream()
        val data = ByteArray(4096)
        var count: Int
        while (input.read(data).also { count = it } != -1) {
            buffer.write(data, 0, count)
            if (count < data.size) break
        }
        return buffer.toString("UTF-8")
    }

    private fun buildHttpResponse(body: String): String {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/xml; charset=\"utf-8\"\r\n" +
                "Connection: close\r\n" +
                "Content-Length: ${body.toByteArray().size}\r\n" +
                "\r\n$body"
    }
}
