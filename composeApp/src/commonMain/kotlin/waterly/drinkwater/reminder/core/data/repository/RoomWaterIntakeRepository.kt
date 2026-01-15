package waterly.drinkwater.reminder.core.data.repository

import waterly.drinkwater.reminder.core.data.database.dao.DailySummaryDao
import waterly.drinkwater.reminder.core.data.database.dao.WaterIntakeDao
import waterly.drinkwater.reminder.core.data.database.entity.DailySummaryEntity
import waterly.drinkwater.reminder.core.data.database.entity.WaterIntakeEntity
import waterly.drinkwater.reminder.core.domain.model.DailySummary
import waterly.drinkwater.reminder.core.domain.model.WaterIntake
import waterly.drinkwater.reminder.core.domain.model.WeeklyStats
import waterly.drinkwater.reminder.core.domain.repository.UserProfileRepository
import waterly.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Implementation of WaterIntakeRepository using Room Database
 */
class RoomWaterIntakeRepository(
    private val waterIntakeDao: WaterIntakeDao,
    private val dailySummaryDao: DailySummaryDao,
    private val userProfileRepository: UserProfileRepository
) : WaterIntakeRepository {
    
    override suspend fun addWaterIntake(amountMl: Int): WaterIntake {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        val dateString = formatDate(localDateTime.date)
        
        val entity = WaterIntakeEntity(
            amountMl = amountMl,
            timestamp = now.toEpochMilliseconds(),
            date = dateString
        )
        
        val id = waterIntakeDao.insert(entity)
        
        // Update daily summary
        updateDailySummary(dateString)
        
        return WaterIntake(
            id = id,
            amountMl = amountMl,
            timestamp = now.toEpochMilliseconds(),
            date = dateString
        )
    }
    
    override fun getTodayTotalFlow(): Flow<Int> {
        val today = getTodayDateString()
        return waterIntakeDao.getTotalForDate(today)
    }
    
    override suspend fun getTodayTotal(): Int {
        val today = getTodayDateString()
        return waterIntakeDao.getTotalForDateOnce(today)
    }
    
    override fun getTodaySummaryFlow(): Flow<DailySummary?> {
        val today = getTodayDateString()
        return dailySummaryDao.getSummaryForDate(today).map { it?.toDomain() }
    }
    
    override suspend fun getDailySummary(date: String): DailySummary? {
        return dailySummaryDao.getSummaryForDateOnce(date)?.toDomain()
    }
    
    override suspend fun getDailySummaries(startDate: String, endDate: String): List<DailySummary> {
        return dailySummaryDao.getSummariesBetweenDatesOnce(startDate, endDate)
            .map { it.toDomain() }
    }
    
    override fun getDailySummariesFlow(startDate: String, endDate: String): Flow<List<DailySummary>> {
        return dailySummaryDao.getSummariesBetweenDates(startDate, endDate)
            .map { list -> list.map { it.toDomain() } }
    }
    
    override suspend fun getWeeklyStats(weekStartDate: String, weekEndDate: String): WeeklyStats {
        val dailySummaries = getDailySummaries(weekStartDate, weekEndDate)
        val totalMl = dailySummaryDao.getTotalBetweenDates(weekStartDate, weekEndDate)
        val avgMl = dailySummaryDao.getAverageBetweenDates(weekStartDate, weekEndDate)
        val goalsReached = dailySummaryDao.countGoalsReachedBetweenDates(weekStartDate, weekEndDate)
        // Best day = day with highest absolute water intake (ml)
        // This reflects actual hydration, which is what matters for health
        // Chart shows percentage of goal, but "best day" is about drinking the most water
        val bestDay = dailySummaries
            .filter { it.totalMl > 0 }
            .maxByOrNull { it.totalMl }
        val currentStreak = getCurrentStreak()
        
        // Calculate previous week's total for percentage change
        val prevWeekStart = subtractDays(weekStartDate, 7)
        val prevWeekEnd = subtractDays(weekEndDate, 7)
        val prevWeekTotal = dailySummaryDao.getTotalBetweenDates(prevWeekStart, prevWeekEnd)
        
        val percentageChange = if (prevWeekTotal > 0) {
            ((totalMl - prevWeekTotal).toFloat() / prevWeekTotal * 100).toInt()
        } else null
        
        return WeeklyStats(
            weekStartDate = weekStartDate,
            weekEndDate = weekEndDate,
            totalMl = totalMl,
            dailyAverageMl = avgMl,
            daysTracked = dailySummaries.size,
            goalsReached = goalsReached,
            currentStreak = currentStreak,
            bestDay = bestDay,
            dailySummaries = dailySummaries,
            percentageChangeFromLastWeek = percentageChange
        )
    }
    
    override suspend fun getCurrentStreak(): Int {
        var streak = 0
        var currentDate = getTodayDateString()
        
        // Check if today's goal is reached first
        val todaySummary = dailySummaryDao.getSummaryForDateOnce(currentDate)
        if (todaySummary?.goalReached != true) {
            // Check yesterday if today isn't completed yet
            currentDate = subtractDays(currentDate, 1)
        }
        
        // Count consecutive days with goal reached
        while (true) {
            val summary = dailySummaryDao.getSummaryForDateOnce(currentDate)
            if (summary?.goalReached == true) {
                streak++
                currentDate = subtractDays(currentDate, 1)
            } else {
                break
            }
        }
        
        return streak
    }
    
    override suspend fun resetTodayIntake() {
        val today = getTodayDateString()
        waterIntakeDao.deleteIntakesForDate(today)
        dailySummaryDao.deleteForDate(today)
    }
    
    override suspend fun deleteAllData() {
        waterIntakeDao.deleteAll()
        dailySummaryDao.deleteAll()
    }
    
    private suspend fun updateDailySummary(date: String) {
        val totalMl = waterIntakeDao.getTotalForDateOnce(date)
        val goalMl = userProfileRepository.getDailyGoal() ?: 2500
        val now = Clock.System.now().toEpochMilliseconds()
        
        val summary = DailySummaryEntity(
            date = date,
            totalMl = totalMl,
            goalMl = goalMl,
            goalReached = totalMl >= goalMl,
            lastUpdated = now
        )
        
        dailySummaryDao.insertOrUpdate(summary)
    }
    
    private fun getTodayDateString(): String {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        return formatDate(localDateTime.date)
    }
    
    private fun formatDate(date: LocalDate): String {
        val year = date.year
        val month = date.monthNumber.toString().padStart(2, '0')
        val day = date.dayOfMonth.toString().padStart(2, '0')
        return "$year-$month-$day"
    }
    
    private fun subtractDays(dateStr: String, days: Int): String {
        val parts = dateStr.split("-")
        val date = LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        val newDate = LocalDate.fromEpochDays(date.toEpochDays() - days)
        return formatDate(newDate)
    }
    
    private fun DailySummaryEntity.toDomain(): DailySummary {
        return DailySummary(
            date = date,
            totalMl = totalMl,
            goalMl = goalMl,
            goalReached = goalReached,
            lastUpdated = lastUpdated
        )
    }
}
