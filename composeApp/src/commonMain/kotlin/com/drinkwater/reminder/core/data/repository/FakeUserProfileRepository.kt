package com.drinkwater.reminder.core.data.repository

import com.drinkwater.reminder.core.domain.model.*
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow

class FakeUserProfileRepository : UserProfileRepository {
    
    private var profile: UserProfile? = UserProfile(
        biologicalSex = BiologicalSex.FEMALE,
        ageGroup = AgeGroup.AGE_31_50,
        weight = 75f,
        weightUnit = WeightUnit.KG,
        activityLevel = ActivityLevel.MODERATE
    )
    
    private var dailyGoal: Int = 2500
    private var volumeUnit: VolumeUnit = VolumeUnit.ML
    
    override suspend fun saveProfile(profile: UserProfile) {
        this.profile = profile
    }
    
    override suspend fun getProfile(): UserProfile? {
        return profile
    }

    override fun observeProfile(): Flow<UserProfile?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWeight(weight: Float, unit: WeightUnit) {
        profile = profile?.copy(weight = weight, weightUnit = unit)
    }
    
    override suspend fun updateActivityLevel(level: ActivityLevel) {
        profile = profile?.copy(activityLevel = level)
    }
    
    override suspend fun saveDailyGoal(goalMl: Int) {
        dailyGoal = goalMl
    }
    
    override suspend fun getDailyGoal(): Int {
        return dailyGoal
    }
    
    override suspend fun saveVolumeUnit(unit: VolumeUnit) {
        volumeUnit = unit
    }
    
    override suspend fun getVolumeUnit(): VolumeUnit {
        return volumeUnit
    }
}
