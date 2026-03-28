package com.architect.banking.engine.sdui.model

import kotlinx.serialization.Serializable

/**
 * Supported action types that can be triggered from a SDUI component.
 */
@Serializable
enum class SduiActionType {
    NAVIGATE,
    API_CALL,
    DEEP_LINK,
    SHOW_DIALOG,
    DISMISS,
    SUBMIT_FORM,
    BIOMETRIC_AUTH,
    LOCAL_STATE,
}

/**
 * Supported HTTP methods for [SduiActionType.API_CALL] actions.
 */
@Serializable
enum class SduiHttpMethod { GET, POST, PUT, DELETE }

/**
 * Serializable action model from the SDUI JSON contract.
 * Each screen's "actions" map values deserialize to this type.
 *
 * @property type The category of action to execute.
 * @property destination Route name for [SduiActionType.NAVIGATE].
 * @property endpoint API path for [SduiActionType.API_CALL].
 * @property method HTTP method for [SduiActionType.API_CALL].
 * @property params Extra key-value data for this action.
 * @property onSuccess Chained action to execute on API success.
 * @property onError Chained action to execute on API failure.
 * @property deepLink Deep link URI for [SduiActionType.DEEP_LINK].
 */
@Serializable
data class ActionModel(
    val type: SduiActionType,
    val destination: String? = null,
    val endpoint: String? = null,
    val method: SduiHttpMethod? = null,
    val params: Map<String, String> = emptyMap(),
    val onSuccess: ActionModel? = null,
    val onError: ActionModel? = null,
    val deepLink: String? = null,
)
