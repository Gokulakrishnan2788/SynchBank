# Login — Domain Layer
# cat with: CONTEXT.md + base/domain_contract.md + this file

## Generate these files ONLY
- LoginRepository.kt (interface)
- LoginRepositoryImpl.kt
- LoginUseCase.kt
- LogoutUseCase.kt
- GetSessionUseCase.kt
- LoginMapper.kt
- AuthSession.kt (domain model — if not exists in :core:domain)

## LoginRepository Interface
interface LoginRepository {
    suspend fun login(email: String, password: String): Result<AuthSession>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(refreshToken: String): Result<AuthSession>
    fun observeSession(): Flow<AuthSession?>
}

## LoginUseCase
Params: email: String, password: String
Validation:
  - email must not be blank
  - email must match email regex
  - password must not be blank
  - password must be >= 6 chars
On valid: calls repository.login()
Returns: Result<AuthSession>

## LogoutUseCase
Params: NoParams
Calls: repository.logout() + sessionDao.deleteAll()
Returns: Result<Unit>

## GetSessionUseCase
Params: NoParams
Returns: Flow<Result<AuthSession?>>
Use case: check if user already logged in on app launch

## LoginMapper
fun LoginResponseDto.toSession(): AuthSession
fun AuthSession.toEntity(): SessionEntity
fun SessionEntity.toSession(): AuthSession

## Rules
- AuthSession is a pure Kotlin data class — no Android/Room imports
- LoginRepositoryImpl in :feature:login — interface in :core:domain
- All validation in LoginUseCase, NOT in ViewModel or Repository
