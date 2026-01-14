package com.drinkwater.reminder.features.settings.navigation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

private const val APP_PACKAGE_NAME = "com.drinkwater.reminder"
private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=$APP_PACKAGE_NAME"
private const val APP_SHARE_URL = "https://play.google.com/store/apps/details?id=$APP_PACKAGE_NAME"
private const val PRIVACY_POLICY_URL = "https://yourwebsite.com/privacy-policy"
private const val TERMS_URL = "https://yourwebsite.com/terms-of-service"

/**
 * Android-specific navigator factory
 * Creates navigator with Android platform operations
 */
@Composable
actual fun rememberSettingsNavigator(navController: NavHostController): SettingsNavigator {
    val context = LocalContext.current
    return SettingsNavigator(
        navController = navController,
        platformOps = AndroidPlatformOps(context.applicationContext)
    )
}

private class AndroidPlatformOps(private val context: Context) : PlatformOperations {

    override fun shareApp() {
        try {
            val shareText = """
Check out HydroTracker - Water Reminder! 💧

Stay hydrated and healthy with smart water intake tracking.

Download now: $APP_SHARE_URL
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "HydroTracker - Water Reminder")
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            val chooserIntent = Intent.createChooser(intent, "Share HydroTracker via")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to share", Toast.LENGTH_SHORT).show()
        }
    }

    override fun rateApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$APP_PACKAGE_NAME"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Unable to open Play Store", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun copyLink() {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("HydroTracker App Link", APP_SHARE_URL)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to copy link", Toast.LENGTH_SHORT).show()
        }
    }

    override fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open URL", Toast.LENGTH_SHORT).show()
        }
    }
}
