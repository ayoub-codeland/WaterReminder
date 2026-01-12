package com.drinkwater.reminder.core.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

/**
 * Helper class to handle notification permission requests on Android 13+.
 *
 * Usage:
 * ```
 * val permissionHandler = NotificationPermissionHandler(activity)
 * permissionHandler.requestPermission { granted ->
 *     if (granted) {
 *         // Permission granted, schedule notifications
 *     } else {
 *         // Permission denied, show explanation
 *     }
 * }
 * ```
 */
class NotificationPermissionHandler(
    private val activity: ComponentActivity
) {
    private var permissionCallback: ((Boolean) -> Unit)? = null

    private val permissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback?.invoke(isGranted)
        permissionCallback = null
    }

    /**
     * Check if notification permission is granted.
     */
    fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No runtime permission needed for older versions
        }
    }

    /**
     * Request notification permission.
     *
     * On Android 13+, shows system permission dialog.
     * On older versions, immediately calls callback with true.
     *
     * @param callback Called with result (true if granted, false if denied)
     */
    fun requestPermission(callback: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionCallback = callback
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // No permission needed for older Android versions
            callback(true)
        }
    }

    /**
     * Check if we should show permission rationale.
     *
     * Returns true if user previously denied the permission.
     */
    fun shouldShowRationale(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            false
        }
    }
}
