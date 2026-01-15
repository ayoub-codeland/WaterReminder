package waterly.drinkwater.reminder

import androidx.compose.ui.window.ComposeUIViewController
import waterly.drinkwater.reminder.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }