package dev.omobile.shell

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.omobile.shell.data.AppEntry
import dev.omobile.shell.data.DeviceRepository
import dev.omobile.shell.data.DeviceStatus
import dev.omobile.shell.data.SensorEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DeviceRepository(application)

    private val _apps = MutableStateFlow<List<AppEntry>>(emptyList())
    val apps: StateFlow<List<AppEntry>> = _apps.asStateFlow()

    private val _status = MutableStateFlow(DeviceStatus())
    val status: StateFlow<DeviceStatus> = _status.asStateFlow()

    private val _sensors = MutableStateFlow<List<SensorEntry>>(emptyList())
    val sensors: StateFlow<List<SensorEntry>> = _sensors.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        viewModelScope.launch {
            _apps.value = withContext(Dispatchers.Default) { repository.installedApps() }
            _sensors.value = withContext(Dispatchers.Default) { repository.sensors() }
        }
        viewModelScope.launch {
            while (isActive) {
                _status.value = withContext(Dispatchers.IO) { repository.status() }
                delay(10_000)
            }
        }
    }

    fun launch(app: AppEntry) {
        if (!repository.launch(app)) notify("Could not open ${app.label}")
    }

    fun runCommand(raw: String): CommandResult {
        val command = raw.trim()
        if (command.isBlank()) return CommandResult.None
        val lower = command.lowercase()

        apps.value.firstOrNull { it.label.equals(command, ignoreCase = true) }?.let {
            launch(it)
            return CommandResult.Launched
        }

        return when {
            lower == "apps" || lower == "launch" -> CommandResult.OpenApps
            lower == "field" || lower == "tools" || lower == "sensors" -> CommandResult.OpenField
            lower == "system" || lower == "status" || lower == "ip" -> CommandResult.OpenSystem
            lower == "phone" || lower == "call" -> launchIntent(Intent(Intent.ACTION_DIAL))
            lower == "camera" -> launchIntent(Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA))
            lower == "messages" || lower == "sms" -> launchIntent(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING))
            lower == "browser" || lower == "web" -> launchIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://duckduckgo.com")))
            lower == "files" -> launchIntent(Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*/*").addCategory(Intent.CATEGORY_OPENABLE))
            lower == "settings" -> launchIntent(Intent(Settings.ACTION_SETTINGS))
            lower == "wifi" -> launchIntent(Intent(Settings.ACTION_WIFI_SETTINGS))
            lower == "terminal" || lower == "term" -> launchPackage("com.termux")
            lower.startsWith("web ") -> launchIntent(Intent(Intent.ACTION_VIEW, normalizeUrl(command.drop(4))))
            else -> CommandResult.Unknown(command)
        }
    }

    fun clearMessage() { _message.value = null }

    private fun launchPackage(packageName: String): CommandResult {
        val app = apps.value.firstOrNull { it.packageName == packageName }
        return if (app != null) {
            launch(app)
            CommandResult.Launched
        } else {
            notify("Termux is not installed yet")
            CommandResult.None
        }
    }

    private fun launchIntent(intent: Intent): CommandResult =
        if (repository.launchIntent(intent)) CommandResult.Launched
        else CommandResult.Unknown("No compatible app found")

    private fun normalizeUrl(value: String): Uri {
        val clean = value.trim()
        return Uri.parse(if (clean.startsWith("http://") || clean.startsWith("https://")) clean else "https://$clean")
    }

    private fun notify(text: String) {
        _message.value = text
    }
}

sealed interface CommandResult {
    data object None : CommandResult
    data object Launched : CommandResult
    data object OpenApps : CommandResult
    data object OpenField : CommandResult
    data object OpenSystem : CommandResult
    data class Unknown(val command: String) : CommandResult
}
