package com.drinkwater.reminder.features.settings.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIPasteboard
import platform.UIKit.UIActivityViewController
import kotlinx.cinterop.ExperimentalForeignApi

private const val APP_PACKAGE_NAME = "com.drinkwater.reminder"
private const val APP_STORE_URL = "https://apps.apple.com/app/id1234567890"
private const val APP_SHARE_URL = "https://apps.apple.com/app/id1234567890"

/**
 * iOS-specific navigator factory
 * Creates navigator with iOS platform operations
 */
@Composable
actual fun rememberSettingsNavigator(navController: NavHostController): SettingsNavigator {
    return SettingsNavigator(
        navController = navController,
        platformOps = IosPlatformOps()
    )
}

@OptIn(ExperimentalForeignApi::class)
private class IosPlatformOps : PlatformOperations {

    override fun shareApp() {
        try {
            val shareText = """
Check out HydroTracker - Water Reminder! 💧

Stay hydrated and healthy with smart water intake tracking.

Download now: $APP_SHARE_URL
            """.trimIndent()

            val activityViewController = UIActivityViewController(
                activityItems = listOf(shareText),
                applicationActivities = null
            )

            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            rootViewController?.presentViewController(
                activityViewController,
                animated = true,
                completion = null
            )
        } catch (e: Exception) {
            println("Unable to share: ${e.message}")
        }
    }

    override fun rateApp() {
        try {
            val url = NSURL.URLWithString(APP_STORE_URL)
            if (url != null) {
                UIApplication.sharedApplication.openURL(url)
            }
        } catch (e: Exception) {
            println("Unable to open App Store: ${e.message}")
        }
    }

    override fun copyLink() {
        try {
            UIPasteboard.generalPasteboard.string = APP_SHARE_URL
            println("Link copied to clipboard!")
        } catch (e: Exception) {
            println("Unable to copy link: ${e.message}")
        }
    }

    override fun openUrl(url: String) {
        try {
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                UIApplication.sharedApplication.openURL(nsUrl)
            }
        } catch (e: Exception) {
            println("Unable to open URL: ${e.message}")
        }
    }
}
