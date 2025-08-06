/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import app.theme.FireflyAppTheme
import com.udnahc.rundown.ImmediateSync
import org.koin.mp.KoinPlatformTools

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val target = TARGET.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun PermissionScreen(onProceed: () -> Unit) {
    val context = LocalContext.current

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val permissionRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { result ->
            hasNotificationPermission = result
        }
    LaunchedEffect(hasNotificationPermission) {
        if (hasNotificationPermission) {
            onProceed()
        }
    }
    val dimensions = FireflyAppTheme.dimensions
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FireflyAppTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(dimensions.contentMargin)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                "Android app needs notification permission to sync items successfully. " +
                        "If you skip this, sync process will fail when the app is backgrounded",
                style = FireflyAppTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(64.dp))
            ElevatedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.elevatedButtonColors()
                    .copy(
                        containerColor = FireflyAppTheme.colorScheme.secondary,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ),
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onProceed.invoke()
                    }
                }
            ) {
                Text(
                    text = "Grant Notification Permission",
                    style = FireflyAppTheme.typography.titleMedium
                )
            }
            if (hasNotificationPermission) {
                Spacer(modifier = Modifier.height(64.dp))
                Text("Notification permission granted. Please proceed")
            }
            Spacer(modifier = Modifier.height(64.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ElevatedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.elevatedButtonColors()
                    .copy(
                        containerColor = FireflyAppTheme.colorScheme.secondary,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ),
                onClick = { onProceed.invoke() }
            ) {
                Text(
                    text = "Proceed",
                    style = FireflyAppTheme.typography.titleMedium
                )
            }
        }
    }
}

actual fun scheduleImmediateSync() {
    val immediateSync = KoinPlatformTools.defaultContext().get().get<ImmediateSync>()
    immediateSync.startImmediateSync()
}