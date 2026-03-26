# Validate — Run after every feature generation
# cat with: CONTEXT.md + this file

## Checklist — report PASS ✅ or FAIL ❌ for each

### Architecture
[ ] All screens use SDUIRenderer — zero hardcoded Composable layouts
[ ] Navigation triggered via Effect only — no direct navController.navigate() in screen
[ ] MVI contract followed — State/Intent/Effect sealed classes present
[ ] ViewModel extends BaseViewModel with correct generics
[ ] No feature module imports another feature module

### Code Quality
[ ] No !! operator used anywhere
[ ] No hardcoded strings — all in strings.xml
[ ] No hardcoded colors or dimensions — design tokens only
[ ] No business logic in Composables
[ ] Every public function has KDoc comment
[ ] No lateinit var in production code

### Dependency Injection
[ ] All dependencies injected via Hilt
[ ] @HiltViewModel on every ViewModel
[ ] Feature module has XModule.kt with all bindings
[ ] No manual instantiation (no MyClass())

### SDUI
[ ] Screen JSON exists in assets/mock/screens/
[ ] All action IDs in JSON match handler in ViewModel
[ ] All component types used are registered in ComponentRegistry

### API / Data
[ ] Mock JSON exists in assets/mock/api/
[ ] DTOs are @Serializable
[ ] Domain models are pure Kotlin (no Android imports)
[ ] Mapper functions exist for DTO ↔ Domain ↔ Entity

### Tests
[ ] ViewModelTest exists with min 5 test cases
[ ] UseCaseTest exists with min 4 test cases
[ ] JSON parsing test exists
[ ] All tests pass: ./gradlew :feature:X:testDebugUnitTest

### Build
[ ] ./gradlew :feature:X:compileDebugKotlin — zero errors
[ ] ./gradlew :feature:X:testDebugUnitTest — zero failures
[ ] No new lint warnings introduced

## Output format
For each FAIL: provide exact file name + line number + fix needed
For each PASS: single checkmark only
Summary: X/Y checks passed
