package com.drinkwater.reminder.features.settings.presentation.notifications

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

/**
 * Android-specific permission handler for POST_NOTIFICATIONS.
 *
 * Shows permission dialog on Android 13+ when user enables notifications.
 * If permission denied, offers to open app settings.
 */
@Composable
actual fun RequestNotificationPermissionIfNeeded(
    shouldRequest: Boolean,
    onPermissionResult: (Boolean) -> Unit
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        // No runtime permission needed for Android 12 and below
        LaunchedEffect(shouldRequest) {
            if (shouldRequest) {
                onPermissionResult(true)
            }
        }
        return
    }

    val context = LocalContext.current
    var showRationaleDialog by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionResult(true)
            } else {
                // Check if we should show rationale (user can still be asked)
                // or if permission was permanently denied
                permissionDeniedPermanently = true
                showRationaleDialog = true
                onPermissionResult(false)
            }
        }
    )

    // Check and request permission when shouldRequest becomes true
    LaunchedEffect(shouldRequest) {
        if (shouldRequest) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                onPermissionResult(true)
            } else {
                // Request permission
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Rationale dialog for permanently denied permission
    if (showRationaleDialog && permissionDeniedPermanently) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text("Notification Permission Required") },
            text = {
                Text(
                    "To receive water reminders, please grant notification permission in app settings.\n\n" +
                            "Go to: Settings → Apps → Water Reminder → Permissions → Notifications"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Open app settings
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                        showRationaleDialog = false
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationaleDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
