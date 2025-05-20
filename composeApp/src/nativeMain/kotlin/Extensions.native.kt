import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue


actual fun Double.getDisplayWithCurrency(currencySymbol: String): String {
    val formatter = NSNumberFormatter()
    val isNegative = this.div(this.absoluteValue) == -1.0
    val negativeText = if (isNegative) "-" else ""
    formatter.minimumFractionDigits = 0u
    formatter.maximumFractionDigits = 2u
    formatter.numberStyle = 1u //Decimal
    return """$negativeText$currencySymbol${formatter.stringFromNumber(NSNumber(this.absoluteValue))!!}"""
}

@Composable
actual fun BackHandler(onBack: () -> Unit) {
}

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext,
    initialValue: T
): State<T> = collectAsState(context)