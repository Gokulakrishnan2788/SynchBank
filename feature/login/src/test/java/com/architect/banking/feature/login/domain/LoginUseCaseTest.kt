package com.architect.banking.feature.login.domain

import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.model.AuthSession
import com.architect.banking.core.domain.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private val repository: LoginRepository = mockk()
    private lateinit var useCase: LoginUseCase

    @Before
    fun setUp() {
        useCase = LoginUseCase(repository)
    }

    // ── Happy path ────────────────────────────────────────────────────────────

    @Test
    fun `given valid params, when invoked, then repository called and returns success`() = runTest {
        coEvery { repository.login(any(), any()) } returns Result.Success(fakeSession)

        val result = useCase(LoginUseCase.Params(VALID_EMAIL, VALID_PASSWORD))

        assertTrue(result is Result.Success)
        assertEquals(fakeSession, (result as Result.Success).data)
        coVerify(exactly = 1) { repository.login(VALID_EMAIL, VALID_PASSWORD) }
    }

    // ── Validation failures ───────────────────────────────────────────────────

    @Test
    fun `given invalid email format, when invoked, then validation error`() = runTest {
        val result = useCase(LoginUseCase.Params("not_an_email", VALID_PASSWORD))

        assertTrue(result is Result.Error)
        assertEquals("VALIDATION_ERROR", (result as Result.Error).code)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun `given blank email, when invoked, then validation error`() = runTest {
        val result = useCase(LoginUseCase.Params("   ", VALID_PASSWORD))

        assertTrue(result is Result.Error)
        assertEquals("VALIDATION_ERROR", (result as Result.Error).code)
    }

    @Test
    fun `given short password, when invoked, then validation error`() = runTest {
        val result = useCase(LoginUseCase.Params(VALID_EMAIL, "abc"))

        assertTrue(result is Result.Error)
        assertEquals("VALIDATION_ERROR", (result as Result.Error).code)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    // ── Repository failure ────────────────────────────────────────────────────

    @Test
    fun `given repository failure, when invoked, then error propagated`() = runTest {
        coEvery { repository.login(any(), any()) } returns
            Result.Error("AUTH_FAILED", "Invalid credentials")

        val result = useCase(LoginUseCase.Params(VALID_EMAIL, VALID_PASSWORD))

        assertTrue(result is Result.Error)
        assertEquals("AUTH_FAILED", (result as Result.Error).code)
        assertEquals("Invalid credentials", result.message)
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private companion object {
        const val VALID_EMAIL = "user@architect.com"
        const val VALID_PASSWORD = "password123"

        val fakeSession = AuthSession(
            userId = "usr_001",
            token = "mock_token",
            refreshToken = "mock_refresh",
            expiresAt = Long.MAX_VALUE,
        )
    }
}
