package com.drinkwater.reminder.di

import com.drinkwater.reminder.core.data.datastore.createDataStore
import com.drinkwater.reminder.core.data.repository.DataStoreUserProfileRepository
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import com.drinkwater.reminder.core.domain.usecase.CalculateDailyGoalUseCase
import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import com.drinkwater.reminder.features.settings.domain.usecase.SaveUserProfileUseCase
import com.drinkwater.reminder.features.settings.domain.usecase.UpdateActivityLevelUseCase
import com.drinkwater.reminder.features.settings.domain.usecase.UpdateWeightUseCase
import com.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelViewModel
import com.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalViewModel
import com.drinkwater.reminder.features.settings.presentation.main.SettingsViewModel
import com.drinkwater.reminder.features.settings.presentation.profile.EditProfileViewModel
import com.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    // DataStore
    single { createDataStore() }

    // Repository
    single<UserProfileRepository> { DataStoreUserProfileRepository(get()) }
}

val domainModule = module {
    // Core Use Cases
    factoryOf(::GetUserProfileUseCase)
    factoryOf(::SaveUserProfileUseCase)
    factoryOf(::CalculateDailyGoalUseCase)

    // Settings Use Cases
    factoryOf(::UpdateWeightUseCase)
    factoryOf(::UpdateActivityLevelUseCase)
}

val viewModelModule = module {
    factoryOf(::SettingsViewModel)
    factoryOf(::EditProfileViewModel)
    factoryOf(::UpdateWeightViewModel)
    factoryOf(::UpdateActivityLevelViewModel)
    factoryOf(::UpdateDailyGoalViewModel)
}

fun appModules() = listOf(
    dataModule,
    domainModule,
    viewModelModule
)