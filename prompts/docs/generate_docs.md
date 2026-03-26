# Generate Production Documentation
# cat with: CONTEXT.md + this file
# Run at end of each milestone or final delivery

## Scope
Generate docs for the module specified in the prompt.
Do NOT document :core or :engine internals unless asked.

## Output Files to Generate

### 1. README.md (module root)
# :feature:X — [Feature Name]

## Overview
[2-sentence purpose]

## Module Dependencies
| Depends On | Why |
|------------|-----|
| :core:ui   | Design system components |

## Exposed to :app
- XScreen.kt — composable entry point
- loginGraph() — NavGraphBuilder extension

## SDUI Screens
| Screen ID | JSON Asset | Description |
|-----------|-----------|-------------|

## MVI Summary
| | Type | Description |
|-|------|-------------|
| State | LoginState | ... |
| Intents | 6 intents | ... |
| Effects | 3 effects | ... |

---

### 2. api_contract.md
# API Contract — :feature:X

| Endpoint | Method | Request | Response | Mock File |
|----------|--------|---------|----------|-----------|
| /auth/login | POST | LoginRequestDto | LoginResponseDto | auth_login_success.json |

### Error Codes
| Code | Message | Handling |
|------|---------|---------|

---

### 3. sdui_reference.md
# SDUI Component Reference — :feature:X

## Screens
### [screen_id]
| Component ID | Type | Props Summary | Action |
|-------------|------|---------------|--------|

## Component Types Used
| Type | Props | Actions Supported |
|------|-------|------------------|

## JSON Schema
[Full annotated JSON for each screen]

---

### 4. adr.md (append to existing DECISIONS.md)
## ADR-00X: [Decision Title]
Status: Accepted | Deprecated | Superseded
Context: Why this decision was needed
Decision: What was decided
Consequences: Trade-offs and impact

---

### 5. ai_usage.md (required by assignment)
# AI-Assisted Development Log

## Tools Used
- Claude (claude.ai Pro) — architecture, code generation, prompt engineering
- GitHub Copilot — inline completions

## Prompt System
Describe the prompts/ folder structure and how it was used

## Phases Where AI Was Used
| Phase | AI Tool | Usage | Time Saved |
|-------|---------|-------|-----------|

## Prompt Effectiveness
- What worked well
- What required manual correction
- Patterns discovered

## Code Generated vs Hand-Written
Approximate % and which parts

---

## Documentation Rules
- KDoc already in code — reference it, don't repeat it
- Keep README under 150 lines — link to other docs for detail
- All JSON examples must match actual assets/ files
- Flag any inconsistency between code and SDUI contract
- ai_usage.md is mandatory for assignment submission

## Module to Document
[SPECIFY WHICH MODULE OR "ALL" WHEN RUNNING THIS]
