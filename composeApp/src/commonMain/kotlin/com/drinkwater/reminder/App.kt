package com.drinkwater.reminder

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.drinkwater.reminder.core.theme.AppTheme
import com.drinkwater.reminder.features.home.navigation.homeGraph
import com.drinkwater.reminder.features.progress.navigation.progressGraph
import com.drinkwater.reminder.features.settings.navigation.settingsGraph

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = "home_graph"
        ) {
            // Home feature (Dashboard)
            homeGraph(
                navController = navController,
                route = "home_graph"
            )
            
            // Progress feature (Analytics)
            progressGraph(
                navController = navController,
                route = "progress_graph"
            )
            
            // Settings feature
            settingsGraph(
                navController = navController,
                route = "settings_graph"
            )
        }
    }
}
