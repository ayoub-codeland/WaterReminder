package com.drinkwater.reminder.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing daily aggregated water intake
 * Used for quick access to daily totals without aggregating individual records
 */
@Entity(tableName = "daily_summary")
data class DailySummaryEntity(
    @PrimaryKey
    val date: String, // "yyyy-MM-dd" format
    val totalMl: Int,
    val goalMl: Int,
    val goalReached: Boolean,
    val lastUpdated: Long // epoch millis
)
