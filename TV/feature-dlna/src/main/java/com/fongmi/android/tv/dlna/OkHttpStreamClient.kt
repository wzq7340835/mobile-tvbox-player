package com.fongmi.android.tv.dlna

import com.squareup.okhttp.*
import org.jupnp.transport.spi.StreamClient
import org.jupnp.transport.spi.StreamClientConfiguration
import java.io.IOException
import java.io.InputStream

class OkHttpStreamClient(private val configuration: StreamClientConfiguration) : StreamClient<StreamClientConfiguration> {

    private val client = OkHttpClient()

    override fun getConfiguration(): StreamClientConfiguration = configuration
    override fun stop() {}

    override fun sendRequest(request: org.jupnp.model.message.StreamRequestMessage): org.jupnp.model.message.StreamResponseMessage {
        val httpRequest = Request.Builder()
            .url(request.uri.toString())
            .method(request.method.httpMethodName, request.body?.let { RequestBody.create(null, it) })
            .build()

        val response = client.newCall(httpRequest).execute()
        return org.jupnp.model.message.StreamResponseMessage(
            response.body()?.byteStream() ?: byteArrayOf().inputStream(),
            response.code(),
            response.headers().toMultimap().mapValues { it.value.joinToString(",") }
        )
    }
}
