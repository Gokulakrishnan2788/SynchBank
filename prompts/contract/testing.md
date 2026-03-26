# Testing Contract
# Every feature must have tests. No exceptions.

## Test Dependencies (already in build.gradle.kts — DO NOT re-add)
testImplementation: junit4, mockk, kotlinx-coroutines-test, turbine
androidTestImplementation: compose-ui-test, hilt-android-testing

## ViewModel Test Pattern
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val mockUseCase: LoginUseCase = mockk()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() { viewModel = LoginViewModel(mockUseCase) }

    @Test
    fun `given valid credentials, when Submit intent, then NavigateToDashboard effect`() = runTest {
        // Given
        coEvery { mockUseCase(any()) } returns Result.Success(fakeSession)

        // When + Then
        viewModel.effect.test {
            viewModel.handleIntent(LoginIntent.Submit("user@test.com", "password"))
            assertEquals(LoginEffect.NavigateToDashboard, awaitItem())
        }
    }

    @Test
    fun `given invalid email, when Submit intent, then error state`() = runTest {
        viewModel.handleIntent(LoginIntent.Submit("not_an_email", "password"))
        assertEquals("Invalid email format", viewModel.state.value.error)
    }

    @Test
    fun `given API failure, when Submit intent, then error state with message`() = runTest {
        coEvery { mockUseCase(any()) } returns Result.Error("AUTH_FAILED", "Invalid credentials")
        viewModel.handleIntent(LoginIntent.Submit("user@test.com", "password"))
        assertEquals("Invalid credentials", viewModel.state.value.error)
    }
}

## UseCase Test Pattern
class LoginUseCaseTest {
    private val mockRepository: LoginRepository = mockk()
    private val useCase = LoginUseCase(mockRepository)

    @Test
    fun `given valid params, when invoked, then returns success`() = runTest {
        coEvery { mockRepository.login(any(), any()) } returns Result.Success(fakeSession)
        val result = useCase(LoginUseCase.Params("user@test.com", "pass"))
        assertTrue(result is Result.Success)
    }

    @Test
    fun `given invalid email, when invoked, then returns validation error`() = runTest {
        val result = useCase(LoginUseCase.Params("bad_email", "pass"))
        assertTrue(result is Result.Error)
        assertEquals("VALIDATION_ERROR", (result as Result.Error).code)
    }
}

## SDUI JSON Parsing Test Pattern
class ScreenModelParserTest {
    @Test
    fun `given login_screen json, when parsed, then correct component count`() {
        val json = readAsset("mock/screens/login_screen.json")
        val screen = ScreenModelParser.parse(json)
        assertEquals("login", screen.screenId)
        assertTrue(screen.components.isNotEmpty())
        assertNotNull(screen.actions["SUBMIT_FORM"])
    }
}

## Compose UI Test Pattern
@HiltAndroidTest
class LoginScreenTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `login screen renders username field from SDUI json`() {
        composeRule.onNodeWithTag("username_field").assertIsDisplayed()
    }

    @Test
    fun `tapping login button with empty fields shows error`() {
        composeRule.onNodeWithTag("login_btn").performClick()
        composeRule.onNodeWithText("This field is required").assertIsDisplayed()
    }
}

## Test File Naming
LoginViewModelTest.kt       → in :feature:login/test/
LoginUseCaseTest.kt         → in :feature:login/test/
LoginScreenModelTest.kt     → in :feature:login/test/
LoginScreenTest.kt          → in :feature:login/androidTest/

## MainDispatcherRule (in :core:domain/test — DO NOT regenerate)
Already provided. Sets Dispatchers.Main to TestCoroutineDispatcher.
