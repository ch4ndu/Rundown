package app.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

interface CommonShapes {

    val small: CornerBasedShape
        get() = RoundedCornerShape(4.dp)
    val medium: CornerBasedShape
        get() = RoundedCornerShape(8.dp)
    val large: CornerBasedShape
        get() = RoundedCornerShape(12.dp)

    fun toShapes(): Shapes {
        return Shapes(
            small = small,
            medium = medium,
            large = large
        )
    }
}

object FireflyAppShapes : CommonShapes
