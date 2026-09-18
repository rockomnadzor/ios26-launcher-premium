package com.rockomnadzor.ios26.premium.app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.rockomnadzor.ios26.premium.app.ui.DynamicIslandCapsule
import com.rockomnadzor.ios26.premium.app.ui.OrbsBackground
import com.rockomnadzor.ios26.premium.app.ui.GlassIconContainer
import com.rockomnadzor.ios26.premium.app.ui.GlassSurface
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

data class AppInfo(val label: String, val packageName: String, val icon: Drawable)

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apps = loadInstalledApps()
        setContent {
            LauncherScreen(apps = apps, onLaunch = { pkg ->
                packageManager.getLaunchIntentForPackage(pkg)?.let { startActivity(it) }
            })
        }
    }

    private fun loadInstalledApps(): List<AppInfo> {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(intent, 0).map {
            AppInfo(it.loadLabel(pm).toString(), it.activityInfo.packageName, it.loadIcon(pm))
        }.sortedBy { it.label }
    }
}

@Composable
fun LauncherScreen(apps: List<AppInfo>, onLaunch: (String) -> Unit) {
    val hazeState = remember { HazeState() }

    Box(modifier = Modifier.fillMaxSize()) {
        OrbsBackground(
            modifier = Modifier
                .fillMaxSize()
                .haze(state = hazeState)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 100.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(apps) { app ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlassIconContainer(hazeState = hazeState, icon = {
                        Image(app.icon.toBitmap().asImageBitmap(), app.label, Modifier.fillMaxSize())
                    })
                    Text(app.label, color = Color.White)
                }
            }
        }

        DynamicIslandCapsule(
            hazeState = hazeState,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 12.dp)
        )

        GlassSurface(
            hazeState = hazeState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .height(80.dp)
                .fillMaxWidth(0.85f)
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                apps.take(4).forEach { app ->
                    GlassIconContainer(hazeState = hazeState, icon = {
                        Image(app.icon.toBitmap().asImageBitmap(), app.label, Modifier.fillMaxSize())
                    })
                }
            }
        }
    }
}

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) return bitmap
    val bmp = Bitmap.createBitmap(intrinsicWidth.coerceAtLeast(1), intrinsicHeight.coerceAtLeast(1), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bmp
}
