#!/bin/bash
# =============================================================================
# Architect Banking App — Automated Workflow Runner
# Place in project root (same level as gradlew)
#
# Usage:
#   ./run_workflow.sh                   → full run from scratch
#   ./run_workflow.sh --skip-login      → skip Phase 1 & 2 (login already done ✅)
#   ./run_workflow.sh --resume          → resume from last failed step
#   ./run_workflow.sh --from phase5     → resume from specific phase
#   ./run_workflow.sh --only phase4     → run single phase only
#   ./run_workflow.sh --list            → show all step statuses
#   ./run_workflow.sh --reset           → clear checkpoints for fresh start
# =============================================================================

set -uo pipefail

# ─── Colors ──────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
BLUE='\033[0;34m'; CYAN='\033[0;36m'; BOLD='\033[1m'; NC='\033[0m'

# ─── Config ───────────────────────────────────────────────────────────────────
PROMPTS="./prompts"
LOG_DIR="./logs/workflow"
CHECKPOINT="./.workflow_checkpoint"
CHECKPOINT_LOG="./.workflow_checkpoint.log"
CLAUDE_MODEL="${CLAUDE_MODEL:-claude-sonnet-4-5}"
MAX_FIX_RETRIES=2

# ─── Args ─────────────────────────────────────────────────────────────────────
FROM_PHASE=""; ONLY_PHASE=""
SKIP_LOGIN=false; RESUME=false; LIST_MODE=false; RESET_MODE=false

while [[ $# -gt 0 ]]; do
  case "$1" in
    --from=*)    FROM_PHASE="${1#*=}" ;;
    --from)      FROM_PHASE="$2"; shift ;;
    --only=*)    ONLY_PHASE="${1#*=}" ;;
    --only)      ONLY_PHASE="$2"; shift ;;
    --skip-login) SKIP_LOGIN=true ;;
    --resume)    RESUME=true ;;
    --list)      LIST_MODE=true ;;
    --reset)     RESET_MODE=true ;;
  esac
  shift
done

mkdir -p "$LOG_DIR"

# ─── Print helpers ────────────────────────────────────────────────────────────
log()     { echo -e "${CYAN}[$(date '+%H:%M:%S')]${NC} $1"; }
success() { echo -e "${GREEN}✅ $1${NC}"; }
warn()    { echo -e "${YELLOW}⚠️  $1${NC}"; }
error()   { echo -e "${RED}❌ $1${NC}"; }
phase()   { echo -e "\n${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
            echo -e "${BOLD}${BLUE}  $1${NC}"
            echo -e "${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}\n"; }

