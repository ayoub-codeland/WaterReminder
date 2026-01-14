# 1. Compose Specific Rules
-keepclassmembers class androidx.compose.runtime.Recomposer { *; }
-keep class androidx.compose.ui.platform.** { *; }

# 2. Kotlin Multiplatform / Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.android.HandlerContext {
    private ArrayList handlers;
}

# 3. Koin (Dependency Injection)
# Koin uses reflection to find your ViewModels/Modules; we must keep them.
-keep class org.koin.** { *; }
-keepclassmembers class * {
    @org.koin.core.annotation.** *;
}

# 4. App Specific (Your ViewModel and State)
# We keep your UI state classes so they don't break during collection
-keep class your.package.name.ui.** { *; }