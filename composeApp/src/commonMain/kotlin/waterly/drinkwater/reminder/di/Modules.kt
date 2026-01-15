package waterly.drinkwater.reminder.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import waterly.drinkwater.reminder.core.data.database.DatabaseFactory
import waterly.drinkwater.reminder.core.data.database.WaterReminderDatabase
import waterly.drinkwater.reminder.core.data.datastore.DataStoreFactory
import waterly.drinkwater.reminder.core.data.repository.DataStoreNotificationRepository
import waterly.drinkwater.reminder.core.data.repository.DataStoreUserProfileRepository
import waterly.drinkwater.reminder.core.data.repository.InMemoryDailyTipRepository
import waterly.drinkwater.reminder.core.data.repository.RoomWaterIntakeRepository
import waterly.drinkwater.reminder.core.domain.repository.DailyTipRepository
import waterly.drinkwater.reminder.core.domain.repository.NotificationRepository
import waterly.drinkwater.reminder.core.domain.repository.UserProfileRepository
import waterly.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
import waterly.drinkwater.reminder.core.domain.usecase.CalculateDailyGoalUseCase
import waterly.drinkwater.reminder.core.domain.usecase.GetDailyTipUseCase
import waterly.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import waterly.drinkwater.reminder.core.domain.usecase.SaveUserProfileUseCase
import waterly.drinkwater.reminder.core.domain.usecase.notification.GetNotificationPreferencesUseCase
import waterly.drinkwater.reminder.core.domain.usecase.notification.ObserveNotificationPreferencesUseCase
import waterly.drinkwater.reminder.core.domain.usecase.notification.SaveNotificationPreferencesUseCase
import waterly.drinkwater.reminder.features.home.domain.usecase.AddWaterIntakeUseCase
import waterly.drinkwater.reminder.features.home.domain.usecase.GetCurrentStreakUseCase
import waterly.drinkwater.reminder.features.home.domain.usecase.GetTodayIntakeUseCase
import waterly.drinkwater.reminder.features.home.domain.usecase.ResetTodayIntakeUseCase
import waterly.drinkwater.reminder.features.home.presentation.HomeViewModel
import waterly.drinkwater.reminder.features.main.AppViewModel
import waterly.drinkwater.reminder.features.progress.domain.usecase.GetAllTimeStatsUseCase
import waterly.drinkwater.reminder.features.progress.domain.usecase.GetWeeklyStatsUseCase
import waterly.drinkwater.reminder.features.progress.presentation.ProgressViewModel
import waterly.drinkwater.reminder.features.settings.domain.usecase.UpdateActivityLevelUseCase
import waterly.drinkwater.reminder.features.settings.domain.usecase.UpdateWeightUseCase
import waterly.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelViewModel
import waterly.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalViewModel
import waterly.drinkwater.reminder.features.settings.presentation.main.SettingsViewModel
import waterly.drinkwater.reminder.features.settings.presentation.notifications.NotificationPreferencesViewModel
import waterly.drinkwater.reminder.features.settings.presentation.profile.EditProfileViewModel
import waterly.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightViewModel
import waterly.drinkwater.reminder.features.onboarding.presentation.profile.ProfileSetupViewModel
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