# ─── All steps in execution order ─────────────────────────────────────────────
ALL_STEPS=(
  "phase1:setup:Project Setup — generate base modules"
  "phase1:build:Project Setup — gradle build"
  "phase2:login_api:Login — API layer"
  "phase2:login_db:Login — DB layer"
  "phase2:login_domain:Login — Domain layer"
  "phase2:login_ui:Login — UI layer"
  "phase2:login_test:Login — Tests"
  "phase2:login_validate:Login — Validate"
  "phase3:composite:Composite Components + MainScreen"
  "phase3:composite_ui_build:Composite — :core:ui build"
  "phase3:composite_sdui_build:Composite — :engine:sdui build"
  "phase3:composite_dash_build:Composite — :feature:dashboard build"
  "phase4:home_api:Home — API layer"
  "phase4:home_api_build:Home — API build"
  "phase4:home_domain:Home — Domain layer"
  "phase4:home_domain_build:Home — Domain build"
  "phase4:home_ui:Home — UI layer"
  "phase4:home_ui_build:Home — UI build"
  "phase4:home_test:Home — Tests"
  "phase4:home_test_run:Home — Test run"
  "phase4:home_validate:Home — Validate"
  "phase5:payments_api:Payments — API layer"
  "phase5:payments_api_build:Payments — API build"
  "phase5:payments_domain:Payments — Domain layer"
  "phase5:payments_domain_build:Payments — Domain build"
  "phase5:payments_ui:Payments — UI layer"
  "phase5:payments_ui_build:Payments — UI build"
  "phase5:payments_test:Payments — Tests"
  "phase5:payments_test_run:Payments — Test run"
  "phase5:payments_validate:Payments — Validate"
  "phase5:add_ben_ui:Add Beneficiary — UI layer"
  "phase5:add_ben_build:Add Beneficiary — build"
  "phase5:add_ben_test:Add Beneficiary — Tests"
  "phase5:add_ben_test_run:Add Beneficiary — Test run"
  "phase5:add_ben_validate:Add Beneficiary — Validate"
  "phase6:accounts_ui:Accounts — full feature"
  "phase6:accounts_build:Accounts — build"
  "phase6:accounts_test_run:Accounts — Test run"
  "phase6:accounts_validate:Accounts — Validate"
  "phase7:profile_ui:Profile — full feature"
  "phase7:profile_build:Profile — build"
  "phase7:profile_test_run:Profile — Test run"
  "phase7:profile_validate:Profile — Validate"
  "phase8:navigation:Wire Navigation"
  "phase8:nav_build:Navigation — app build"
  "phase9:docs:Final Documentation"
)

# ─── Checkpoint helpers ───────────────────────────────────────────────────────
cp_done() {
  local key="$1"
  grep -v "^${key}=" "$CHECKPOINT" 2>/dev/null > "${CHECKPOINT}.tmp" || true
  echo "${key}=DONE" >> "${CHECKPOINT}.tmp"
  mv "${CHECKPOINT}.tmp" "$CHECKPOINT"
  echo "$(date '+%Y-%m-%d %H:%M:%S')  DONE   $key" >> "$CHECKPOINT_LOG"
}

cp_failed() {
  local key="$1"
  grep -v "^${key}=" "$CHECKPOINT" 2>/dev/null > "${CHECKPOINT}.tmp" || true
  echo "${key}=FAILED" >> "${CHECKPOINT}.tmp"
  mv "${CHECKPOINT}.tmp" "$CHECKPOINT"
  echo "$(date '+%Y-%m-%d %H:%M:%S')  FAILED $key" >> "$CHECKPOINT_LOG"
}

cp_is_done() { [ -f "$CHECKPOINT" ] && grep -q "^${1}=DONE$" "$CHECKPOINT"; }

cp_last_failed() {
  [ -f "$CHECKPOINT" ] && grep "=FAILED$" "$CHECKPOINT" | tail -1 | cut -d= -f1 || echo ""
}

cp_reset() { rm -f "$CHECKPOINT" "${CHECKPOINT}.tmp"; echo "" > "$CHECKPOINT_LOG"; success "Checkpoints cleared."; }

cp_list() {
  echo -e "\n${BOLD}Step Status:${NC}"
  echo "──────────────────────────────────────────────────────────"
  for entry in "${ALL_STEPS[@]}"; do
    local pid; pid=$(echo "$entry" | cut -d: -f1)
    local sid; sid=$(echo "$entry" | cut -d: -f2)
    local desc; desc=$(echo "$entry" | cut -d: -f3)
    local key="${pid}_${sid}"
    if cp_is_done "$key"; then
      echo -e "  ${GREEN}✅ DONE   ${NC} [$key]  $desc"
    elif [ -f "$CHECKPOINT" ] && grep -q "^${key}=FAILED$" "$CHECKPOINT"; then
      echo -e "  ${RED}❌ FAILED ${NC} [$key]  $desc"
    else
      echo -e "  ${YELLOW}⏳ PENDING${NC} [$key]  $desc"
    fi
  done
  echo "──────────────────────────────────────────────────────────"
}

