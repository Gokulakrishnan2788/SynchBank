package com.architect.banking.core.domain

import kotlinx.coroutines.flow.Flow

/**
 * Base class for use cases that perform a single suspend operation.
 *
 * @param Params Input parameters. Use [NoParams] when no input is needed.
 * @param T The type of the success result.
 */
abstract class UseCase<in Params, out T> {

    /**
     * Executes the use case with [params].
     * All business validation logic lives here, not in ViewModels or Repositories.
     */
    abstract suspend operator fun invoke(params: Params): Result<T>
}

/**
 * Base class for use cases that emit a stream of results via [Flow].
 *
 * @param Params Input parameters. Use [NoParams] when no input is needed.
 * @param T The type of each emitted value.
 */
abstract class FlowUseCase<in Params, out T> {

    /** Returns a [Flow] of [Result] values for the given [params]. */
    abstract operator fun invoke(params: Params): Flow<Result<T>>
}

/** Sentinel object for use cases that require no input parameters. */
object NoParams
