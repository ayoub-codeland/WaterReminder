package com.drinkwater.reminder.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.drinkwater.reminder.core.data.database.entity.DailySummaryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for daily summary records
 */
@Dao
interface DailySummaryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(summary: DailySummaryEntity)
    
    @Query("SELECT * FROM daily_summary WHERE date = :date")
    fun getSummaryForDate(date: String): Flow<DailySummaryEntity?>
    
    @Query("SELECT * FROM daily_summary WHERE date = :date")
    suspend fun getSummaryForDateOnce(date: String): DailySummaryEntity?
    
    @Query("SELECT * FROM daily_summary WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getSummariesBetweenDates(startDate: String, endDate: String): Flow<List<DailySummaryEntity>>
    
    @Query("SELECT * FROM daily_summary WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    suspend fun getSummariesBetweenDatesOnce(startDate: String, endDate: String): List<DailySummaryEntity>
    
    @Query("SELECT COUNT(*) FROM daily_summary WHERE goalReached = 1 AND date BETWEEN :startDate AND :endDate")
    suspend fun countGoalsReachedBetweenDates(startDate: String, endDate: String): Int
    
    @Query("SELECT * FROM daily_summary WHERE goalReached = 1 ORDER BY date DESC LIMIT 1")
    suspend fun getLastGoalReachedDate(): DailySummaryEntity?
    
    @Query("SELECT COALESCE(SUM(totalMl), 0) FROM daily_summary WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalBetweenDates(startDate: String, endDate: String): Int
    
    @Query("SELECT COALESCE(AVG(totalMl), 0) FROM daily_summary WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getAverageBetweenDates(startDate: String, endDate: String): Int
    
    @Query("DELETE FROM daily_summary WHERE date = :date")
    suspend fun deleteForDate(date: String)
    
    @Query("DELETE FROM daily_summary")
    suspend fun deleteAll()
}
