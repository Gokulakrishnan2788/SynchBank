# Architect Banking App — Claude Workflow Cheatsheet
# Pin this. Reference every session.

## ─── PHASE 1: Project Setup (run once) ───────────────────────────────────

cat prompts/CONTEXT.md prompts/ARCHITECTURE.md prompts/DECISIONS.md \
    prompts/contract/naming.md prompts/contracts/constraints.md | claude

# What this generates:
# - Full multi-module Gradle structure
# - libs.versions.toml with all dependencies
# - BaseViewModel, BaseUseCase, Result wrapper in :core:domain
# - SDUIRenderer + ComponentRegistry skeleton in :engine:sdui
# - NavigationEngine skeleton in :engine:navigation
# - DesignTokens + ArchitectTheme in :core:ui
# - MockInterceptor in :core:network
# - AppDatacontract skeleton in :core:data

cat prompts/CONTEXT.md \
prompts/ARCHITECTURE.md \
prompts/DECISIONS.md \
prompts/build_fix.md \
prompts/fixes/error_fix.md | claude --plan

./gradlew build
git add . && git commit -m "chore: project setup"


## ─── PHASE 2: Login Feature ───────────────────────────────────────────────

# Step 1 — API layer
cat prompts/CONTEXT.md prompts/contract/api_contract.md \
    prompts/features/login/api.md | claude
./gradlew :feature:login:compileDebugKotlin

# Step 2 — DB layer (session)
cat prompts/CONTEXT.md prompts/contract/db_contract.md | claude
# (session table — only if not created in setup phase)

# Step 3 — Domain layer
cat prompts/CONTEXT.md prompts/contract/domain_contract.md \
    prompts/features/login/domain.md | claude
./gradlew :feature:login:compileDebugKotlin

# Step 4 — UI layer
cat prompts/CONTEXT.md prompts/contract/mvi.md prompts/contract/sdui_contract.md \
    prompts/contract/renderer.md prompts/features/login/contract.md \
    prompts/features/login/ui.md | claude
git diff   # ← ALWAYS review before building
./gradlew :feature:login:compileDebugKotlin

# Step 5 — Tests
cat prompts/CONTEXT.md prompts/contract/testing.md \
    prompts/features/login/test.md | claude
./gradlew :feature:login:testDebugUnitTest

# Step 6 — Validate
cat prompts/CONTEXT.md prompts/features/validate.md | claude

[//]: # (cat prompts/CONTEXT.md prompts/features/login/test.md | claude)

# Step 7 — Fix if needed (paste error into fix file first)
cat prompts/CONTEXT.md prompts/fixes/error_fix.md | claude    # Kotlin error
cat prompts/CONTEXT.md prompts/fixes/build_fix.md | claude    # Gradle error

# Step 8 — Commit
git add . && git commit -m "feat: login — SDUI + MVI + tests ✅"


## ─── PHASE 3+: Every New Feature (repeat this pattern) ───────────────────

# Replace "login" with: dashboard | profile | transfer | transactions

cat prompts/CONTEXT.md prompts/contract/api_contract.md \
    prompts/features/FEATURE/api.md | claude

cat prompts/CONTEXT.md prompts/contract/domain_contract.md \
    prompts/features/FEATURE/domain.md | claude

cat prompts/CONTEXT.md prompts/contract/mvi.md prompts/contract/sdui_contract.md \
    prompts/contract/renderer.md prompts/features/FEATURE/contract.md \
    prompts/features/FEATURE/ui.md | claude

git diff
./gradlew :feature:FEATURE:compileDebugKotlin
./gradlew :feature:FEATURE:testDebugUnitTest

cat prompts/CONTEXT.md prompts/features/validate.md | claude
git add . && git commit -m "feat: FEATURE ✅"


## ─── ADDING NAVIGATION (when wiring features together) ───────────────────

cat prompts/CONTEXT.md prompts/contract/navigation.md | claude
# Then update assets/mock/nav/navigation_config.json


## ─── ADDING NEW SDUI COMPONENT TYPE ──────────────────────────────────────

cat prompts/CONTEXT.md prompts/contract/renderer.md prompts/contract/sdui_contract.md \
    | claude
# Describe the new component type needed


## ─── FINAL DOCS (before submission) ──────────────────────────────────────

cat prompts/CONTEXT.md prompts/docs/generate_docs.md | claude
# Add "Document ALL features" at the end


## ─── QUICK REFERENCE ──────────────────────────────────────────────────────

# Always load:          CONTEXT.md
# For new feature:      + contract/mvi.md + contract/sdui_contract.md + feature/X/ui.md
# For API work:         + contract/api_contract.md + feature/X/api.md
# For DB work:          + contract/db_contract.md
# For domain/usecase:   + contract/domain_contract.md + feature/X/domain.md
# For tests:            + contract/testing.md + feature/X/test.md
# For routing:          + contract/navigation.md
# For renderer change:  + contract/renderer.md
# For errors:           + fixes/error_fix.md  (paste error in file)
# For build errors:     + fixes/build_fix.md  (paste gradle output in file)
# For docs:             + docs/generate_docs.md
# For setup only:       + ARCHITECTURE.md + DECISIONS.md + contract/naming.md

## ─── TOKEN SAVING RULES ───────────────────────────────────────────────────
# 1. CONTEXT.md must stay under 50 lines — trim if it grows
# 2. One layer per session (api OR domain OR ui — not all at once)
# 3. Never paste full files into claude — paste only error messages
# 4. Commit after each layer so git diff stays small and focused
# 5. Use ./gradlew :feature:X:task (not root ./gradlew) to save build time
# 6. Use Haiku model for error fixes, Sonnet for generation
