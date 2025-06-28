import androidx.compose.runtime.Composable

@Composable
actual fun PermissionScreen(onProceed: () -> Unit) {
    onProceed()
}
actual fun scheduleImmediateSync() {
}