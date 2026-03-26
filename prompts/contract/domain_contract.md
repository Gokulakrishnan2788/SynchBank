# Domain Contract
# UseCase and Repository patterns

## BaseUseCase (in :core:domain — DO NOT regenerate)
abstract class UseCase<in Params, out T> {
    abstract suspend operator fun invoke(params: Params): Result<T>
}

abstract class FlowUseCase<in Params, out T> {
    abstract operator fun invoke(params: Params): Flow<Result<T>>
}

object NoParams

## UseCase Pattern
class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) : UseCase<LoginUseCase.Params, AuthSession>() {

    data class Params(val email: String, val password: String)

    override suspend operator fun invoke(params: Params): Result<AuthSession> {
        // Validation
        if (!params.email.isValidEmail()) {
            return Result.Error("VALIDATION_ERROR", "Invalid email format")
        }
        // Delegate to repository
        return repository.login(params.email, params.password)
    }
}

## Repository Pattern
// Interface in :core:domain or :feature:x
interface LoginRepository {
    suspend fun login(email: String, password: String): Result<AuthSession>
    suspend fun logout(): Result<Unit>
    fun getActiveSession(): Flow<AuthSession?>
}

// Implementation in :feature:x
class LoginRepositoryImpl @Inject constructor(
    private val apiService: LoginApiService,
    private val sessionDao: SessionDao,
    private val mapper: LoginMapper
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<AuthSession> {
        return safeApiCall { apiService.login(LoginRequestDto(email, password)) }
            .map { dto ->
                val session = mapper.toSession(dto)
                sessionDao.insert(mapper.toEntity(session))
                session
            }
    }
}

## Domain Models (pure Kotlin — no Android/framework imports)
data class AuthSession(val userId: String, val token: String, val expiresAt: Long)
data class Account(val id: String, val name: String, val balance: Double, val currency: String)
data class Transaction(val id: String, val amount: Double, val description: String, val date: Long)

## Rules
- Domain models: pure Kotlin data classes, no @Serializable, no @Entity
- UseCase has exactly ONE public function: operator fun invoke()
- Repository interface in domain layer — implementation in data/feature layer
- No Android framework imports in domain layer
- All business validation logic in UseCase, not ViewModel or Repository
