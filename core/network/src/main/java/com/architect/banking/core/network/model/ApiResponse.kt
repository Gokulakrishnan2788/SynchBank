package com.architect.banking.core.network.model

import com.architect.banking.core.domain.Result
import kotlinx.serialization.Serializable

/**
 * Generic API response wrapper used for all Retrofit calls.
 * MockInterceptor always returns responses in this shape.
 *
 * @param T The type of the payload in [data].
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null,
    val meta: ResponseMeta? = null,
)

/**
 * Error payload returned when [ApiResponse.success] is false.
 *
 * @property code Machine-readable error code (e.g. "INVALID_CREDENTIALS").
 * @property message Human-readable error description.
 */
@Serializable
data class ApiError(
    val code: String,
    val message: String,
)

/**
 * Optional pagination metadata attached to list responses.
 *
 * @property page Current page number (1-indexed).
 * @property total Total number of items across all pages.
 */
@Serializable
data class ResponseMeta(
    val page: Int? = null,
    val total: Int? = null,
)

/**
 * Safely executes [call] and maps the [ApiResponse] to a domain [Result].
 * Catches any exception (network, parsing) and wraps it in [Result.Error].
 *
 * @param call Suspend lambda that calls a Retrofit service method.
 * @return [Result.Success] with the response data, or [Result.Error] on failure.
 */
suspend fun <T> safeApiCall(call: suspend () -> ApiResponse<T>): Result<T> {
    return try {
        val response = call()
        if (response.success && response.data != null) {
            Result.Success(response.data)
        } else {
            Result.Error(
                code = response.error?.code ?: "UNKNOWN_ERROR",
                message = response.error?.message ?: "An unknown error occurred",
            )
        }
    } catch (e: Exception) {
        Result.Error(
            code = "NETWORK_ERROR",
            message = e.message ?: "Network error",
        )
    }
}
