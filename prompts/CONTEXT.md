# Architect Banking App — Session Context
# Load this file in EVERY claude session without exception.

# Development Workflow
Always follow WORKFLOW.md:
- Plan → Apply → Review → Build → Fix → Commit

## Project
Name: Architect Banking App
Platform: Android (Kotlin + Jetpack Compose)
Assignment: Production-grade Server-Driven UI banking app

## Stack
- Language: Kotlin 1.9+
- UI: Jetpack Compose (no XML layouts)
- Architecture: MVI (strict unidirectional data flow)
- DI: Hilt
- Network: Retrofit 2 + OkHttp 3 (mocked JSON — no real backend)
- DB: Room 2.6+
- Async: Coroutines + StateFlow
- Navigation: Compose Navigation (API-driven, no hardcoded routes)
- Image: Coil
- Serialization: Kotlinx Serialization

## Module Map
:app                  → entry point, NavHost, Hilt app
:core:ui              → design system, tokens, base components
:core:network         → Retrofit, interceptors, base models
:core:data            → Room, base repository, session
:core:domain          → base UseCase, Result wrapper
:engine:sdui          → SDUIRenderer, component registry, action handler
:engine:navigation    → dynamic NavGraph builder, deep link handler
:feature:login        → login screen + MVI
:feature:dashboard    → dashboard screen + MVI
:feature:profile      → profile screen + MVI

[//]: # (:feature:accounts     → accounts list + detail)

[//]: # (:feature:transfer     → fund transfer flow)

[//]: # (:feature:transactions → transaction history)

## Absolute Rules (never violate)
1. NO hardcoded UI — every screen rendered via SDUIRenderer from JSON
2. NO hardcoded navigation — all routes from API NavigationAction JSON
3. MVI only — State sealed class, Intent sealed class, Effect Channel
4. Hilt for ALL dependency injection
5. Never modify :core or :engine files unless explicitly instructed
6. All values from design tokens — no magic colors/sizes/strings
7. Every public API must have KDoc
8. All API responses mocked via local JSON assets — no real network calls
9. Feature modules must NOT depend on each other
10. SOLID principles and clean architecture in every file
