package com.architect.banking.core.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Color tokens for the Architect Banking design system.
 * Use these constants everywhere — never use raw Color() values in composables.
 */
object ArchitectColors {
    // Primary palette
    val NavyPrimary = Color(0xFF0D1B2A)
    val NavySecondary = Color(0xFF1B2E42)
    val NavyTertiary = Color(0xFF2C4A6E)

    // Accent
    val GoldAccent = Color(0xFFC9A84C)
    val GoldLight = Color(0xFFE8C97A)

    // Neutrals
    val White = Color(0xFFFFFFFF)
    val OffWhite = Color(0xFFF5F5F0)
    val LightGray = Color(0xFFE0E0E0)
    val MediumGray = Color(0xFF9E9E9E)
    val DarkGray = Color(0xFF424242)

    // Semantic
    val Success = Color(0xFF2E7D32)
    val Error = Color(0xFFC62828)
    val Warning = Color(0xFFE65100)
    val Info = Color(0xFF1565C0)

    // Surface
    val SurfaceLight = Color(0xFFFAFAFA)
    val SurfaceDark = Color(0xFF121212)
    val CardBackground = Color(0xFFFFFFFF)
    val OverlayDark = Color(0x80000000)
}

/**
 * Spacing tokens. Use these to set padding, margin, and gap values.
 * Never use raw [Dp] values — always reference these tokens.
 */
object ArchitectSpacing {
    /** 2dp — micro gap */
    val XXS: Dp = 2.dp

    /** 4dp — extra small */
    val XS: Dp = 4.dp

    /** 8dp — small */
    val SM: Dp = 8.dp

    /** 12dp — medium-small */
    val MD_SM: Dp = 12.dp

    /** 16dp — medium */
    val MD: Dp = 16.dp

    /** 20dp — medium-large */
    val MD_LG: Dp = 20.dp

    /** 24dp — large */
    val LG: Dp = 24.dp

    /** 32dp — extra large */
    val XL: Dp = 32.dp

    /** 40dp — double extra large */
    val XXL: Dp = 40.dp

    /** 48dp — triple extra large */
    val XXXL: Dp = 48.dp

    /** 64dp — section spacing */
    val SECTION: Dp = 64.dp
}

/**
 * Elevation tokens for shadow depths.
 */
object ArchitectElevation {
    val None: Dp = 0.dp
    val XS: Dp = 1.dp
    val SM: Dp = 2.dp
    val MD: Dp = 4.dp
    val LG: Dp = 8.dp
    val XL: Dp = 16.dp
}

/**
 * Typography tokens for the design system.
 * Each style is a [TextStyle] ready for use in [Text] composables.
 */
object ArchitectTypography {
    val Display = TextStyle(
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 56.sp,
        letterSpacing = (-0.5).sp,
    )

    val Heading1 = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
    )

    val Heading2 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 32.sp,
    )

    val Heading3 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 28.sp,
    )

    val Body = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
    )

    val BodySmall = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
    )

    val Caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
    )

    val Label = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )

    val ButtonText = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )
}