# ─── Resume key resolution ───────────────────────────────────────────────────
resolve_resume_key() {
  if [ "$RESUME" = true ]; then cp_last_failed
  elif [ -n "$FROM_PHASE" ]; then
    for entry in "${ALL_STEPS[@]}"; do
      local pid; pid=$(echo "$entry" | cut -d: -f1)
      local sid; sid=$(echo "$entry" | cut -d: -f2)
      [ "$pid" = "$FROM_PHASE" ] && echo "${pid}_${sid}" && return
    done
  fi
  echo ""
}

# ─── Should this step run? ───────────────────────────────────────────────────
should_run() {
  local pid="$1" sid="$2"
  local key="${pid}_${sid}"
  [ -n "$ONLY_PHASE" ] && { [ "$pid" = "$ONLY_PHASE" ] || return 1; }
  [ "$SKIP_LOGIN" = true ] && [[ "$pid" == "phase1" || "$pid" == "phase2" ]] && return 1
  cp_is_done "$key" && { log "Skip (done): $key"; return 1; }
  local resume_from; resume_from=$(resolve_resume_key)
  if [ -n "$resume_from" ]; then
    local found=false
    for entry in "${ALL_STEPS[@]}"; do
      local ep; ep=$(echo "$entry" | cut -d: -f1)
      local es; es=$(echo "$entry" | cut -d: -f2)
      local ek="${ep}_${es}"
      [ "$ek" = "$resume_from" ] && found=true
      [ "$ek" = "$key" ] && { [ "$found" = true ] && return 0 || return 1; }
    done
  fi
  return 0
}

# ─── Core: run claude ────────────────────────────────────────────────────────
run_claude() {
  local key="$1"; shift
  local log_file="$LOG_DIR/${key}.log"
  log "Claude → $key"
  if cat "$@" | claude --model "$CLAUDE_MODEL" --print --dangerously-skip-permissions 2>&1 | tee "$log_file"; then
    cp_done "$key"; success "Claude done: $key"
  else
    cp_failed "$key"; error "Claude failed: $key"
    print_fix_help "$key" "$log_file"; exit 1
  fi
}

# ─── Core: run gradle ────────────────────────────────────────────────────────
run_gradle() {
  local key="$1" task="$2"
  local log_file="$LOG_DIR/gradle_${key}.log"
  local attempt=0
  while [ $attempt -le $MAX_FIX_RETRIES ]; do
    log "Gradle → $task (attempt $((attempt+1)))"
    if ./gradlew "$task" 2>&1 | tee "$log_file"; then
      cp_done "$key"; success "Gradle done: $task"; return 0
    fi
    attempt=$((attempt+1))
    if [ $attempt -le $MAX_FIX_RETRIES ]; then
      warn "Build failed — auto-fix attempt $attempt/$MAX_FIX_RETRIES"
      local fix_file="$PROMPTS/fixes/build_fix.md"
      local orig; orig=$(cat "$fix_file")
      echo -e "\n\n## Error\n\`\`\`\n$(tail -80 "$log_file")\n\`\`\`" >> "$fix_file"
      cat "$PROMPTS/CONTEXT.md" "$fix_file" \
        | claude --model "$CLAUDE_MODEL" --no-interactive --dangerously-skip-permissions 2>&1 \
        | tee "$LOG_DIR/autofix_${key}.log" || true
      echo "$orig" > "$fix_file"
    fi
  done
  cp_failed "$key"; error "Gradle failed after retries: $task"
  print_fix_help "$key" "$log_file"; exit 1
}

