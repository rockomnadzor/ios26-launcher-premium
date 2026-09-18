package com.rockomnadzor.ios26.premium.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartScreen(
                onStart = { onStartClicked() },
                onSettings = { /* заглушка настроек лаунчера */ }
            )
        }
    }

    private fun onStartClicked() {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
            )
        } else {
            startService(Intent(this, DynamicIslandService::class.java))
        }
        startActivity(Intent(this, LauncherActivity::class.java))
    }
}

@Composable
fun StartScreen(onStart: () -> Unit, onSettings: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Button(
            onClick = onStart,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp)
        ) { Text("Старт") }

        Button(
            onClick = onSettings,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
        ) { Text("Настройки") }
    }
}
