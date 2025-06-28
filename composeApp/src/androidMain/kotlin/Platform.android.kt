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
import com.udnahc.firefly.ImmediateSync
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
//            if (hasNotificationPermission) {
//                onProceed()
//            }
        }
    val dimensions = FireflyAppTheme.dimensions
//    LaunchedEffect(canProceed) {
//        if (canProceed) {
//            onProceed.invoke()
//        }
//    }
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