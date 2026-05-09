package com.fongmi.android.tv.dlna

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.*
import org.jupnp.UpnpService
import org.jupnp.UpnpServiceImpl
import org.jupnp.android.AndroidUpnpServiceConfiguration
import org.jupnp.android.AndroidUpnpServiceImpl
import org.jupnp.controlpoint.ControlPoint
import org.jupnp.model.action.ActionInvocation
import org.jupnp.model.message.UpnpResponse
import org.jupnp.model.meta.Device
import org.jupnp.model.meta.LocalDevice
import org.jupnp.model.meta.RemoteDevice
import org.jupnp.model.types.ServiceId
import org.jupnp.model.types.UDAServiceId
import org.jupnp.registry.Registry
import org.jupnp.registry.RegistryListener

class DLNAServiceConfiguration : AndroidUpnpServiceConfiguration() {
    override fun getStreamClientConfiguration() = OkHttpStreamClientConfiguration()
    override fun getStreamServerConfiguration() = SocketHttpStreamServerConfiguration()
}

class DLNARendererService : LifecycleService() {

    private var upnpService: UpnpService? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startUpnpService()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpnpService()
        scope.cancel()
    }

    private fun startUpnpService() {
        scope.launch {
            try {
                upnpService = UpnpServiceImpl(DLNAServiceConfiguration())
                upnpService?.registry?.addListener(dlnaRegistryListener)
                upnpService?.controlPoint?.search()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopUpnpService() {
        upnpService?.shutdown()
        upnpService = null
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DLNARendererService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, DLNARendererService::class.java)
            context.stopService(intent)
        }
    }
}

private val dlnaRegistryListener = object : RegistryListener {
    override fun remoteDeviceAdded(registry: Registry, device: RemoteDevice) {}
    override fun remoteDeviceUpdated(registry: Registry, device: RemoteDevice) {}
    override fun remoteDeviceRemoved(registry: Registry, device: RemoteDevice) {}
    override fun localDeviceAdded(registry: Registry, device: LocalDevice) {}
    override fun localDeviceRemoved(registry: Registry, device: LocalDevice) {}
    override fun beforeShutdown(registry: Registry) {}
    override fun afterShutdown() {}
}

object DLNACast {

    private var upnpService: UpnpService? = null

    fun setUpnpService(service: UpnpService) {
        upnpService = service
    }

    fun searchDevices(): List<Device<*, *, *>> {
        return upnpService?.registry?.devices?.toList() ?: emptyList()
    }

    fun play(device: Device<*, *, *>, url: String) {
        val service = device.findService(UDAServiceId("AVTransport")) ?: return
        upnpService?.controlPoint?.execute(
            org.jupnp.model.action.ActionInvocation(service.getAction("SetAVTransportURI"))
                .apply {
                    setInput("InstanceID", "0")
                    setInput("CurrentURI", url)
                    setInput("CurrentURIMetaData", "")
                }
        )
    }

    fun stop(device: Device<*, *, *>) {
        val service = device.findService(UDAServiceId("AVTransport")) ?: return
        upnpService?.controlPoint?.execute(
            org.jupnp.model.action.ActionInvocation(service.getAction("Stop"))
                .apply {
                    setInput("InstanceID", "0")
                }
        )
    }

    fun pause(device: Device<*, *, *>) {
        val service = device.findService(UDAServiceId("AVTransport")) ?: return
        upnpService?.controlPoint?.execute(
            org.jupnp.model.action.ActionInvocation(service.getAction("Pause"))
                .apply {
                    setInput("InstanceID", "0")
                }
        )
    }

    fun seek(device: Device<*, *, *>, position: String) {
        val service = device.findService(UDAServiceId("AVTransport")) ?: return
        upnpService?.controlPoint?.execute(
            org.jupnp.model.action.ActionInvocation(service.getAction("Seek"))
                .apply {
                    setInput("InstanceID", "0")
                    setInput("Unit", "REL_TIME")
                    setInput("Target", position)
                }
        )
    }
}

class OkHttpStreamClientConfiguration
class SocketHttpStreamServerConfiguration
