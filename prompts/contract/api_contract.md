# API Contract
# All APIs are mocked. Retrofit interface is real. MockInterceptor serves JSON from assets.

## MockInterceptor Pattern (in :core:network — DO NOT regenerate)
Intercepts all requests
Maps URL pattern → assets/mock/api/*.json
Returns 200 with file contents
Add new mocks by adding JSON files — no code change needed

## Base Response Wrapper
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null,
    val meta: ResponseMeta? = null
)

@Serializable
data class ApiError(val code: String, val message: String)

@Serializable
data class ResponseMeta(val page: Int? = null, val total: Int? = null)

## Result Wrapper (in :core:domain)
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val code: String, val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

suspend fun <T> safeApiCall(call: suspend () -> ApiResponse<T>): Result<T> {
    return try {
        val response = call()
        if (response.success && response.data != null)
            Result.Success(response.data)
        else
            Result.Error(response.error?.code ?: "UNKNOWN", response.error?.message ?: "Unknown error")
    } catch (e: Exception) {
        Result.Error("NETWORK_ERROR", e.message ?: "Network error")
    }
}

## API Services Pattern
interface LoginApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): ApiResponse<LoginResponseDto>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequestDto): ApiResponse<TokenResponseDto>
}

## Mock JSON Files Location
assets/mock/api/auth_login.json          → POST /auth/login
assets/mock/api/auth_refresh.json        → POST /auth/refresh
assets/mock/api/accounts_list.json       → GET /accounts
assets/mock/api/account_detail.json      → GET /accounts/{id}
assets/mock/api/transactions_list.json   → GET /transactions
assets/mock/api/transfer_submit.json     → POST /transfer

## DTO Rules
- All DTOs annotated with @Serializable
- Suffix: RequestDto, ResponseDto
- No business logic in DTOs
- Mapper extension functions in XMapper.kt
- Nullable fields for optional API fields
