import androidx.compose.runtime.Composable

interface Platform {
    val name: String
    val target: TARGET
}

enum class TARGET {
    ANDROID, IOS, DESKTOP;

    companion object {
        fun TARGET.isAndroid(): Boolean = this == ANDROID
    }
}

expect fun getPlatform(): Platform


@Composable
expect fun PermissionScreen(onProceed: () -> Unit)


expect fun scheduleImmediateSync()