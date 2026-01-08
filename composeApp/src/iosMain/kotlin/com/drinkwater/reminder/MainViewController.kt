package com.drinkwater.reminder

import androidx.compose.ui.window.ComposeUIViewController
import com.drinkwater.reminder.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }