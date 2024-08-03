import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter


actual fun Double.getDisplayWithCurrency(currencySymbol: String): String {
    val formatter = NSNumberFormatter()
    formatter.minimumFractionDigits = 0u
    formatter.maximumFractionDigits = 2u
    formatter.numberStyle = 1u //Decimal
    return formatter.stringFromNumber(NSNumber(this))!!
}