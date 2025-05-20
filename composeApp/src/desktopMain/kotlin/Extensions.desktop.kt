import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext


actual fun Double.getDisplayWithCurrency(currencySymbol: String): String {
    return this.toString()
}

@Composable
actual fun BackHandler(onBack: () -> Unit) {

}

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext,
    initialValue: T
): State<T> = collectAsState(context)