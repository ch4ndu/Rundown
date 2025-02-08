import androidx.compose.runtime.Composable
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter


actual fun Double.getDisplayWithCurrency(currencySymbol: String): String {
    val formatter = NSNumberFormatter()
    formatter.minimumFractionDigits = 0u
    formatter.maximumFractionDigits = 2u
    formatter.numberStyle = 1u //Decimal
    return """$currencySymbol${formatter.stringFromNumber(NSNumber(this))!!}"""
}

@Composable
actual fun BackHandler(onBack: () -> Unit) {
}