# ─── Manual fix instructions ─────────────────────────────────────────────────
print_fix_help() {
  local key="$1" log_file="$2"
  echo ""
  echo -e "${BOLD}${RED}╔══════════════════════════════════════════════════════╗${NC}"
  echo -e "${BOLD}${RED}║  ❌  STOPPED — Manual Fix Required                   ║${NC}"
  echo -e "${BOLD}${RED}╚══════════════════════════════════════════════════════╝${NC}"
  echo ""
  echo -e "  ${BOLD}Failed step:${NC}  $key"
  echo -e "  ${BOLD}Error log:${NC}    $log_file"
  echo ""
  echo -e "${YELLOW}── Steps to fix ──────────────────────────────────────────${NC}"
  echo -e "  1. Read the error:"
  echo -e "     ${CYAN}cat $log_file | tail -50${NC}"
  echo ""
  echo -e "  2. Fix manually OR run claude with error:"
  echo -e "     # Paste error into prompts/fixes/error_fix.md then:"
  echo -e "     ${CYAN}cat prompts/CONTEXT.md prompts/fixes/error_fix.md | claude${NC}"
  echo ""
  echo -e "  3. Resume exactly from this step:"
  echo -e "     ${CYAN}./run_workflow.sh --resume${NC}"
  echo ""
  echo -e "  4. See full status:"
  echo -e "     ${CYAN}./run_workflow.sh --list${NC}"
  echo ""
  echo -e "${YELLOW}── Checkpoint saved — --resume will skip completed steps ─${NC}"
  echo ""
}

git_commit() { git add . && git commit -m "$1" || warn "Nothing to commit"; }

# ─── Handle --list / --reset ─────────────────────────────────────────────────
[ "$LIST_MODE"  = true ] && { cp_list; exit 0; }
[ "$RESET_MODE" = true ] && { cp_reset; exit 0; }

# ─── Banner ───────────────────────────────────────────────────────────────────
echo -e "\n${BOLD}${GREEN}╔══════════════════════════════════════════════════════╗${NC}"
echo -e "${BOLD}${GREEN}║   Architect Banking App — Workflow Runner            ║${NC}"
echo -e "${BOLD}${GREEN}╚══════════════════════════════════════════════════════╝${NC}\n"
log "Model: $CLAUDE_MODEL  |  Logs: $LOG_DIR"
[ "$RESUME"         = true ] && log "Mode: RESUME from last failure"
[ -n "$FROM_PHASE"         ] && log "Mode: FROM $FROM_PHASE"
[ -n "$ONLY_PHASE"         ] && log "Mode: ONLY $ONLY_PHASE"
[ "$SKIP_LOGIN"     = true ] && log "Mode: SKIP Phase 1 & 2 (login done ✅)"

if [ "$RESUME" = true ]; then
  cp_list
  last=$(cp_last_failed)
  [ -z "$last" ] && { warn "No failed step found. Run --list to check."; exit 0; }
  log "Resuming from: $last"
fi

# =============================================================================
# PHASE 1 — Project Setup
# =============================================================================
if should_run "phase1" "setup"; then
  phase "PHASE 1: Project Setup"
  run_claude "phase1_setup" \
    "$PROMPTS/CONTEXT.md" "$PROMPTS/ARCHITECTURE.md" "$PROMPTS/DECISIONS.md" \
    "$PROMPTS/contract/naming.md" "$PROMPTS/contract/constraints.md"
fi
if should_run "phase1" "build"; then
  run_gradle "phase1_build" "build"
  git_commit "chore: project setup"
fi

# =============================================================================
# PHASE 2 — Login
# =============================================================================
if should_run "phase2" "login_api"; then
  phase "PHASE 2: Login Feature"
  run_claude "phase2_login_api" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/api_contract.md" "$PROMPTS/features/login/api.md"
fi
if should_run "phase2" "login_api_build"; then run_gradle "phase2_login_api_build" ":feature:login:compileDebugKotlin"; fi

if should_run "phase2" "login_db"; then
  run_claude "phase2_login_db" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/db_contract.md"
fi
if should_run "phase2" "login_domain"; then
  run_claude "phase2_login_domain" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/domain_contract.md" "$PROMPTS/features/login/domain.md"
