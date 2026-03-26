package com.architect.banking.feature.login

import com.architect.banking.core.domain.Result
import com.architect.banking.engine.sdui.model.SduiActionType
import com.architect.banking.engine.sdui.parser.SDUIParser
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginScreenModelParserTest {

    private lateinit var parser: SDUIParser

    @Before
    fun setUp() {
        parser = SDUIParser(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
                encodeDefaults = true
            },
        )
    }

    @Test
    fun `given login screen json, when parsed, then correct screenId`() {
        val result = parser.parse(LOGIN_SCREEN_JSON)

        assertTrue(result is Result.Success)
        assertEquals("login", (result as Result.Success).data.screenId)
    }

    @Test
    fun `given login screen json, when parsed, then all required components present`() {
        val result = parser.parse(LOGIN_SCREEN_JSON) as Result.Success
        val componentIds = result.data.components.map { it.id }

        assertTrue("username_field missing", "username_field" in componentIds)
        assertTrue("password_field missing", "password_field" in componentIds)
        assertTrue("login_btn missing", "login_btn" in componentIds)
        assertTrue("biometric missing", "biometric" in componentIds)
        assertTrue("register_link missing", "register_link" in componentIds)
    }

    @Test
    fun `given login screen json, when parsed, then SUBMIT_FORM action exists with correct type`() {
        val result = parser.parse(LOGIN_SCREEN_JSON) as Result.Success
        val submitAction = result.data.actions["SUBMIT_FORM"]

        assertNotNull("SUBMIT_FORM action missing", submitAction)
        assertEquals(SduiActionType.API_CALL, submitAction!!.type)
        assertNotNull("onSuccess action missing", submitAction.onSuccess)
        assertEquals(SduiActionType.NAVIGATE, submitAction.onSuccess!!.type)
        assertEquals("dashboard", submitAction.onSuccess.destination)
    }

    @Test
    fun `given malformed json, when parsed, then returns error result`() {
        val result = parser.parse("{ this is not valid json }")

        assertTrue(result is Result.Error)
        assertEquals("PARSE_ERROR", (result as Result.Error).code)
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
                { "id": "subtitle", "type": "TEXT", "visible": true,
                  "props": { "text": "Access your institutional wealth management dashboard.", "style": "BODY" } },
                { "id": "spacer_2", "type": "SPACER", "visible": true, "props": { "height": 32 } },
                { "id": "username_field", "type": "TEXT_FIELD", "visible": true,
                  "props": { "label": "USERNAME", "hint": "Institutional ID or Email",
                    "inputType": "EMAIL", "required": true } },
                { "id": "password_field", "type": "TEXT_FIELD", "visible": true,
                  "props": { "label": "PASSWORD", "hint": "", "inputType": "PASSWORD", "required": true },
                  "action": "FORGOT_PASSWORD" },
                { "id": "spacer_3", "type": "SPACER", "visible": true, "props": { "height": 24 } },
                { "id": "login_btn", "type": "BUTTON", "visible": true,
                  "props": { "label": "Login", "style": "PRIMARY", "loading": false },
                  "action": "SUBMIT_FORM" },
                { "id": "spacer_4", "type": "SPACER", "visible": true, "props": { "height": 16 } },
                { "id": "biometric", "type": "BIOMETRIC_ROW", "visible": true,
                  "props": { "options": ["FINGERPRINT", "FACE_ID"], "label": "AUTHENTICATION" } },
                { "id": "spacer_5", "type": "SPACER", "visible": true, "props": { "height": 24 } },
                { "id": "register_link", "type": "LINK_TEXT", "visible": true,
                  "props": { "prefix": "New to the firm? ", "linkText": "Inquire about an account", "suffix": "" },
                  "action": "INQUIRE" }
              ],
              "actions": {
                "SUBMIT_FORM": {
                  "type": "API_CALL", "endpoint": "/auth/login", "method": "POST",
                  "onSuccess": { "type": "NAVIGATE", "destination": "dashboard" },
                  "onError": { "type": "SHOW_DIALOG", "params": { "message": "Invalid credentials. Please try again." } }
                },
                "FORGOT_PASSWORD": { "type": "NAVIGATE", "destination": "forgot_password" },
                "INQUIRE": { "type": "DEEP_LINK", "destination": "architect://onboarding/inquire" }
              }
            }
        """.trimIndent()
    }
}
