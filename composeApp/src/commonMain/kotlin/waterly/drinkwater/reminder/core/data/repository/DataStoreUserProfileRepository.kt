package waterly.drinkwater.reminder.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import waterly.drinkwater.reminder.core.domain.model.*
import waterly.drinkwater.reminder.core.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStore-based implementation of UserProfileRepository
 * 
 * Persists user profile data locally using DataStore Preferences
 * All data survives app restarts on both Android and iOS
 */
class DataStoreUserProfileRepository(
    private val dataStore: DataStore<Preferences>
) : UserProfileRepository {
    
    private object Keys {
        val USERNAME = stringPreferencesKey("username")
        val BIOLOGICAL_SEX = stringPreferencesKey("biological_sex")
        val AGE_GROUP = stringPreferencesKey("age_group")
        val WEIGHT = floatPreferencesKey("weight")
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit")
        val ACTIVITY_LEVEL = stringPreferencesKey("activity_level")
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val VOLUME_UNIT = stringPreferencesKey("volume_unit")
        val CREATED_AT = longPreferencesKey("created_at")
        val UPDATED_AT = longPreferencesKey("updated_at")
    }
    
    override suspend fun getProfile(): UserProfile? {
        return observeProfile().first()
    }
    
    override fun observeProfile(): Flow<UserProfile?> {
        return dataStore.data.map { preferences ->
            val weight = preferences[Keys.WEIGHT] ?: return@map null

            UserProfile(
                username = preferences[Keys.USERNAME],
                biologicalSex = preferences[Keys.BIOLOGICAL_SEX]?.let {
                    try {
                        BiologicalSex.valueOf(it)
                    } catch (e: IllegalArgumentException) {
                        BiologicalSex.MALE
                    }
                } ?: BiologicalSex.MALE,
                ageGroup = preferences[Keys.AGE_GROUP]?.let {
                    try {
                        AgeGroup.valueOf(it)
                    } catch (e: IllegalArgumentException) {
                        AgeGroup.AGE_18_30
                    }
                } ?: AgeGroup.AGE_18_30,
                weight = weight,
                weightUnit = preferences[Keys.WEIGHT_UNIT]?.let {
                    try {
                        WeightUnit.valueOf(it)
                    } catch (e: IllegalArgumentException) {
                        WeightUnit.KG
                    }
                } ?: WeightUnit.KG,
                activityLevel = preferences[Keys.ACTIVITY_LEVEL]?.let {
                    try {
                        ActivityLevel.valueOf(it)
                    } catch (e: IllegalArgumentException) {
                        ActivityLevel.MODERATE
                    }
                } ?: ActivityLevel.MODERATE,
                dailyGoal = preferences[Keys.DAILY_GOAL] ?: 2500,
                createdAt = preferences[Keys.CREATED_AT] ?: 0L,
                updatedAt = preferences[Keys.UPDATED_AT] ?: 0L
            )
        }
    }
    
    override suspend fun saveProfile(profile: UserProfile) {
        val currentTimeMillis = System.currentTimeMillis()

        dataStore.edit { preferences ->
            // Handle username - remove key if null, set if not null
            if (profile.username != null) {
                preferences[Keys.USERNAME] = profile.username
            } else {
                preferences.remove(Keys.USERNAME)
            }

            preferences[Keys.BIOLOGICAL_SEX] = profile.biologicalSex.name
            preferences[Keys.AGE_GROUP] = profile.ageGroup.name
            preferences[Keys.WEIGHT] = profile.weight
            preferences[Keys.WEIGHT_UNIT] = profile.weightUnit.name
            preferences[Keys.ACTIVITY_LEVEL] = profile.activityLevel.name
            preferences[Keys.DAILY_GOAL] = profile.dailyGoal

            if (preferences[Keys.CREATED_AT] == null) {
                preferences[Keys.CREATED_AT] = currentTimeMillis
            }

            preferences[Keys.UPDATED_AT] = currentTimeMillis
        }
    }
    
    override suspend fun updateWeight(weight: Float, unit: WeightUnit) {
        dataStore.edit { preferences ->
            preferences[Keys.WEIGHT] = weight
            preferences[Keys.WEIGHT_UNIT] = unit.name
            preferences[Keys.UPDATED_AT] = System.currentTimeMillis()
        }
    }
    
    override suspend fun updateActivityLevel(level: ActivityLevel) {
        dataStore.edit { preferences ->
            preferences[Keys.ACTIVITY_LEVEL] = level.name
            preferences[Keys.UPDATED_AT] = System.currentTimeMillis()
        }
    }
    
    override suspend fun saveDailyGoal(goalMl: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.DAILY_GOAL] = goalMl
            preferences[Keys.UPDATED_AT] = System.currentTimeMillis()
        }
    }
    
    override suspend fun getDailyGoal(): Int? {
        return dataStore.data.map { preferences ->
            preferences[Keys.DAILY_GOAL]
        }.first()
    }
    
    override suspend fun saveVolumeUnit(unit: VolumeUnit) {
        dataStore.edit { preferences ->
            preferences[Keys.VOLUME_UNIT] = unit.name
            preferences[Keys.UPDATED_AT] = System.currentTimeMillis()
        }
    }
    
    override suspend fun getVolumeUnit(): VolumeUnit {
        return dataStore.data.map { preferences ->
            preferences[Keys.VOLUME_UNIT]?.let {
                try {
                    VolumeUnit.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    VolumeUnit.ML
                }
            } ?: VolumeUnit.ML
        }.first()
    }
}
