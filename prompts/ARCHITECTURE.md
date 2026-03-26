# Architecture Reference
# Load only for: initial setup, new module creation, core/engine changes

## Layer Order (strict — no skipping)
Compose UI → ViewModel (MVI) → UseCase → Repository → MockApiService / DAO

## Module Dependency Rules
:feature:*  → can use :core:*, :engine:*
:engine:*   → can use :core:*
:core:*     → no internal cross-dependencies
:app        → can use everything (wiring only)

## SDUI Engine Architecture
MockApi returns ScreenResponse JSON
    ↓
SDUIParser (Kotlinx Serialization) → ScreenModel
    ↓
SDUIRenderer → ComponentRegistry.resolve(type) → @Composable
    ↓
ActionHandler → NavigationEngine or ViewModel Intent

## MVI Architecture Per Feature
┌─────────────────────────────────────┐
│  Screen (Composable)                │
│  observes: State                    │
│  emits: Intent                      │
│  collects: Effect (one-shot)        │
└─────────┬──────────────────┬────────┘
          │ Intent           │ State/Effect
    ┌─────▼──────────────────▼────────┐
    │  ViewModel                      │
    │  handleIntent() → reducer()     │
    │  reducer: (State, Intent) →     │
    │           (State, Effect?)      │
    └─────────────────────────────────┘

## Navigation Architecture
All navigation is data-driven:
NavigationAction JSON → NavigationEngine → NavController.navigate()

NavigationAction schema:
{
  "type": "PUSH" | "REPLACE" | "POP" | "DEEP_LINK" | "MODAL",
  "destination": "route_string",
  "params": { "key": "value" },
  "deepLink": "architect://feature/screen"
}

NavHost lives ONLY in :app — features expose NavGraphBuilder extensions

## Design System Architecture
All UI primitives in :core:ui:
- ArchitectTheme (MaterialTheme wrapper)
- DesignTokens (colors, typography, spacing, elevation)
- BaseComponents: ArchButton, ArchTextField, ArchCard, ArchTopBar
- SDUIComponentRegistry maps type strings → Composables

## Mock API Architecture
No real network. All data from:
assets/mock/screens/   → screen SDUI JSONs
assets/mock/api/       → API response JSONs
assets/mock/nav/       → navigation config JSONs

MockInterceptor reads from assets based on URL pattern matching.
