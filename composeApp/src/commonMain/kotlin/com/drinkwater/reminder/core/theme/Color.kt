package com.drinkwater.reminder.core.theme

import androidx.compose.ui.graphics.Color

/**
 * Light Theme Colors
 * Material 3 Color Scheme for Light Mode
 */
object LightColors {
    val Primary = Color(0xFF006494)           // Deep blue (water theme)
    val OnPrimary = Color(0xFFFFFFFF)         // White text on primary
    val PrimaryContainer = Color(0xFFCAE6FF)  // Light blue container
    val OnPrimaryContainer = Color(0xFF001E30) // Dark text on primary container
    
    val Secondary = Color(0xFF50606E)         // Muted blue-grey
    val OnSecondary = Color(0xFFFFFFFF)       // White text on secondary
    val SecondaryContainer = Color(0xFFD3E4F4) // Light grey-blue
    val OnSecondaryContainer = Color(0xFF0C1D29) // Dark text on secondary container
    
    val Tertiary = Color(0xFF65587B)          // Purple accent
    val OnTertiary = Color(0xFFFFFFFF)        // White text on tertiary
    val TertiaryContainer = Color(0xFFEBDCFF) // Light purple
    val OnTertiaryContainer = Color(0xFF201634) // Dark text on tertiary container
    
    val Error = Color(0xFFBA1A1A)             // Red for errors
    val OnError = Color(0xFFFFFFFF)           // White text on error
    val ErrorContainer = Color(0xFFFFDAD6)    // Light red
    val OnErrorContainer = Color(0xFF410002)  // Dark text on error container
    
    val Background = Color(0xFFFCFCFF)        // Off-white background
    val OnBackground = Color(0xFF191C1E)      // Dark text on background
    
    val Surface = Color(0xFFFCFCFF)           // Same as background
    val OnSurface = Color(0xFF191C1E)         // Dark text on surface
    val SurfaceVariant = Color(0xFFDDE3EA)    // Variant surface
    val OnSurfaceVariant = Color(0xFF41484D)  // Text on variant surface
    
    val Outline = Color(0xFF71787E)           // Border color
    val OutlineVariant = Color(0xFFC1C7CE)    // Lighter border
    
    val Scrim = Color(0xFF000000)             // Dialog overlay
}

/**
 * Dark Theme Colors
 * Material 3 Color Scheme for Dark Mode
 */
object DarkColors {
    val Primary = Color(0xFF8DCDFF)           // Light blue for dark mode
    val OnPrimary = Color(0xFF00344F)         // Dark blue text on primary
    val PrimaryContainer = Color(0xFF004C6F)  // Medium blue container
    val OnPrimaryContainer = Color(0xFFCAE6FF) // Light text on primary container
    
    val Secondary = Color(0xFFB7C9D9)         // Light grey-blue
    val OnSecondary = Color(0xFF22323F)       // Dark text on secondary
    val SecondaryContainer = Color(0xFF384956) // Medium grey-blue
    val OnSecondaryContainer = Color(0xFFD3E4F4) // Light text on secondary container
    
    val Tertiary = Color(0xFFCFC0E8)          // Light purple
    val OnTertiary = Color(0xFF352B4A)        // Dark text on tertiary
    val TertiaryContainer = Color(0xFF4C4162) // Medium purple
    val OnTertiaryContainer = Color(0xFFEBDCFF) // Light text on tertiary container
    
    val Error = Color(0xFFFFB4AB)             // Light red for dark mode
    val OnError = Color(0xFF690005)           // Dark red text on error
    val ErrorContainer = Color(0xFF93000A)    // Medium red container
    val OnErrorContainer = Color(0xFFFFDAD6)  // Light text on error container
    
    val Background = Color(0xFF191C1E)        // Dark background
    val OnBackground = Color(0xFFE1E2E5)      // Light text on background
    
    val Surface = Color(0xFF191C1E)           // Same as background
    val OnSurface = Color(0xFFE1E2E5)         // Light text on surface
    val SurfaceVariant = Color(0xFF41484D)    // Variant surface
    val OnSurfaceVariant = Color(0xFFC1C7CE)  // Text on variant surface
    
    val Outline = Color(0xFF8B9297)           // Border color
    val OutlineVariant = Color(0xFF41484D)    // Darker border
    
    val Scrim = Color(0xFF000000)             // Dialog overlay
}
