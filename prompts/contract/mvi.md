# MVI Contract
# Use this EXACT pattern for every feature. Zero deviation.

## BaseViewModel (already in :core:domain — DO NOT regenerate)
abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect> : ViewModel() {
    abstract fun initialState(): S
    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<S> = _state.asStateFlow()
    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()
    fun handleIntent(intent: I) { viewModelScope.launch { reduce(intent) } }
    protected abstract suspend fun reduce(intent: I)
    protected fun setState(block: S.() -> S) { _state.update { it.block() } }
    protected fun setEffect(effect: E) { viewModelScope.launch { _effect.send(effect) } }
}

## Marker interfaces (in :core:domain — DO NOT regenerate)
interface UiState
interface UiIntent
interface UiEffect

## Per-Feature Pattern

### XState.kt (one file for all three)
data class XState(
    val isLoading: Boolean = false,
    val error: String? = null,
    // feature-specific fields
) : UiState

sealed class XIntent : UiIntent {
    // user actions as data classes/objects
}

sealed class XEffect : UiEffect {
    // one-shot events: navigation, toasts
}

### XViewModel.kt
@HiltViewModel
class XViewModel @Inject constructor(
    private val useCase: XUseCase
) : BaseViewModel<XState, XIntent, XEffect>() {

    override fun initialState() = XState()

    override suspend fun reduce(intent: XIntent) {
        when (intent) {
            is XIntent.SomeAction -> handleSomeAction(intent)
        }
    }

    private suspend fun handleSomeAction(intent: XIntent.SomeAction) {
        setState { copy(isLoading = true) }
        useCase(intent.param)
            .onSuccess { result ->
                setState { copy(isLoading = false, data = result) }
                setEffect(XEffect.NavigateNext)
            }
            .onFailure { error ->
                setState { copy(isLoading = false, error = error.message) }
            }
    }
}

### XScreen.kt
@Composable
fun XScreen(
    navController: NavController,
    viewModel: XViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is XEffect.NavigateNext -> navController.navigate(Routes.NEXT)
            }
        }
    }

    XScreenContent(
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@Composable
private fun XScreenContent(
    state: XState,
    onIntent: (XIntent) -> Unit
) {
    // Pure UI — no ViewModel reference here
    SDUIRenderer(
        components = state.screenData?.components ?: emptyList(),
        onAction = { action -> onIntent(XIntent.HandleAction(action)) }
    )
}