fi
if should_run "phase2" "login_domain_build"; then run_gradle "phase2_login_domain_build" ":feature:login:compileDebugKotlin"; fi

if should_run "phase2" "login_ui"; then
  run_claude "phase2_login_ui" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/mvi.md" \
    "$PROMPTS/contract/sdui_contract.md" "$PROMPTS/contract/renderer.md" \
    "$PROMPTS/features/login/contract.md" "$PROMPTS/features/login/ui.md"
fi
if should_run "phase2" "login_ui_build"; then run_gradle "phase2_login_ui_build" ":feature:login:compileDebugKotlin"; fi

if should_run "phase2" "login_test"; then
  run_claude "phase2_login_test" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/testing.md" "$PROMPTS/features/login/test.md"
fi
if should_run "phase2" "login_test_run"; then run_gradle "phase2_login_test_run" ":feature:login:testDebugUnitTest"; fi

if should_run "phase2" "login_validate"; then
  run_claude "phase2_login_validate" "$PROMPTS/CONTEXT.md" "$PROMPTS/features/validate.md"
  git_commit "feat: login — SDUI + MVI + tests ✅"
fi

# =============================================================================
# PHASE 3 — Composite Components
# =============================================================================
if should_run "phase3" "composite"; then
  phase "PHASE 3: Composite Components + MainScreen"
  run_claude "phase3_composite" "$PROMPTS/CONTEXT.md" \
    "$PROMPTS/contract/sdui_contract.md" "$PROMPTS/contract/renderer.md" \
    "$PROMPTS/composite/composite_components.md"
fi
if should_run "phase3" "composite_ui_build";   then run_gradle "phase3_composite_ui_build"   ":core:ui:compileDebugKotlin"; fi
if should_run "phase3" "composite_sdui_build"; then run_gradle "phase3_composite_sdui_build" ":engine:sdui:compileDebugKotlin"; fi
if should_run "phase3" "composite_dash_build"; then
  run_gradle "phase3_composite_dash_build" ":feature:dashboard:compileDebugKotlin"
  git_commit "feat: composite components + MainScreen bottom nav ✅"
fi

# =============================================================================
# PHASE 4 — Home Tab
# =============================================================================
if should_run "phase4" "home_api"; then
  phase "PHASE 4: Home Tab (Dashboard)"
  run_claude "phase4_home_api" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/api_contract.md" "$PROMPTS/features/home/api.md"
fi
if should_run "phase4" "home_api_build"; then run_gradle "phase4_home_api_build" ":feature:dashboard:compileDebugKotlin"; fi

if should_run "phase4" "home_domain"; then
  run_claude "phase4_home_domain" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/domain_contract.md" "$PROMPTS/features/home/domain.md"
fi
if should_run "phase4" "home_domain_build"; then run_gradle "phase4_home_domain_build" ":feature:dashboard:compileDebugKotlin"; fi

if should_run "phase4" "home_ui"; then
  run_claude "phase4_home_ui" "$PROMPTS/CONTEXT.md" \
    "$PROMPTS/contract/mvi.md" "$PROMPTS/contract/sdui_contract.md" "$PROMPTS/contract/renderer.md" \
    "$PROMPTS/composite/composite_components.md" \
    "$PROMPTS/features/home/contract.md" "$PROMPTS/features/home/feature.md" "$PROMPTS/features/home/ui.md"
fi
if should_run "phase4" "home_ui_build"; then run_gradle "phase4_home_ui_build" ":feature:dashboard:compileDebugKotlin"; fi

if should_run "phase4" "home_test"; then
  run_claude "phase4_home_test" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/testing.md" "$PROMPTS/features/home/test.md"
fi
if should_run "phase4" "home_test_run"; then run_gradle "phase4_home_test_run" ":feature:dashboard:testDebugUnitTest"; fi

