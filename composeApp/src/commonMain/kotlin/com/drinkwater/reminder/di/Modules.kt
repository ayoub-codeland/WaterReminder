package com.drinkwater.reminder.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.drinkwater.reminder.core.data.database.DatabaseFactory
import com.drinkwater.reminder.core.data.database.WaterReminderDatabase
import com.drinkwater.reminder.core.data.datastore.DataStoreFactory
import com.drinkwater.reminder.core.data.repository.DataStoreNotificationRepository
import com.drinkwater.reminder.core.data.repository.DataStoreUserProfileRepository
import com.drinkwater.reminder.core.data.repository.InMemoryDailyTipRepository
import com.drinkwater.reminder.core.data.repository.RoomWaterIntakeRepository
import com.drinkwater.reminder.core.domain.repository.DailyTipRepository
import com.drinkwater.reminder.core.domain.repository.NotificationRepository
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import com.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
import com.drinkwater.reminder.core.domain.usecase.CalculateDailyGoalUseCase
import com.drinkwater.reminder.core.domain.usecase.GetDailyTipUseCase
import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import com.drinkwater.reminder.core.domain.usecase.SaveUserProfileUseCase
import com.drinkwater.reminder.core.domain.usecase.notification.GetNotificationPreferencesUseCase
import com.drinkwater.reminder.core.domain.usecase.notification.ObserveNotificationPreferencesUseCase
import com.drinkwater.reminder.core.domain.usecase.notification.SaveNotificationPreferencesUseCase
import com.drinkwater.reminder.features.home.domain.usecase.AddWaterIntakeUseCase
import com.drinkwater.reminder.features.home.domain.usecase.GetCurrentStreakUseCase
import com.drinkwater.reminder.features.home.domain.usecase.GetTodayIntakeUseCase
import com.drinkwater.reminder.features.home.domain.usecase.ResetTodayIntakeUseCase
import com.drinkwater.reminder.features.home.presentation.HomeViewModel
import com.drinkwater.reminder.features.main.AppViewModel
import com.drinkwater.reminder.features.progress.domain.usecase.GetAllTimeStatsUseCase
import com.drinkwater.reminder.features.progress.domain.usecase.GetWeeklyStatsUseCase
import com.drinkwater.reminder.features.progress.presentation.ProgressViewModel
import com.drinkwater.reminder.features.settings.domain.usecase.UpdateActivityLevelUseCase
import com.drinkwater.reminder.features.settings.domain.usecase.UpdateWeightUseCase
import com.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelViewModel
import com.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalViewModel
import com.drinkwater.reminder.features.settings.presentation.main.SettingsViewModel
import com.drinkwater.reminder.features.settings.presentation.notifications.NotificationPreferencesViewModel
import com.drinkwater.reminder.features.settings.presentation.profile.EditProfileViewModel
import com.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightViewModel
import com.drinkwater.reminder.features.onboarding.presentation.profile.ProfileSetupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val databaseModule = module {
    single<WaterReminderDatabase> {
        get<DatabaseFactory>().createDatabase()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single { get<WaterReminderDatabase>().waterIntakeDao() }
    single { get<WaterReminderDatabase>().dailySummaryDao() }
}

val dataModule = module {
    single {
        get<DataStoreFactory>().createAppDataStore()
    }

    // Repositories
    singleOf(::DataStoreUserProfileRepository).bind<UserProfileRepository>()
    singleOf(::RoomWaterIntakeRepository).bind<WaterIntakeRepository>()
    singleOf(::InMemoryDailyTipRepository).bind<DailyTipRepository>()
    singleOf(::DataStoreNotificationRepository).bind<NotificationRepository>()
}

val domainModule = module {
    // Core use cases
    factory { GetUserProfileUseCase(get()) }
    factory { SaveUserProfileUseCase(get()) }
    factory { CalculateDailyGoalUseCase() }
    factory { UpdateWeightUseCase(get(), get()) }
    factory { UpdateActivityLevelUseCase(get(), get()) }
    factory { GetDailyTipUseCase(get()) }

    // Home use cases
    factory { AddWaterIntakeUseCase(get()) }
    factory { GetTodayIntakeUseCase(get()) }
    factory { GetCurrentStreakUseCase(get()) }
    factory { ResetTodayIntakeUseCase(get()) }

    // Progress use cases
    factory { GetWeeklyStatsUseCase(get()) }
    factory { GetAllTimeStatsUseCase(get()) }

    // Notification use cases
    factory { GetNotificationPreferencesUseCase(get()) }
    factory { SaveNotificationPreferencesUseCase(get()) }
    factory { ObserveNotificationPreferencesUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AppViewModel(get()) }
    // Onboarding feature
    viewModel { ProfileSetupViewModel(get(), get()) }

    // Home feature
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }

    // Progress feature
    viewModel { ProgressViewModel(get(), get(), get()) }

    // Settings feature
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { UpdateWeightViewModel(get(), get()) }
    viewModel { UpdateActivityLevelViewModel(get(), get()) }
    viewModel { UpdateDailyGoalViewModel(get(), get(), get()) }
    viewModel { NotificationPreferencesViewModel(get(), get(), get()) }
}

fun appModules() = listOf(
    platformModule,
    databaseModule,
    dataModule,
    domainModule,
    viewModelModule
)
