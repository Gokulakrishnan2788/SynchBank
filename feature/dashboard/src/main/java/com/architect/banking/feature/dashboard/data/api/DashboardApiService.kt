package com.architect.banking.feature.dashboard.data.api

import com.architect.banking.core.network.model.ApiResponse
import com.architect.banking.feature.dashboard.data.dto.ChartDataDto
import com.architect.banking.feature.dashboard.data.dto.DashboardResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for dashboard endpoints.
 *
 * All requests are intercepted by [MockInterceptor] and served from local JSON assets.
 * No real network calls are made.
 */
interface DashboardApiService {

    /**
     * Fetches the complete dashboard data including net worth, accounts, and recent activity.
     *
     * Mock asset: `mock/api/dashboard.json`
     *
     * @return [ApiResponse] wrapping [DashboardResponseDto] on success.
     */
    @GET("dashboard")
    suspend fun getDashboard(): ApiResponse<DashboardResponseDto>

    /**
     * Fetches historical chart data for net worth visualization.
     *
     * Mock asset: `mock/api/dashboard_chart.json`
     *
     * @param period Time period for chart data (e.g., "6m", "3m", "1y"). Defaults to "6m".
     * @return [ApiResponse] wrapping [ChartDataDto] on success.
     */
    @GET("dashboard/chart")
    suspend fun getChartData(
        @Query("period") period: String = "6m"
    ): ApiResponse<ChartDataDto>
}
