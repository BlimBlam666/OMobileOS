package dev.omobile.shell.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.omobile.shell.CommandResult
import dev.omobile.shell.MainViewModel
import dev.omobile.shell.data.AppEntry
import dev.omobile.shell.data.DeviceStatus
import dev.omobile.shell.data.SensorEntry
import dev.omobile.shell.ui.theme.Cyan
import dev.omobile.shell.ui.theme.Muted
import dev.omobile.shell.ui.theme.Panel
import dev.omobile.shell.ui.theme.PanelRaised
import dev.omobile.shell.ui.theme.Signal
import dev.omobile.shell.ui.theme.Void
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private enum class Section(val label: String, val icon: ImageVector) {
    HOME("HOME", Icons.Outlined.Home),
    APPS("APPS", Icons.Outlined.Apps),
    FIELD("FIELD", Icons.Outlined.Sensors),
    SYSTEM("SYSTEM", Icons.Outlined.Settings)
}

@Composable
fun OMMobileApp(viewModel: MainViewModel = viewModel()) {
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val sensors by viewModel.sensors.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    var section by remember { mutableStateOf(Section.HOME) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbar.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    fun execute(command: String) {
        when (val result = viewModel.runCommand(command)) {
            CommandResult.OpenApps -> section = Section.APPS
            CommandResult.OpenField -> section = Section.FIELD
            CommandResult.OpenSystem -> section = Section.SYSTEM
            is CommandResult.Unknown -> section = Section.APPS
            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Void,
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            NavigationBar(
                containerColor = Panel,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Section.entries.forEach {
                    NavigationBarItem(
                        selected = section == it,
                        onClick = { section = it },
                        icon = { Icon(it.icon, contentDescription = it.label) },
                        label = { Text(it.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (section) {
                Section.HOME -> HomeScreen(status, ::execute)
                Section.APPS -> AppsScreen(apps, viewModel::launch, ::execute)
                Section.FIELD -> FieldScreen(status, sensors)
                Section.SYSTEM -> SystemScreen(status, apps.size, sensors.size, ::execute)
            }
        }
    }
}

@Composable
private fun HomeScreen(status: DeviceStatus, execute: (String) -> Unit) {
    val now by produceState(initialValue = LocalDateTime.now()) {
        while (true) {
            value = LocalDateTime.now()
            delay(1_000)
        }
    }
    val time = remember(now) { now.format(DateTimeFormatter.ofPattern("HH:mm")) }
    val date = remember(now.toLocalDate()) { now.format(DateTimeFormatter.ofPattern("EEEE  ·  dd MMMM")) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { StatusLine(status) }
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(time, style = MaterialTheme.typography.displayLarge)
                Text(date.uppercase(), style = MaterialTheme.typography.labelMedium, color = Muted)
                Spacer(Modifier.height(22.dp))
                Text("OM", style = MaterialTheme.typography.headlineMedium, color = Signal)
                Text("MOBILE / BRAMBLE", style = MaterialTheme.typography.labelMedium, color = Muted)
            }
        }
        item { CommandBar(execute) }
        item {
            Text("QUICK LAUNCH", style = MaterialTheme.typography.labelMedium, color = Muted)
            Spacer(Modifier.height(10.dp))
            QuickGrid(execute)
        }
        item { NetworkCard(status) }
    }
}

@Composable
private fun StatusLine(status: DeviceStatus) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("OM.01", style = MaterialTheme.typography.labelMedium, color = Signal)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Wifi, null, tint = if (status.networkValidated) Cyan else Muted, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Text("${status.batteryPercent}%", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun CommandBar(execute: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    fun submit() {
        execute(query)
        query = ""
        keyboard?.hide()
    }

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Text(">", color = Signal, style = MaterialTheme.typography.titleMedium) },
        placeholder = { Text("launch or command", color = Muted) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(onGo = { submit() }),
        trailingIcon = {
            IconButton(onClick = ::submit) {
                Icon(Icons.Outlined.Terminal, "Run command", tint = Signal)
            }
        },
        shape = RoundedCornerShape(4.dp)
    )
}

private data class QuickAction(val label: String, val command: String, val icon: ImageVector)

@Composable
private fun QuickGrid(execute: (String) -> Unit) {
    val actions = listOf(
        QuickAction("WEB", "browser", Icons.Outlined.Language),
        QuickAction("PHONE", "phone", Icons.Outlined.Phone),
        QuickAction("TERM", "terminal", Icons.Outlined.Terminal),
        QuickAction("CAMERA", "camera", Icons.Outlined.CameraAlt),
        QuickAction("FILES", "files", Icons.Outlined.FolderOpen),
        QuickAction("TOOLS", "field", Icons.Outlined.Explore)
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        actions.chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { action ->
                    Card(
                        modifier = Modifier.weight(1f).clickable { execute(action.command) },
                        colors = CardDefaults.cardColors(containerColor = PanelRaised),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(
                            Modifier.fillMaxWidth().padding(vertical = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(action.icon, action.label, tint = Signal)
                            Spacer(Modifier.height(8.dp))
                            Text(action.label, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NetworkCard(status: DeviceStatus) {
    InfoCard("NETWORK") {
        DataRow("LINK", status.connection)
        DataRow("ADDRESS", status.localAddress)
        DataRow("INTERNET", if (status.networkValidated) "VALIDATED" else "UNAVAILABLE")
    }
}

@Composable
private fun AppsScreen(apps: List<AppEntry>, launch: (AppEntry) -> Unit, execute: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(apps, query) {
        if (query.isBlank()) apps else apps.filter {
            it.label.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(top = 12.dp)) {
        Text("APPLICATIONS", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 20.dp))
        Text("${apps.size} LAUNCH TARGETS", style = MaterialTheme.typography.labelMedium, color = Muted, modifier = Modifier.padding(horizontal = 20.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            placeholder = { Text("filter apps or enter a command") },
            leadingIcon = { Text(">", color = Signal) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = { execute(query) }),
            shape = RoundedCornerShape(4.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(92.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtered, key = { it.componentName.flattenToString() }) { app ->
                AppTile(app) { launch(app) }
            }
        }
    }
}

@Composable
private fun AppTile(app: AppEntry, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clip(RoundedCornerShape(4.dp)).clickable(onClick = onClick).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DrawableIcon(app.icon, app.label)
        Spacer(Modifier.height(8.dp))
        Text(app.label, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DrawableIcon(drawable: Drawable?, label: String) {
    if (drawable == null) {
        Box(Modifier.size(48.dp).background(PanelRaised), contentAlignment = Alignment.Center) {
            Text(label.take(1).uppercase(), color = Signal)
        }
    } else {
        val bitmap = remember(drawable) { drawable.toBitmap(96, 96).asImageBitmap() }
        Image(bitmap, label, modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun FieldScreen(status: DeviceStatus, sensors: List<SensorEntry>) {
    LazyColumn(
        Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("FIELD TOOLS", style = MaterialTheme.typography.headlineMedium)
            Text("NETWORK + SENSOR INVENTORY", style = MaterialTheme.typography.labelMedium, color = Muted)
        }
        item { NetworkCard(status) }
        item {
            InfoCard("SENSOR ARRAY / ${sensors.size}") {
                Text(
                    "Android currently reports these physical and composite sensors. Live readouts arrive in the next checkpoint.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Muted
                )
            }
        }
        items(sensors, key = { "${it.type}:${it.name}" }) { sensor -> SensorRow(sensor) }
    }
}

@Composable
private fun SensorRow(sensor: SensorEntry) {
    Row(
        Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp)).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Sensors, null, tint = Cyan)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(sensor.name, style = MaterialTheme.typography.bodyLarge)
            Text(sensor.vendor.uppercase(), style = MaterialTheme.typography.labelMedium, color = Muted)
        }
        Text("${sensor.powerMilliAmps} mA", style = MaterialTheme.typography.labelMedium, color = Signal)
    }
}

@Composable
private fun SystemScreen(status: DeviceStatus, appCount: Int, sensorCount: Int, execute: (String) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("SYSTEM", style = MaterialTheme.typography.headlineMedium)
            Text("OM MOBILE 0.1 / ALPHA 1", style = MaterialTheme.typography.labelMedium, color = Signal)
        }
        item {
            InfoCard("DEVICE") {
                DataRow("TARGET", "GOOGLE PIXEL 4A 5G")
                DataRow("CODENAME", "BRAMBLE")
                DataRow("APPS", appCount.toString())
                DataRow("SENSORS", sensorCount.toString())
            }
        }
        item {
            InfoCard("POWER") {
                DataRow("BATTERY", "${status.batteryPercent}%")
                DataRow("CHARGING", if (status.charging) "YES" else "NO")
            }
        }
        item {
            InfoCard("COMMANDS") {
                listOf("settings", "wifi", "terminal", "browser", "camera", "files").forEach { command ->
                    Row(
                        Modifier.fillMaxWidth().clickable { execute(command) }.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(">", color = Signal)
                        Spacer(Modifier.width(10.dp))
                        Text(command, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = Signal)
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun DataRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Muted)
        Text(value, style = MaterialTheme.typography.labelMedium)
    }
}
