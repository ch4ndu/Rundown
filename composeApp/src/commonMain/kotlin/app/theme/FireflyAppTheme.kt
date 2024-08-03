package app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun FireflyAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    val colors = if (!darkTheme) {
        M3LightColors
    } else {
        M3DarkColors
    }

    val typography = FireflyAppTypography.toTypography()
    val shapes = FireflyAppShapes.toShapes()
    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

object FireflyAppTheme {

    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = theme.colorScheme

    val typography: CommonTypography
        @Composable
        @ReadOnlyComposable
        get() = theme.typography

    val dimensions: CommonDimensions
        @Composable
        @ReadOnlyComposable
        get() = theme.dimensions

    val shapes: CommonShapes
        @Composable
        @ReadOnlyComposable
        get() = theme.shapes

    private var isDarkOverride: Boolean? = null

    private val theme: CommonThemeInterface
        @Composable
        @ReadOnlyComposable
        get() {
            val isDark = isDarkOverride ?: isSystemInDarkTheme()
            return if (isDark) {
                FireflyAppDarkTheme
            } else {
                FireflyAppLightTheme
            }
        }
}


private val M3LightColors = ColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
//    primaryVariant = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
//    secondaryVariant = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
//    isLight = true
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val M3DarkColors = ColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
//    primaryVariant = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
//    secondaryVariant = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
//    isLight = false
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)


private interface CommonThemeInterface {

    val typography: CommonTypography
    val shapes: CommonShapes
    val dimensions: CommonDimensions
    val colorScheme: ColorScheme
}

private object FireflyAppLightTheme : CommonThemeInterface {

    override val typography: CommonTypography = FireflyAppTypography
    override val shapes: CommonShapes = FireflyAppShapes
    override val dimensions: CommonDimensions = FireFlyAppDimensions
    override val colorScheme: ColorScheme = FireflyAppLightColorPalette
}

private object FireflyAppDarkTheme : CommonThemeInterface {

    override val typography: CommonTypography = FireflyAppTypography
    override val shapes: CommonShapes = FireflyAppShapes
    override val dimensions: CommonDimensions = FireFlyAppDimensions
    override val colorScheme: ColorScheme = FireflyAppDarkColorPalette
}


private val FireflyAppDarkColorPalette = M3DarkColors

private val FireflyAppLightColorPalette = M3LightColors
