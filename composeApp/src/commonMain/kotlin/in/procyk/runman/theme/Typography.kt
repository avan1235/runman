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

private val Baseline = Typography()

internal val BodyFontFamily: FontFamily
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

internal val DisplayFontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font(resource = font.PlaywriteAT_ExtraLight, weight = FontWeight.ExtraLight, style = Normal),
        Font(resource = font.PlaywriteAT_ExtraLightItalic, weight = FontWeight.ExtraLight, style = Italic),
        Font(resource = font.PlaywriteAT_Regular, weight = FontWeight.Normal, style = Normal),
        Font(resource = font.PlaywriteAT_Italic, weight = FontWeight.Normal, style = Italic),
        Font(resource = font.PlaywriteAT_Light, weight = FontWeight.Light, style = Normal),
        Font(resource = font.PlaywriteAT_LightItalic, weight = FontWeight.Light, style = Italic),
        Font(resource = font.PlaywriteAT_Thin, weight = FontWeight.Thin, style = Normal),
        Font(resource = font.PlaywriteAT_ThinItalic, weight = FontWeight.Thin, style = Italic),
    )

internal val AppTypography: Typography
    @Composable
    get() = Typography(
        displayLarge = Baseline.displayLarge.copy(fontFamily = DisplayFontFamily),
        displayMedium = Baseline.displayMedium.copy(fontFamily = DisplayFontFamily),
        displaySmall = Baseline.displaySmall.copy(fontFamily = DisplayFontFamily),
        headlineLarge = Baseline.headlineLarge.copy(fontFamily = DisplayFontFamily),
        headlineMedium = Baseline.headlineMedium.copy(fontFamily = DisplayFontFamily),
        headlineSmall = Baseline.headlineSmall.copy(fontFamily = DisplayFontFamily),
        titleLarge = Baseline.titleLarge.copy(fontFamily = DisplayFontFamily),
        titleMedium = Baseline.titleMedium.copy(fontFamily = DisplayFontFamily),
        titleSmall = Baseline.titleSmall.copy(fontFamily = DisplayFontFamily),
        bodyLarge = Baseline.bodyLarge.copy(fontFamily = BodyFontFamily),
        bodyMedium = Baseline.bodyMedium.copy(fontFamily = BodyFontFamily),
        bodySmall = Baseline.bodySmall.copy(fontFamily = BodyFontFamily),
        labelLarge = Baseline.labelLarge.copy(fontFamily = BodyFontFamily),
        labelMedium = Baseline.labelMedium.copy(fontFamily = BodyFontFamily),
        labelSmall = Baseline.labelSmall.copy(fontFamily = BodyFontFamily),
    )