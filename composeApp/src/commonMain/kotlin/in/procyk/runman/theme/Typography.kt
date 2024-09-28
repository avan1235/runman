package `in`.procyk.runman.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontStyle.Companion.Normal
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import runman.composeapp.generated.resources.*
import runman.composeapp.generated.resources.Res.font

private val baseline = Typography()

internal val bodyFontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font(resource = font.RobotoCondensed_Black, weight = FontWeight.Black, style = Normal),
        Font(resource = font.RobotoCondensed_BlackItalic, weight = FontWeight.Black, style = Italic),
        Font(resource = font.RobotoCondensed_Bold, weight = FontWeight.Bold, style = Normal),
        Font(resource = font.RobotoCondensed_BoldItalic, weight = FontWeight.Bold, style = Italic),
        Font(resource = font.RobotoCondensed_ExtraBold, weight = FontWeight.ExtraBold, style = Normal),
        Font(resource = font.RobotoCondensed_ExtraBoldItalic, weight = FontWeight.ExtraBold, style = Italic),
        Font(resource = font.RobotoCondensed_ExtraLight, weight = FontWeight.ExtraLight, style = Normal),
        Font(resource = font.RobotoCondensed_ExtraLightItalic, weight = FontWeight.ExtraLight, style = Italic),
        Font(resource = font.RobotoCondensed_Regular, weight = FontWeight.Normal, style = Normal),
        Font(resource = font.RobotoCondensed_Italic, weight = FontWeight.Normal, style = Italic),
        Font(resource = font.RobotoCondensed_Light, weight = FontWeight.Light, style = Normal),
        Font(resource = font.RobotoCondensed_LightItalic, weight = FontWeight.Light, style = Italic),
        Font(resource = font.RobotoCondensed_Medium, weight = FontWeight.Medium, style = Normal),
        Font(resource = font.RobotoCondensed_MediumItalic, weight = FontWeight.Medium, style = Italic),
        Font(resource = font.RobotoCondensed_SemiBold, weight = FontWeight.SemiBold, style = Normal),
        Font(resource = font.RobotoCondensed_SemiBoldItalic, weight = FontWeight.SemiBold, style = Italic),
        Font(resource = font.RobotoCondensed_Thin, weight = FontWeight.Thin, style = Normal),
        Font(resource = font.RobotoCondensed_ThinItalic, weight = FontWeight.Thin, style = Italic),
    )

internal val displayFontFamily: FontFamily
    @Composable
    get() = bodyFontFamily

internal val AppTypography: Typography
    @Composable
    get() = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )