package com.architect.banking.core.network.interceptor

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp interceptor that intercepts all Retrofit requests and returns
 * pre-built JSON responses from [assets/mock/].
 *
 * No real network calls are made. Swap this out for production by removing
 * it from the [OkHttpClient] in [NetworkModule].
 *
 * URL → asset mapping rules (checked in order):
 * 1. Exact path match from [URL_TO_ASSET_MAP]
 * 2. Pattern match (e.g. /screens/{screenId})
 * 3. Fallback: 404 response
 */
@Singleton
class MockInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url.encodedPath
        val assetPath = resolveAssetPath(path)
        val (code, body) = if (assetPath != null) {
            200 to readAsset(assetPath)
        } else {
            404 to """{"success":false,"error":{"code":"NOT_FOUND","message":"Mock not found for: $path"}}"""
        }

        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(if (code == 200) "OK" else "Not Found")
            .body(body.toResponseBody(JSON_MEDIA_TYPE))
            .build()
    }

    private fun resolveAssetPath(path: String): String? {
        // 1. Exact match
        URL_TO_ASSET_MAP[path]?.let { return it }

        // 2. Pattern match for parameterised paths
        for ((pattern, assetFile) in URL_PATTERNS) {
            if (path.matches(pattern)) return assetFile
        }

        return null
    }

    private fun readAsset(assetPath: String): String = try {
        context.assets.open(assetPath).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        """{"success":false,"error":{"code":"ASSET_NOT_FOUND","message":"Asset missing: $assetPath"}}"""
    }

    private companion object {
        val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

        /** Exact URL path → asset file path mappings. */
        val URL_TO_ASSET_MAP = mapOf(
            "/auth/login" to "mock/api/auth_login.json",
            "/auth/refresh" to "mock/api/auth_refresh.json",
            "/accounts" to "mock/api/accounts_list.json",
            "/transactions" to "mock/api/transactions_list.json",
            "/transfer" to "mock/api/transfer_submit.json",
            "/screens/login" to "mock/screens/login_screen.json",
            "/screens/dashboard" to "mock/screens/dashboard_screen.json",
            "/screens/profile" to "mock/screens/profile_screen.json",
            "/nav/config" to "mock/nav/navigation_config.json",
        )

        /** Regex pattern → asset file path for parameterised routes. */
        val URL_PATTERNS = listOf(
            Regex("/accounts/.*") to "mock/api/account_detail.json",
        )
    }
}
