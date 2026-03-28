# Loading Skeleton Box — Screen Loading State
# cat with: CONTEXT.md + contract/renderer.md + this file
# Apply this to ALL feature screens: home, payments, accounts, profile, add_beneficiary

## Requirement
Every screen MUST show a skeleton/shimmer placeholder box while the SDUI
screenModel is null (loading state). Only after screenModel is loaded should
the SDUIRenderer render the actual components.
This matches the instruction: "Each screen uses the box before it loads the dynamic views"

## Implementation

### 1. ScreenLoadingBox.kt (add to :core:ui)
@Composable
fun ScreenLoadingBox(modifier: Modifier = Modifier) {
    // Full screen shimmer skeleton — matches approximate layout of each screen
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.BackgroundGrey)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Header skeleton
        ShimmerBox(width = 120.dp, height = 24.dp, cornerRadius = 8.dp)
        Spacer(modifier = Modifier.height(24.dp))
        // Hero skeleton
        ShimmerBox(width = 200.dp, height = 40.dp, cornerRadius = 8.dp)
        Spacer(modifier = Modifier.height(8.dp))
        ShimmerBox(width = 100.dp, height = 16.dp, cornerRadius = 8.dp)
        Spacer(modifier = Modifier.height(24.dp))
        // Card skeleton 1
        ShimmerBox(width = Dp.Unspecified, height = 100.dp, cornerRadius = 12.dp, fillMaxWidth = true)
        Spacer(modifier = Modifier.height(12.dp))
        // Card skeleton 2
        ShimmerBox(width = Dp.Unspecified, height = 100.dp, cornerRadius = 12.dp, fillMaxWidth = true)
        Spacer(modifier = Modifier.height(12.dp))
        // Card skeleton 3
        ShimmerBox(width = Dp.Unspecified, height = 80.dp, cornerRadius = 12.dp, fillMaxWidth = true)
        Spacer(modifier = Modifier.height(24.dp))
        // List skeleton rows
        repeat(3) {
            ShimmerBox(width = Dp.Unspecified, height = 56.dp, cornerRadius = 8.dp, fillMaxWidth = true)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

### 2. ShimmerBox.kt (add to :core:ui)
@Composable
fun ShimmerBox(
    width: Dp,
    height: Dp,
    cornerRadius: Dp = 8.dp,
    fillMaxWidth: Boolean = false
) {
    val shimmerColors = listOf(
        Color(0xFFE5E7EB),
        Color(0xFFF9FAFB),
        Color(0xFFE5E7EB)
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, 0f),
        end = Offset(translateAnim, 0f)
    )
    Box(
        modifier = Modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier.width(width))
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

### 3. Update every Screen composable to use ScreenLoadingBox
# Pattern to apply in HomeScreen, AccountsScreen, TransferScreen, ProfileScreen, AddBeneficiaryScreen:

@Composable
fun HomeScreen(viewModel: DashboardViewModel = hiltViewModel(), ...) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Effect handling
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect -> /* handle effects */ }
    }

    // ← LOADING BOX: show skeleton while screenModel is null
    if (state.isLoading || state.screenModel == null) {
        ScreenLoadingBox(modifier = Modifier.fillMaxSize())
    } else {
        SDUIRenderer(
            screenModel = state.screenModel!!,
            onAction = { actionId -> viewModel.handleIntent(DashboardIntent.HandleAction(actionId)) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

# Apply the exact same pattern to ALL screens:
# - TransferScreen (state.isLoading || state.screenModel == null) → ScreenLoadingBox
# - AccountsScreen (state.isLoading || state.screenModel == null) → ScreenLoadingBox
# - ProfileScreen  (state.isLoading || state.screenModel == null) → ScreenLoadingBox
# - AddBeneficiaryScreen (state.isLoading || state.screenModel == null) → ScreenLoadingBox

### 4. SDUIRenderer update (append — DO NOT replace)
# In SDUIRenderer.kt, the null/loading state is now handled at Screen level.
# SDUIRenderer only renders when screenModel is non-null.
# DO NOT add skeleton logic inside SDUIRenderer itself.

## Rules
- ScreenLoadingBox and ShimmerBox go in :core:ui — never in feature modules
- Every screen MUST use this pattern — no exceptions
- Shimmer animation uses LinearEasing infinite transition
- Skeleton layout approximates the real screen structure (header + hero + cards + list rows)
- shimmer bg: #E5E7EB → #F9FAFB → #E5E7EB gradient sweep
