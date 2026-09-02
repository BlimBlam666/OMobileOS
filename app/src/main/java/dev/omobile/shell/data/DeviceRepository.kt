package dev.omobile.shell.data

import android.app.LauncherApps
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Process
import java.net.Inet4Address
import java.net.NetworkInterface

class DeviceRepository(private val context: Context) {
    fun installedApps(): List<AppEntry> {
        val launcherApps = context.getSystemService(LauncherApps::class.java)
        return launcherApps.getActivityList(null, Process.myUserHandle())
            .map {
                AppEntry(
                    label = it.label.toString(),
                    packageName = it.applicationInfo.packageName,
                    componentName = it.componentName,
                    icon = runCatching { it.getBadgedIcon(0) }.getOrNull()
                )
            }
            .distinctBy { it.componentName }
            .sortedBy { it.label.lowercase() }
    }

    fun status(): DeviceStatus {
        val battery = context.getSystemService(BatteryManager::class.java)
        val connectivity = context.getSystemService(ConnectivityManager::class.java)
        val network = connectivity.activeNetwork
        val capabilities = network?.let(connectivity::getNetworkCapabilities)
        val connection = when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "WIFI"
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "CELL"
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> "ETH"
            else -> "OFFLINE"
        }

        return DeviceStatus(
            batteryPercent = battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
            charging = battery.isCharging,
            connection = connection,
            localAddress = localIpv4Address(),
            networkValidated = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        )
    }

    fun sensors(): List<SensorEntry> {
        val manager = context.getSystemService(SensorManager::class.java)
        return manager.getSensorList(android.hardware.Sensor.TYPE_ALL)
            .map { SensorEntry(it.name, it.vendor, it.type, it.power) }
            .sortedBy { it.name.lowercase() }
    }

    fun launch(app: AppEntry): Boolean = runCatching {
        val launcherApps = context.getSystemService(LauncherApps::class.java)
        launcherApps.startMainActivity(app.componentName, Process.myUserHandle(), null, null)
        true
    }.getOrDefault(false)

    fun launchIntent(intent: Intent): Boolean = runCatching {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        true
    }.getOrDefault(false)

    private fun localIpv4Address(): String = runCatching {
        NetworkInterface.getNetworkInterfaces().toList()
            .flatMap { it.inetAddresses.toList() }
            .firstOrNull { !it.isLoopbackAddress && it is Inet4Address }
            ?.hostAddress ?: "—"
    }.getOrDefault("—")
}
