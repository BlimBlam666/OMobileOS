package dev.omobile.shell.data

import android.content.ComponentName
import android.graphics.drawable.Drawable

data class AppEntry(
    val label: String,
    val packageName: String,
    val componentName: ComponentName,
    val icon: Drawable?
)

data class DeviceStatus(
    val batteryPercent: Int = 0,
    val charging: Boolean = false,
    val connection: String = "OFFLINE",
    val localAddress: String = "—",
    val networkValidated: Boolean = false
)

data class SensorEntry(
    val name: String,
    val vendor: String,
    val type: Int,
    val powerMilliAmps: Float
)
