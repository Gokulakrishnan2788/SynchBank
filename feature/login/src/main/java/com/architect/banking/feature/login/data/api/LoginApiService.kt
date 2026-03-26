package com.architect.banking.feature.login.data.api

import com.architect.banking.core.network.model.ApiResponse
import com.architect.banking.feature.login.data.dto.LoginRequestDto
import com.architect.banking.feature.login.data.dto.LoginResponseDto
import com.architect.banking.feature.login.data.dto.RefreshRequestDto
import com.architect.banking.feature.login.data.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface for authentication endpoints.
 *
 * All requests are intercepted by [MockInterceptor] and served from local JSON assets.
 * No real network calls are made.
 */
interface LoginApiService {

    /**
     * Authenticates a user with email and password credentials.
     *
     * Mock asset: `mock/api/auth_login.json`
     *
     * @param request The login credentials and device identifier.
     * @return [ApiResponse] wrapping [LoginResponseDto] on success.
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): ApiResponse<LoginResponseDto>

    /**
     * Exchanges a valid refresh token for a new access token pair.
     *
     * Mock asset: `mock/api/auth_refresh.json`
     *
     * @param request The current refresh token.
     * @return [ApiResponse] wrapping [TokenResponseDto] on success.
     */
    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequestDto): ApiResponse<TokenResponseDto>

    /**
     * Invalidates the current session on the server.
     *
     * Mock asset: not yet mapped — returns 404 from [MockInterceptor].
     */
    @POST("auth/logout")
    suspend fun logout(): ApiResponse<Unit>
}
