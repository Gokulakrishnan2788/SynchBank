# Global Constraints
# These rules apply to EVERY file generated in this project

## Code Constraints
- No lateinit var — use by lazy or constructor injection
- No !! operator — use safe calls and Elvis operator
- No runBlocking in production code — use viewModelScope or suspend
- No hardcoded strings — strings.xml only
- No hardcoded colors — DesignTokens only
- No hardcoded dimensions — spacing tokens only
- No static/companion object state — inject via Hilt
- No God classes — single responsibility per file
- Max function length: 30 lines (extract if longer)
- Max file length: 200 lines (split if longer)

## Compose Constraints
- No side effects in composable body — use LaunchedEffect/SideEffect
- No ViewModel creation in composable — always inject via hiltViewModel()
- No direct state mutation — only through Intent
- Stateless composables where possible — hoist state to ViewModel
- Every composable must have a @Preview

## Architecture Constraints
- Repository interface in :core:domain, impl in :feature:* or :core:data
- UseCase = one public operator fun invoke() only
- ViewModel never imports Android framework (except ViewModel base class)
- No business logic in Composables — only UI rendering and intent emission

## Testing Constraints
- Every ViewModel must have corresponding ViewModelTest
- Every UseCase must have corresponding UseCaseTest
- Mock all dependencies — no real network/DB in tests
- Test naming: `given_when_then` pattern
