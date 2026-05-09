package com.fongmi.android.tv.remote

import android.util.Log
import com.fongmi.android.tv.server.Nano
import fi.iki.elonen.NanoHTTPD
import org.json.JSONObject

class RemoteControlServer(port: Int) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri ?: return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
        val method = session.method
        val params = session.parms ?: emptyMap()

        return when {
            uri == "/api/status" -> handleStatus()
            uri == "/api/play" && method == Method.POST -> handlePlay(params)
            uri == "/api/stop" -> handleStop()
            uri == "/api/pause" -> handlePause()
            uri == "/api/resume" -> handleResume()
            uri == "/api/seek" && method == Method.POST -> handleSeek(params)
            uri == "/api/volume" && method == Method.POST -> handleVolume(params)
            uri == "/api/search" && method == Method.POST -> handleSearch(params)
            uri == "/api/config" -> handleConfig()
            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
        }
    }

    private fun handleStatus(): Response {
        val json = JSONObject().apply {
            put("status", "running")
            put("version", "5.4.1")
        }
        return newFixedLengthResponse(Response.Status.OK, "application/json", json.toString())
    }

    private fun handlePlay(params: Map<String, String>): Response {
        val url = params["url"] ?: return errorResponse("Missing url")
        val json = JSONObject().apply {
            put("action", "play")
            put("url", url)
        }
        return newFixedLengthResponse(Response.Status.OK, "application/json", json.toString())
    }

    private fun handleStop(): Response {
        return newFixedLengthResponse(Response.Status.OK, "application/json", """{"action":"stop"}""")
    }

    private fun handlePause(): Response {
        return newFixedLengthResponse(Response.Status.OK, "application/json", """{"action":"pause"}""")
    }

    private fun handleResume(): Response {
        return newFixedLengthResponse(Response.Status.OK, "application/json", """{"action":"resume"}""")
    }

    private fun handleSeek(params: Map<String, String>): Response {
        val position = params["position"] ?: return errorResponse("Missing position")
        return newFixedLengthResponse(Response.Status.OK, "application/json", """{"action":"seek","position":$position}""")
    }

    private fun handleVolume(params: Map<String, String>): Response {
        val volume = params["volume"] ?: return errorResponse("Missing volume")
        return newFixedLengthResponse(Response.Status.OK, "application/json", """{"action":"volume","volume":$volume}""")
    }

    private fun handleSearch(params: Map<String, String>): Response {
        val keyword = params["keyword"] ?: return errorResponse("Missing keyword")
        return newFixedLengthResponse(Response.Status.OK, "application/json", """{"action":"search","keyword":"$keyword"}""")
    }

    private fun handleConfig(): Response {
        return newFixedLengthResponse(Response.Status.OK, "application/json", """{"config":"ok"}""")
    }

    private fun errorResponse(msg: String): Response {
        return newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", """{"error":"$msg"}""")
    }
}
