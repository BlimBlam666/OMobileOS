package dev.omobile.shell.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Void = Color(0xFF0A0B0C)
val Panel = Color(0xFF111315)
val PanelRaised = Color(0xFF191C1F)
val Bone = Color(0xFFE7E1D6)
val Muted = Color(0xFF8D9498)
val Signal = Color(0xFFC9F27B)
val Cyan = Color(0xFF77D5D9)
val Rust = Color(0xFFCF8266)

private val OMDarkColors = darkColorScheme(
    primary = Signal,
    onPrimary = Void,
    secondary = Cyan,
    onSecondary = Void,
    tertiary = Rust,
    background = Void,
    onBackground = Bone,
    surface = Panel,
    onSurface = Bone,
    surfaceVariant = PanelRaised,
    onSurfaceVariant = Muted,
    outline = Color(0xFF343A3E),
    error = Color(0xFFFF8A80)
)

@Composable
fun OMMobileTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OMDarkColors,
        typography = OMType,
        content = content
    )
}
