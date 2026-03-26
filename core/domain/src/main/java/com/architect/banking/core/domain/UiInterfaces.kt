package com.architect.banking.core.domain

/**
 * Marker interface for all MVI UI state classes.
 * Every feature's State data class must implement this.
 */
interface UiState

/**
 * Marker interface for all MVI UI intent (user action) sealed classes.
 * Every feature's Intent sealed class must implement this.
 */
interface UiIntent

/**
 * Marker interface for all MVI one-shot UI effect sealed classes.
 * Effects are used for navigation, toasts, and dialogs that fire once.
 * Every feature's Effect sealed class must implement this.
 */
interface UiEffect