if should_run "phase4" "home_validate"; then
  run_claude "phase4_home_validate" "$PROMPTS/CONTEXT.md" "$PROMPTS/features/validate.md" "$PROMPTS/features/home/validate.md"
  git_commit "feat: home dashboard tab — SDUI + MVI + tests ✅"
fi

# =============================================================================
# PHASE 5 — Payments Tab (Transfer + Add Beneficiary)
# =============================================================================
if should_run "phase5" "payments_api"; then
  phase "PHASE 5: Payments Tab (Transfer + Add Beneficiary)"
  run_claude "phase5_payments_api" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/api_contract.md" "$PROMPTS/features/payments/api.md"
fi
if should_run "phase5" "payments_api_build"; then run_gradle "phase5_payments_api_build" ":feature:transfer:compileDebugKotlin"; fi

if should_run "phase5" "payments_domain"; then
  run_claude "phase5_payments_domain" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/domain_contract.md" "$PROMPTS/features/payments/domain.md"
fi
if should_run "phase5" "payments_domain_build"; then run_gradle "phase5_payments_domain_build" ":feature:transfer:compileDebugKotlin"; fi

if should_run "phase5" "payments_ui"; then
  run_claude "phase5_payments_ui" "$PROMPTS/CONTEXT.md" \
    "$PROMPTS/contract/mvi.md" "$PROMPTS/contract/sdui_contract.md" "$PROMPTS/contract/renderer.md" \
    "$PROMPTS/composite/composite_components.md" \
    "$PROMPTS/features/payments/contract.md" "$PROMPTS/features/payments/feature.md" "$PROMPTS/features/payments/ui.md"
fi
if should_run "phase5" "payments_ui_build"; then run_gradle "phase5_payments_ui_build" ":feature:transfer:compileDebugKotlin"; fi

if should_run "phase5" "payments_test"; then
  run_claude "phase5_payments_test" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/testing.md" "$PROMPTS/features/payments/test.md"
fi
if should_run "phase5" "payments_test_run"; then run_gradle "phase5_payments_test_run" ":feature:transfer:testDebugUnitTest"; fi

if should_run "phase5" "payments_validate"; then
  run_claude "phase5_payments_validate" "$PROMPTS/CONTEXT.md" "$PROMPTS/features/validate.md" "$PROMPTS/features/payments/validate.md"
  git_commit "feat: payments tab (transfer) — SDUI + MVI + tests ✅"
fi

# Add Beneficiary sub-screen (still Phase 5 — same :feature:transfer module)
if should_run "phase5" "add_ben_ui"; then
  run_claude "phase5_add_ben_ui" "$PROMPTS/CONTEXT.md" \
    "$PROMPTS/contract/mvi.md" "$PROMPTS/contract/sdui_contract.md" "$PROMPTS/contract/renderer.md" \
    "$PROMPTS/composite/composite_components.md" "$PROMPTS/composite/loading_skeleton.md" \
    "$PROMPTS/features/add_beneficiary/feature.md" \
    "$PROMPTS/features/add_beneficiary/api.md" \
    "$PROMPTS/features/add_beneficiary/domain.md" \
    "$PROMPTS/features/add_beneficiary/contract.md" \
    "$PROMPTS/features/add_beneficiary/ui.md"
fi
if should_run "phase5" "add_ben_build"; then run_gradle "phase5_add_ben_build" ":feature:transfer:compileDebugKotlin"; fi

if should_run "phase5" "add_ben_test"; then
  run_claude "phase5_add_ben_test" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/testing.md" \
    "$PROMPTS/features/add_beneficiary/test.md"
fi
if should_run "phase5" "add_ben_test_run"; then run_gradle "phase5_add_ben_test_run" ":feature:transfer:testDebugUnitTest"; fi

if should_run "phase5" "add_ben_validate"; then
  run_claude "phase5_add_ben_validate" "$PROMPTS/CONTEXT.md" "$PROMPTS/features/validate.md" \
    "$PROMPTS/features/add_beneficiary/validate.md"
  git_commit "feat: add beneficiary sub-screen — SDUI + MVI + tests ✅"
