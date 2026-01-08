@echo off
REM Complete Git Commit Script - All Missing Files
REM This commits ALL the changes that were missed

echo Starting complete git commits...
echo.

REM ============================================================================
REM First, let's see what we have
REM ============================================================================
echo Checking current status...
git status --short
echo.

REM ============================================================================
REM COMMIT 1: Bug #11 - Back Navigation (if not already committed)
REM ============================================================================
echo Commit 1/8: Bug #11 - Fix back navigation...

git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/settings/navigation/SettingsNavigator.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/home/navigation/HomeNavigator.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/progress/navigation/ProgressNavigator.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/App.kt

git commit -m "fix(navigation): prevent blank screen on back button press" -m "- Add safe popBackStack() handling in all navigators" -m "- Navigate to home if backstack is empty" -m "- Prevents app from showing blank page" -m "- Update App.kt navigation setup" -m "- Fixes Bug #11" -m "" -m "BREAKING CHANGE: None" -m "Closes: #11"

echo Commit 1 complete
echo.

REM ============================================================================
REM COMMIT 2: Bug #10 - Multiple Clicks
REM ============================================================================
echo Commit 2/8: Bug #10 - Fix multiple clicks...

git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/settings/navigation/SettingsNavigator.kt

git commit -m "fix(navigation): prevent duplicate screens from rapid clicks" -m "- Add launchSingleTop to all navigation calls" -m "- Prevents duplicate destinations in backstack" -m "- Works with existing Channel.CONFLATED in BaseViewModel" -m "- Fixes Bug #10" -m "" -m "BREAKING CHANGE: None" -m "Closes: #10"

echo Commit 2 complete
echo.

REM ============================================================================
REM COMMIT 3: Bug #1 - Username
REM ============================================================================
echo Commit 3/8: Bug #1 - Fix username display...

git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/home/presentation/HomeViewModel.kt

git commit -m "fix(home): display actual username instead of hardcoded value" -m "- Changed from hardcoded 'Alex' to profile.username" -m "- Greeting now shows user's real name from database" -m "- Fixes Bug #1" -m "" -m "BREAKING CHANGE: None" -m "Closes: #1"

echo Commit 3 complete
echo.

REM ============================================================================
REM COMMIT 4: Bug #2 - Reactive Settings
REM ============================================================================
echo Commit 4/8: Bug #2 - Add reactive settings...

git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/core/domain/repository/UserProfileRepository.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/core/data/repository/DataStoreUserProfileRepository.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/settings/presentation/main/SettingsViewModel.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/di/Modules.kt

git commit -m "feat(settings): add reactive profile observation with Flow" -m "- Add observeProfile() method to UserProfileRepository" -m "- Implement Flow-based profile observation in DataStore" -m "- SettingsViewModel now observes profile changes reactively" -m "- Settings screen auto-updates when data changes in sub-screens" -m "- Inject repository into SettingsViewModel via Koin" -m "- Fixes Bug #2" -m "" -m "Architecture:" -m "- Clean Architecture: Repository pattern with domain abstraction" -m "- Reactive Programming: Flow for data observation" -m "- Dependency Injection: Koin for loose coupling" -m "" -m "BREAKING CHANGE: None" -m "Closes: #2"

echo Commit 4 complete
echo.

REM ============================================================================
REM COMMIT 5: Bug #7 - Progress Padding
REM ============================================================================
echo Commit 5/8: Bug #7 - Fix progress padding...

git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/progress/presentation/ProgressScreen.kt

git commit -m "fix(progress): apply scaffold padding to prevent double navbar" -m "- AppScaffold now passes paddingValues to ProgressContent" -m "- ProgressContent applies padding via modifier parameter" -m "- Fixes double navigation bar padding issue" -m "- Fixes Bug #7" -m "" -m "BREAKING CHANGE: None" -m "Closes: #7"

echo Commit 5 complete
echo.

REM ============================================================================
REM COMMIT 6: Bug #12 - Weight Conversion
REM ============================================================================
echo Commit 6/8: Bug #12 - Add weight conversion...

git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/settings/domain/usecase/ConvertWeightUseCase.kt

