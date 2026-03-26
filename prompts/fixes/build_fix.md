# Gradle Build Fix
# cat with: CONTEXT.md + this file
# Paste Gradle error below before running

## Project Context
Multi-module Android Banking App
Gradle: Kotlin DSL (build.gradle.kts)
Plugins: Hilt, Kotlin Serialization, KSP, Compose Compiler

## Fix Rules
- Fix ONLY build.gradle.kts or the annotated file causing the issue
- Do NOT change business logic or domain files
- Do NOT upgrade dependency versions unless that's explicitly the fix
- Output: exact file + exact changed block only

## Common Gradle Issues (check these first)
- Hilt: missing hilt-android-compiler in kapt/ksp dependencies
- KSP vs KAPT conflict: project uses KSP — never add kapt dependencies
- Compose compiler: must match Kotlin version (check libs.versions.toml)
- Missing module dependency: :feature:x forgot to add :core:domain
- Room: schema export directory not configured in build.gradle.kts
- Serialization: missing kotlinx-serialization-json dependency in module
- Multimodule: shared test utilities not in correct module
- Manifest merger: duplicate permissions or application tag

## Gradle Version Catalog (libs.versions.toml — DO NOT regenerate)
All versions managed centrally. Reference as libs.hilt.android etc.
If a library is missing from catalog, add to [libraries] section only.

## Build Output
[PASTE FULL GRADLE ERROR HERE — include task name and error message]
