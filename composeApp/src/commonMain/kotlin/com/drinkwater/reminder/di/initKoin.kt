package com.drinkwater.reminder.di

import com.drinkwater.reminder.core.domain.repository.DailyTipRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    val koin = startKoin {
        config?.invoke(this)
        modules(appModules())
    }
    
    // Load tips on app startup
    CoroutineScope(Dispatchers.Default).launch {
        try {
            val tipRepository = koin.koin.get<DailyTipRepository>()
            tipRepository.loadTips()
        } catch (e: Exception) {
            println("⚠️ Failed to initialize tips: ${e.message}")
        }
    }
}