git commit -m "feat(settings): add weight conversion use case" -m "- Create ConvertWeightUseCase for kg <-> lbs conversion" -m "- Encapsulates conversion business logic (2.20462 factor)" -m "- Supports future weight conversion features" -m "- Related to Bug #12" -m "" -m "Architecture:" -m "- Use Case Pattern: Business logic encapsulation" -m "- Single Responsibility: One purpose per use case" -m "" -m "BREAKING CHANGE: None" -m "Related: #12"

echo Commit 6 complete
echo.

REM ============================================================================
REM COMMIT 7: Bug #3 - Daily Tips System
REM ============================================================================
echo Commit 7/8: Bug #3 - Add daily tips system...

git add -A composeApp/src/commonMain/composeResources/files/
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/core/domain/model/DailyTip.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/core/domain/repository/DailyTipRepository.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/core/data/repository/InMemoryDailyTipRepository.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/core/domain/usecase/GetDailyTipUseCase.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/features/home/presentation/HomeViewModel.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/di/Modules.kt
git add -A composeApp/src/commonMain/kotlin/com/drinkwater/reminder/di/initKoin.kt

git commit -m "feat(tips): add daily hydration tips system (MVP)" -m "" -m "MVP Implementation:" -m "- Add JSON file with 100 curated hydration tips" -m "- Create DailyTip domain model" -m "- Create DailyTipRepository interface (domain layer)" -m "- Implement InMemoryDailyTipRepository (data layer)" -m "- Create GetDailyTipUseCase (business logic)" -m "- Load tips on app startup in initKoin" -m "- Display random tip on HomeScreen" -m "- Fixes Bug #3" -m "" -m "Architecture:" -m "- Clean Architecture: Full layer separation" -m "- Repository Pattern: Easy to swap with server implementation" -m "- Domain-Driven Design: Tip is a domain concept" -m "- Dependency Injection: All components registered in Koin" -m "" -m "Future Enhancement Ready:" -m "- Can replace InMemoryDailyTipRepository with ServerDailyTipRepository" -m "- No ViewModel changes needed for server integration" -m "- Architecture supports daily tip caching and rotation" -m "" -m "BREAKING CHANGE: None" -m "Closes: #3"

echo Commit 7 complete
echo.

REM ============================================================================
REM COMMIT 8: Documentation and Scripts
REM ============================================================================
echo Commit 8/8: Add documentation and commit scripts...

git add -A *.md
git add -A *.bat
git add -A *.sh

git commit -m "docs: add bug fixes documentation and commit scripts" -m "- Add BUG_FIXES_COMPLETE.md with full implementation details" -m "- Add CHANGED_FILES.md with file tree of changes" -m "- Add IMPLEMENTATION_SUMMARY.md with progress tracking" -m "- Add COMMIT_GUIDE.md with commit best practices" -m "- Add START_HERE.md with quick start guide" -m "- Add commit_bug_fixes.bat (Windows script)" -m "- Add commit_bug_fixes.sh (Linux/Mac script)" -m "- Add check_status.sh for debugging" -m "" -m "Documentation covers:" -m "- 7 bugs fixed (all critical and high priority)" -m "- Architecture decisions and patterns" -m "- File changes and statistics" -m "- Remaining work and next steps"

echo Commit 8 complete
echo.

REM ============================================================================
REM COMMIT 9: Any remaining build files
REM ============================================================================
echo Commit 9/9: Add any remaining untracked files...

REM Check if there are any remaining untracked files
git add -A

git status --short

REM If there are changes, commit them
git diff-index --quiet HEAD || git commit -m "chore: add remaining project files" -m "- Add any build configuration files" -m "- Add IDE configuration files" -m "- Add gradle wrapper files" -m "- Ensure project is fully versioned"

echo All remaining files committed
echo.

REM ============================================================================
REM Summary
REM ============================================================================
echo ============================================
echo All commits complete!
echo.
echo Checking final status...
git status
echo.
echo Summary:
echo   - All bug fixes committed
echo   - All new files committed  
echo   - All documentation committed
echo   - Project is now fully versioned
echo.
echo Next steps:
echo   1. Review commits: git log --oneline -10
echo   2. Push to remote: git push origin main
echo   3. Test the fixes on device/emulator
echo.
echo Happy coding!
echo ============================================
pause
