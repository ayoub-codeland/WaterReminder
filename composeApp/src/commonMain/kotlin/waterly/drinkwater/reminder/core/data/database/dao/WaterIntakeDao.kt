package waterly.drinkwater.reminder.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import waterly.drinkwater.reminder.core.data.database.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for water intake records
 */
@Dao
interface WaterIntakeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(intake: WaterIntakeEntity): Long
    
    @Query("SELECT * FROM water_intake WHERE date = :date ORDER BY timestamp DESC")
    fun getIntakesForDate(date: String): Flow<List<WaterIntakeEntity>>
    
    @Query("SELECT * FROM water_intake WHERE date = :date ORDER BY timestamp DESC")
    suspend fun getIntakesForDateOnce(date: String): List<WaterIntakeEntity>
    
    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_intake WHERE date = :date")
    fun getTotalForDate(date: String): Flow<Int>
    
    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_intake WHERE date = :date")
    suspend fun getTotalForDateOnce(date: String): Int
    
    @Query("SELECT * FROM water_intake WHERE date BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getIntakesBetweenDates(startDate: String, endDate: String): Flow<List<WaterIntakeEntity>>
    
    @Query("SELECT * FROM water_intake WHERE date BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    suspend fun getIntakesBetweenDatesOnce(startDate: String, endDate: String): List<WaterIntakeEntity>
    
    @Query("DELETE FROM water_intake WHERE date = :date")
    suspend fun deleteIntakesForDate(date: String)
    
    @Query("DELETE FROM water_intake WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM water_intake")
    suspend fun deleteAll()
}
