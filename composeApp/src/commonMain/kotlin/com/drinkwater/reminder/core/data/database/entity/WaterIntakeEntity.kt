package com.drinkwater.reminder.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a single water intake record
 */
@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amountMl: Int,
    val timestamp: Long, // epoch millis
    val date: String // "yyyy-MM-dd" format for easy grouping
)
