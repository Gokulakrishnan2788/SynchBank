# Architecture Decision Records (ADR)
# Update this when making significant architecture choices

## ADR-001: Kotlinx Serialization over Gson/Moshi
Status: Accepted
Reason: Compile-time safety, Kotlin-first, works with sealed classes for SDUI polymorphism
Consequence: All data classes need @Serializable annotation

## ADR-002: StateFlow over LiveData
Status: Accepted
Reason: Kotlin-native, lifecycle-aware with collectAsStateWithLifecycle, composable-friendly
Consequence: ViewModels expose StateFlow<State> not MutableLiveData

## ADR-003: Hilt over Koin
Status: Accepted
Reason: Compile-time validation, official Google support, better multi-module support
Consequence: All modules need @InstallIn annotations, app needs @HiltAndroidApp

## ADR-004: Component Registry Pattern for SDUI
Status: Accepted
Reason: Open/Closed principle — add new components without touching renderer
Consequence: Each component type must be registered in ComponentRegistry at startup

## ADR-005: Effect Channel (not StateFlow) for one-shot events
Status: Accepted
Reason: Navigation, toasts, dialogs must fire once — StateFlow replays on recomposition
Consequence: Use Channel<Effect>(BUFFERED) + receiveAsFlow() in every ViewModel

## ADR-006: Feature modules cannot depend on each other
Status: Accepted
Reason: Prevent circular dependencies, enforce clean boundaries
Consequence: Shared models go in :core:domain, shared UI in :core:ui

## ADR-007: Mock API via OkHttp Interceptor
Status: Accepted
Reason: Real Retrofit interface, easy swap to real API later, testable
Consequence: MockInterceptor reads from assets/, must be removed for production build
