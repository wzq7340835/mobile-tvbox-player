package com.fongmi.android.tv.server

import android.net.Uri
import com.fongmi.android.tv.Constant
import fi.iki.elonen.NanoHTTPD
import java.io.ByteArrayInputStream
import java.io.InputStream

class Nano(port: Int) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri ?: return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
        val params = session.parms ?: emptyMap()

        return when {
            uri == "/proxy" -> handleProxy(params)
            uri == "/parse" -> handleParse(params)
            uri == "/channel" -> handleChannel(params)
            uri == "/epg" -> handleEpg(params)
            uri.startsWith("/file/") -> handleFile(uri)
            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
        }
    }

    private fun handleProxy(params: Map<String, String>): Response {
        val url = params["url"] ?: return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Missing url")
        try {
            val bytes = com.fongmi.android.tv.rust.TvCoreNative.networkGet(
                url, params["header"] ?: "", 15000
            ) ?: return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Fetch failed")

            val mimeType = getMimeType(url)
            return newFixedLengthResponse(Response.Status.OK, mimeType, ByteArrayInputStream(bytes), bytes.size.toLong())
        } catch (e: Exception) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, e.message ?: "Error")
        }
    }

    private fun handleParse(params: Map<String, String>): Response {
        return newFixedLengthResponse(Response.Status.OK, MIME_JSON, """{"msg":"ok"}""")
    }

    private fun handleChannel(params: Map<String, String>): Response {
        return newFixedLengthResponse(Response.Status.OK, MIME_JSON, """{"channels":[]}""")
    }

    private fun handleEpg(params: Map<String, String>): Response {
        return newFixedLengthResponse(Response.Status.OK, MIME_JSON, """{"epg":[]}""")
    }

    private fun handleFile(uri: String): Response {
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
    }

    private fun getMimeType(url: String): String {
        val path = Uri.parse(url).path ?: return MIME_DEFAULT_BINARY
        return when {
            path.endsWith(".m3u8") -> "application/vnd.apple.mpegurl"
            path.endsWith(".mp4") -> "video/mp4"
            path.endsWith(".ts") -> "video/mp2t"
            path.endsWith(".mkv") -> "video/x-matroska"
            path.endsWith(".avi") -> "video/x-msvideo"
            path.endsWith(".jpg") || path.endsWith(".jpeg") -> "image/jpeg"
            path.endsWith(".png") -> "image/png"
            path.endsWith(".webp") -> "image/webp"
            path.endsWith(".json") -> "application/json"
            path.endsWith(".xml") -> "application/xml"
            else -> MIME_DEFAULT_BINARY
        }
    }

    companion object {
        private const val MIME_JSON = "application/json"
    }
}
