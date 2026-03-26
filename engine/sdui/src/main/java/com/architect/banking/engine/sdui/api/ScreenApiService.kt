package com.architect.banking.engine.sdui.api

import com.architect.banking.core.network.model.ApiResponse
import com.architect.banking.engine.sdui.model.ScreenModel
//import retrofit2.http.GET
//import retrofit2.http.Path

/**
 * Retrofit service for fetching SDUI screen definitions.
 * All responses are served by [MockInterceptor] from `assets/mock/screens/`.
 */
interface ScreenApiService {

    /**
     * Fetches the SDUI screen definition for [screenId].
     *
     * @param screenId The screen identifier (e.g. "login", "dashboard").
     * @return [ApiResponse] wrapping the parsed [ScreenModel].
     */
//    @GET("screens/{screenId}")
//    suspend fun getScreen(@Path("screenId") screenId: String): ApiResponse<ScreenModel>
}
