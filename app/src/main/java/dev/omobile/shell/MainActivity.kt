package dev.omobile.shell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.omobile.shell.ui.OMMobileApp
import dev.omobile.shell.ui.theme.OMMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OMMobileTheme {
                OMMobileApp()
            }
        }
    }
}
