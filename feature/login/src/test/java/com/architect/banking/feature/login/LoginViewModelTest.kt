package com.architect.banking.feature.login

import android.content.Context
import android.content.res.AssetManager
import app.cash.turbine.test
import com.architect.banking.core.domain.NoParams
import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.model.AuthSession
import com.architect.banking.engine.sdui.model.ScreenModel
import com.architect.banking.engine.sdui.parser.SDUIParser
import com.architect.banking.feature.login.domain.GetSessionUseCase
import com.architect.banking.feature.login.domain.LoginUseCase
import com.architect.banking.feature.login.domain.LogoutUseCase
import com.architect.banking.feature.login.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()
    private val getSessionUseCase: GetSessionUseCase = mockk()
    private val sduiParser: SDUIParser = mockk()
    private val context: Context = mockk()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        val assetManager = mockk<AssetManager>()
        every { context.assets } returns assetManager
        every { assetManager.open(any()) } returns "{}".byteInputStream()
        coEvery { sduiParser.parse(any()) } returns Result.Success(fakeScreenModel)
        coEvery { getSessionUseCase(NoParams) } returns flowOf(Result.Success(null))

        viewModel = LoginViewModel(loginUseCase, logoutUseCase, getSessionUseCase, sduiParser, context)
    }

    // ── 1. Session exists on init ─────────────────────────────────────────────

    @Test
    fun `given session exists, when init, then NavigateToDashboard effect emitted`() = runTest {
        coEvery { getSessionUseCase(NoParams) } returns flowOf(Result.Success(fakeSession))

        val sessionViewModel = LoginViewModel(
            loginUseCase, logoutUseCase, getSessionUseCase, sduiParser, context,
        )

        sessionViewModel.effect.test {
            assertEquals(LoginEffect.NavigateToDashboard, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── 2. No session on init — screen model loaded ───────────────────────────

    @Test
    fun `given no session, when init, then screen model is loaded into state`() = runTest {
        advanceUntilIdle()

        assertEquals(fakeScreenModel, viewModel.state.value.screenModel)
    }

    // ── 3. Valid credentials → navigate ──────────────────────────────────────

    @Test
    fun `given valid credentials, when Submit, then loading then NavigateToDashboard effect`() = runTest {
        coEvery { loginUseCase(any()) } returns Result.Success(fakeSession)

        viewModel.handleIntent(LoginIntent.UsernameChanged(VALID_EMAIL))
        viewModel.handleIntent(LoginIntent.PasswordChanged(VALID_PASSWORD))

        viewModel.effect.test {
            viewModel.handleIntent(LoginIntent.Submit)
            advanceUntilIdle()
            assertEquals(LoginEffect.NavigateToDashboard, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── 4. Invalid email → validation error ───────────────────────────────────

    @Test
    fun `given invalid email, when Submit, then validation error state`() = runTest {
        viewModel.handleIntent(LoginIntent.UsernameChanged("not_an_email"))
        viewModel.handleIntent(LoginIntent.PasswordChanged(VALID_PASSWORD))
        viewModel.handleIntent(LoginIntent.Submit)
        advanceUntilIdle()

        assertNull(viewModel.state.value.screenModel?.let { null } ?: viewModel.state.value.error?.let { null })
        assertEquals("Invalid email format", viewModel.state.value.error)
    }

    // ── 5. Empty password → validation error ─────────────────────────────────

    @Test
    fun `given empty password, when Submit, then validation error state`() = runTest {
        viewModel.handleIntent(LoginIntent.UsernameChanged(VALID_EMAIL))
        viewModel.handleIntent(LoginIntent.PasswordChanged(""))
        viewModel.handleIntent(LoginIntent.Submit)
        advanceUntilIdle()

        val error = viewModel.state.value.error
        assertTrue("Expected validation error, got: $error", error != null)
    }

    // ── 6. API failure → error state ─────────────────────────────────────────

    @Test
    fun `given API failure, when Submit, then error state with server message`() = runTest {
        coEvery { loginUseCase(any()) } returns Result.Error("AUTH_FAILED", "Invalid credentials")

        viewModel.handleIntent(LoginIntent.UsernameChanged(VALID_EMAIL))
        viewModel.handleIntent(LoginIntent.PasswordChanged(VALID_PASSWORD))
        viewModel.handleIntent(LoginIntent.Submit)
        advanceUntilIdle()

        assertEquals("Invalid credentials", viewModel.state.value.error)
    }

    // ── 7. FORGOT_PASSWORD action → effect ────────────────────────────────────

    @Test
    fun `given FORGOT_PASSWORD action, when HandleAction, then NavigateToForgotPassword effect`() = runTest {
        viewModel.effect.test {
            viewModel.handleIntent(LoginIntent.HandleAction("FORGOT_PASSWORD"))
            assertEquals(LoginEffect.NavigateToForgotPassword, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── 8. INQUIRE action → effect ────────────────────────────────────────────

    @Test
    fun `given INQUIRE action, when HandleAction, then NavigateToInquire effect`() = runTest {
        viewModel.effect.test {
            viewModel.handleIntent(LoginIntent.HandleAction("INQUIRE"))
            assertEquals(LoginEffect.NavigateToInquire, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── 9. ClearError clears error state ─────────────────────────────────────

    @Test
    fun `given error state, when ClearError intent, then error is null`() = runTest {
        viewModel.handleIntent(LoginIntent.UsernameChanged("bad"))
        viewModel.handleIntent(LoginIntent.Submit)
        advanceUntilIdle()

        viewModel.handleIntent(LoginIntent.ClearError)
        advanceUntilIdle()

        assertNull(viewModel.state.value.error)
    }

    // ── 10. UsernameChanged updates state ────────────────────────────────────

    @Test
    fun `given UsernameChanged intent, when dispatched, then state is updated`() = runTest {
        viewModel.handleIntent(LoginIntent.UsernameChanged(VALID_EMAIL))
        advanceUntilIdle()

        assertEquals(VALID_EMAIL, viewModel.state.value.usernameInput)
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private fun assertTrue(message: String, condition: Boolean) {
        org.junit.Assert.assertTrue(message, condition)
    }

    private companion object {
        const val VALID_EMAIL = "user@architect.com"
        const val VALID_PASSWORD = "password123"

        val fakeSession = AuthSession(
            userId = "usr_001",
            token = "mock_token",
            refreshToken = "mock_refresh",
            expiresAt = Long.MAX_VALUE,
        )

        val fakeScreenModel = ScreenModel(screenId = "login")
    }
}
