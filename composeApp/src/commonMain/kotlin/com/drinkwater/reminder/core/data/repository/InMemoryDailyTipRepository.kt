package com.drinkwater.reminder.core.data.repository

import com.drinkwater.reminder.core.domain.model.DailyTip
import com.drinkwater.reminder.core.domain.repository.DailyTipRepository
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import waterreminderapp.composeapp.generated.resources.Res
import kotlin.random.Random

/**
 * In-memory implementation of DailyTipRepository
 * 
 * Loads tips from JSON resource file and keeps them in memory
 * MVP approach - no database needed
 */
class InMemoryDailyTipRepository : DailyTipRepository {
    
    private val tips = mutableListOf<String>()
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun loadTips() {
        try {
            // Load JSON file from resources
            val jsonString = Res.readBytes("files/hydration_tips.json")
                .decodeToString()
            
            val tipsData = json.decodeFromString<TipsContainer>(jsonString)
            
            tips.clear()
            tips.addAll(tipsData.tips)
            
            // Tips loaded successfully
        } catch (e: Exception) {
            // Error loading tips, using defaults
            // Add default tips if loading fails
            loadDefaultTips()
        }
    }
    
    override suspend fun getRandomTip(): DailyTip {
        if (tips.isEmpty()) {
            loadDefaultTips()
        }
        
        val randomIndex = Random.nextInt(tips.size)
        return DailyTip(content = tips[randomIndex])
    }
    
    private fun loadDefaultTips() {
        tips.clear()
        tips.addAll(
            listOf(
                "Start your day with a glass of water to kickstart your metabolism.",
                "Drink water before meals to aid digestion and control appetite.",
                "Keep a reusable water bottle with you at all times.",
                "Set hourly reminders to take a water break.",
                "Drinking water now helps avoid the 3 PM energy slump."
            )
        )
    }
    
    @Serializable
    private data class TipsContainer(
        val tips: List<String>
    )
}