fi

# =============================================================================
# PHASE 6 — Accounts Tab
# =============================================================================
if should_run "phase6" "accounts_ui"; then
  phase "PHASE 6: Accounts Tab"
  run_claude "phase6_accounts_ui" "$PROMPTS/CONTEXT.md" \
    "$PROMPTS/contract/mvi.md" "$PROMPTS/contract/sdui_contract.md" "$PROMPTS/contract/renderer.md" \
    "$PROMPTS/contract/api_contract.md" "$PROMPTS/contract/domain_contract.md" \
    "$PROMPTS/composite/composite_components.md" \
    "$PROMPTS/features/accounts/feature.md"
fi
if should_run "phase6" "accounts_build"; then run_gradle "phase6_accounts_build" ":feature:accounts:compileDebugKotlin"; fi
if should_run "phase6" "accounts_test_run"; then run_gradle "phase6_accounts_test_run" ":feature:accounts:testDebugUnitTest"; fi
if should_run "phase6" "accounts_validate"; then
  run_claude "phase6_accounts_validate" "$PROMPTS/CONTEXT.md" "$PROMPTS/features/validate.md" "$PROMPTS/features/accounts/feature.md"
  git_commit "feat: accounts tab — SDUI + MVI + tests ✅"
fi

# =============================================================================
# PHASE 7 — Profile Tab
# =============================================================================
if should_run "phase7" "profile_ui"; then
  phase "PHASE 7: Profile Tab"
  run_claude "phase7_profile_ui" "$PROMPTS/CONTEXT.md" \
    "$PROMPTS/contract/mvi.md" "$PROMPTS/contract/sdui_contract.md" "$PROMPTS/contract/renderer.md" \
    "$PROMPTS/contract/api_contract.md" "$PROMPTS/contract/domain_contract.md" \
    "$PROMPTS/composite/composite_components.md" \
    "$PROMPTS/features/profile/feature.md"
fi
if should_run "phase7" "profile_build"; then run_gradle "phase7_profile_build" ":feature:profile:compileDebugKotlin"; fi
if should_run "phase7" "profile_test_run"; then run_gradle "phase7_profile_test_run" ":feature:profile:testDebugUnitTest"; fi
if should_run "phase7" "profile_validate"; then
  run_claude "phase7_profile_validate" "$PROMPTS/CONTEXT.md" "$PROMPTS/features/validate.md" "$PROMPTS/features/profile/feature.md"
  git_commit "feat: profile tab — SDUI + MVI + tests ✅"
fi

# =============================================================================
# PHASE 8 — Wire Navigation
# =============================================================================
if should_run "phase8" "navigation"; then
  phase "PHASE 8: Wire Navigation"
  run_claude "phase8_navigation" "$PROMPTS/CONTEXT.md" "$PROMPTS/contract/navigation.md"
fi
if should_run "phase8" "nav_build"; then
  run_gradle "phase8_nav_build" ":app:compileDebugKotlin"
  git_commit "feat: wire full navigation flow login → main tabs ✅"
fi

# =============================================================================
# PHASE 9 — Final Docs
# =============================================================================
if should_run "phase9" "docs"; then
  phase "PHASE 9: Final Documentation"
  run_claude "phase9_docs" "$PROMPTS/CONTEXT.md" "$PROMPTS/docs/generate_docs.md"
  git_commit "docs: generate full project documentation ✅"
fi

# ─── Done ─────────────────────────────────────────────────────────────────────
echo -e "\n${BOLD}${GREEN}╔══════════════════════════════════════════════════════╗${NC}"
echo -e "${BOLD}${GREEN}║   🎉  All phases complete!                           ║${NC}"
echo -e "${BOLD}${GREEN}╚══════════════════════════════════════════════════════╝${NC}\n"
