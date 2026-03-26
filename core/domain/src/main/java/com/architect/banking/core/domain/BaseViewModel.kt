package com.architect.banking.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel for all MVI features.
 *
 * Exposes:
 * - [state]: current UI state as a [StateFlow] (replayed on subscription)
 * - [effect]: one-shot events via [Channel] (not replayed on recomposition)
 *
 * Subclasses must implement [initialState] and [reduce].
 *
 * @param S UI state type — must implement [UiState]
 * @param I UI intent type — must implement [UiIntent]
 * @param E UI effect type — must implement [UiEffect]
 */
abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect> : ViewModel() {

    /** Returns the initial state when the ViewModel is first created. */
    abstract fun initialState(): S

    private val _state = MutableStateFlow(initialState())

    /** Observable UI state. Collect in composables via [collectAsStateWithLifecycle]. */
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = Channel<E>(Channel.BUFFERED)

    /** One-shot effects: navigation, toasts, dialogs. Use [LaunchedEffect] in composables. */
    val effect: Flow<E> = _effect.receiveAsFlow()

    /**
     * Entry point for all user actions. Schedules [reduce] on [viewModelScope].
     * Call from composables: `viewModel.handleIntent(SomeIntent.Action)`.
     */
    fun handleIntent(intent: I) {
        viewModelScope.launch { reduce(intent) }
    }

    /**
     * Processes [intent] and produces a new state and/or effect.
     * This is the single source of truth for state transitions.
     */
    protected abstract suspend fun reduce(intent: I)

    /**
     * Updates the current state atomically via [block].
     * Usage: `setState { copy(isLoading = true) }`
     */
    protected fun setState(block: S.() -> S) {
        _state.update { it.block() }
    }

    /**
     * Sends a one-shot [effect] to the UI layer.
     * Effects are buffered and delivered exactly once.
     */
    protected fun setEffect(effect: E) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
