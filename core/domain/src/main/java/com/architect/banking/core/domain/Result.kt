package com.architect.banking.core.domain

/**
 * Unified result wrapper for all async operations in the app.
 * Replaces null-checking and exception-based error handling with explicit states.
 *
 * @param T The type of the success data.
 */
sealed class Result<out T> {

    /** Operation completed successfully with [data]. */
    data class Success<T>(val data: T) : Result<T>()

    /** Operation failed with [code] and human-readable [message]. */
    data class Error(val code: String, val message: String) : Result<Nothing>()

    /** Operation is in progress. */
    object Loading : Result<Nothing>()
}

/**
 * Maps a [Result.Success] value using [transform], leaving [Result.Error] and
 * [Result.Loading] unchanged.
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
    is Result.Loading -> this
}

/**
 * Executes [onSuccess] when this result is [Result.Success].
 * Returns the original result to allow chaining.
 */
inline fun <T> Result<T>.onSuccess(onSuccess: (T) -> Unit): Result<T> {
    if (this is Result.Success) onSuccess(data)
    return this
}

/**
 * Executes [onError] when this result is [Result.Error].
 * Returns the original result to allow chaining.
 */
inline fun <T> Result<T>.onError(onError: (Result.Error) -> Unit): Result<T> {
    if (this is Result.Error) onError(this)
    return this
}
