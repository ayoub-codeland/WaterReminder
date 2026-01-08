package com.drinkwater.reminder.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import waterreminderapp.composeapp.generated.resources.Res
import waterreminderapp.composeapp.generated.resources.inter_bold
import waterreminderapp.composeapp.generated.resources.inter_extrabold
import waterreminderapp.composeapp.generated.resources.inter_medium
import waterreminderapp.composeapp.generated.resources.inter_regular
import waterreminderapp.composeapp.generated.resources.inter_semibold

@Composable
fun interFontFamily() = FontFamily(
    Font(Res.font.inter_regular, weight = FontWeight.Normal),      // 400
    Font(Res.font.inter_medium, weight = FontWeight.Medium),
    Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.inter_bold, weight = FontWeight.Bold),
    Font(Res.font.inter_extrabold, weight = FontWeight.ExtraBold),
)

@Composable
fun AppTypography(): Typography {
    val interFontFamily = interFontFamily()

    return Typography(
        // Display Styles (Large Headings)
        displayLarge = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 57.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 64.sp
        ),
        displayMedium = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 30.sp,                      // text-3xl
            fontWeight = FontWeight.ExtraBold,     // font-extrabold (800) ✅ FIXED
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 24.sp,                      // text-2xl
            fontWeight = FontWeight.Bold,          // font-bold (700)
            lineHeight = 32.sp
        ),

        // Headline Styles
        headlineLarge = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 18.sp,                      // text-lg
            fontWeight = FontWeight.Bold,          // font-bold (700)
            lineHeight = 24.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp
        ),

        // Title Styles
        titleLarge = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 14.sp,                      // text-sm ✅ FIXED
            fontWeight = FontWeight.Medium,        // font-medium (500)
            lineHeight = 20.sp
        ),
        titleSmall = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 14.sp,                      // text-sm
            fontWeight = FontWeight.Bold,          // font-bold (700)
            lineHeight = 20.sp
        ),

        // Body Styles
        bodyLarge = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 16.sp,                      // text-base
            fontWeight = FontWeight.Medium,        // font-medium (500) ✅ ADDED
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 14.sp,                      // text-sm
            fontWeight = FontWeight.Normal,        // font-normal (400)
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.sp
        ),

        // Label Styles (Buttons, Badges, Small Text)
        labelLarge = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 14.sp,                      // text-sm
            fontWeight = FontWeight.SemiBold,      // font-semibold (600)
            lineHeight = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 12.sp,                      // text-xs
            fontWeight = FontWeight.Bold,          // font-bold (700) ✅ FIXED
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = interFontFamily,
            fontSize = 11.sp,                      // Slider labels
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp,
            letterSpacing = 1.sp
        )
    )
}