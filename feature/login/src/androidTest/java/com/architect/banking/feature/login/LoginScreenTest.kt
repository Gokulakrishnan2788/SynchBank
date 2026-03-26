package com.architect.banking.feature.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.architect.banking.core.domain.Result
import com.architect.banking.engine.sdui.model.ScreenModel
import com.architect.banking.engine.sdui.parser.SDUIParser
import com.architect.banking.engine.sdui.renderer.SDUIRenderer
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Compose UI tests for the Login screen's SDUI rendering.
 *
 * Uses [createComposeRule] (no Activity required) and verifies that
 * [SDUIRenderer] correctly renders the parsed login screen JSON.
 * Tests are independent of [LoginViewModel] — they validate the rendering layer only.
 */
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var screenModel: ScreenModel

    @Before
    fun setUp() {
        val parser = SDUIParser(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            },
        )
        val result = parser.parse(LOGIN_SCREEN_JSON)
        check(result is Result.Success) { "Failed to parse login screen JSON for tests" }
        screenModel = result.data
    }

    @Test
    fun usernameField_isDisplayed() {
        composeRule.setContent {
            SDUIRenderer(screenModel = screenModel, onAction = {})
        }
        composeRule.onNodeWithText("USERNAME").assertIsDisplayed()
    }

    @Test
    fun passwordField_isDisplayed() {
        composeRule.setContent {
            SDUIRenderer(screenModel = screenModel, onAction = {})
        }
        composeRule.onNodeWithText("PASSWORD").assertIsDisplayed()
    }

    @Test
    fun loginButton_isDisplayed() {
        composeRule.setContent {
            SDUIRenderer(screenModel = screenModel, onAction = {})
        }
        composeRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun loginButton_isEnabled_andClickable() {
        var actionFired = false
        composeRule.setContent {
            SDUIRenderer(
                screenModel = screenModel,
                onAction = { actionId ->
                    if (actionId == "SUBMIT_FORM") actionFired = true
                },
            )
        }
        composeRule.onNodeWithText("Login").assertIsEnabled().performClick()
        assert(actionFired) { "Expected SUBMIT_FORM action to be dispatched on Login button click" }
    }

    @Test
    fun biometricRow_isDisplayed() {
        composeRule.setContent {
            SDUIRenderer(screenModel = screenModel, onAction = {})
        }
        composeRule.onNodeWithText("AUTHENTICATION").assertIsDisplayed()
    }

    @Test
    fun forgotLink_inquireText_isDisplayed() {
        composeRule.setContent {
            SDUIRenderer(screenModel = screenModel, onAction = {})
        }
        composeRule.onNodeWithText("Inquire about an account").assertIsDisplayed()
    }

    private companion object {
        val LOGIN_SCREEN_JSON = """
            {
              "screenId": "login",
              "version": "1.0",
              "metadata": { "title": "Secure Portal", "analyticsTag": "login_screen" },
              "layout": { "type": "COLUMN", "padding": { "horizontal": 24, "vertical": 48 } },
              "components": [
                { "id": "logo", "type": "IMAGE", "visible": true,
                  "props": { "localAsset": "ic_architect_logo", "contentScale": "FIT", "aspectRatio": 1.0 } },
                { "id": "spacer_1", "type": "SPACER", "visible": true, "props": { "height": 48 } },
                { "id": "title", "type": "TEXT", "visible": true,
                  "props": { "text": "Secure\nPortal", "style": "DISPLAY", "color": "NavyPrimary" } },
                { "id": "username_field", "type": "TEXT_FIELD", "visible": true,
                  "props": { "label": "USERNAME", "hint": "Institutional ID or Email",
                    "inputType": "EMAIL", "required": true } },
                { "id": "password_field", "type": "TEXT_FIELD", "visible": true,
                  "props": { "label": "PASSWORD", "hint": "", "inputType": "PASSWORD", "required": true },
                  "action": "FORGOT_PASSWORD" },
                { "id": "login_btn", "type": "BUTTON", "visible": true,
                  "props": { "label": "Login", "style": "PRIMARY", "loading": false },
                  "action": "SUBMIT_FORM" },
                { "id": "biometric", "type": "BIOMETRIC_ROW", "visible": true,
                  "props": { "options": ["FINGERPRINT", "FACE_ID"], "label": "AUTHENTICATION" } },
                { "id": "register_link", "type": "LINK_TEXT", "visible": true,
                  "props": { "prefix": "New to the firm? ", "linkText": "Inquire about an account", "suffix": "" },
                  "action": "INQUIRE" }
              ],
              "actions": {
                "SUBMIT_FORM": {
                  "type": "API_CALL", "endpoint": "/auth/login", "method": "POST",
                  "onSuccess": { "type": "NAVIGATE", "destination": "dashboard" },
                  "onError": { "type": "SHOW_DIALOG", "params": { "message": "Invalid credentials" } }
                },
                "FORGOT_PASSWORD": { "type": "NAVIGATE", "destination": "forgot_password" },
                "INQUIRE": { "type": "DEEP_LINK", "destination": "architect://onboarding/inquire" }
              }
            }
        """.trimIndent()
    }
}
