package com.architect.banking.engine.sdui.model

import kotlinx.serialization.Serializable

/**
 * Screen-level metadata parsed from the SDUI JSON response.
 *
 * @property title Display title for the screen / app bar.
 * @property analyticsTag Tag sent with analytics events for this screen.
 */
@Serializable
data class ScreenMetadata(
    val title: String = "",
    val analyticsTag: String = "",
)

/**
 * Root SDUI screen model. Represents one complete screen definition.
 *
 * @property screenId Unique identifier for this screen (e.g. "login").
 * @property version Schema version of this screen definition.
 * @property metadata Display and analytics metadata.
 * @property layout Top-level layout configuration.
 * @property components Ordered list of components to render.
 * @property actions Named action map — keys referenced by [ComponentModel.action].
 */
@Serializable
data class ScreenModel(
    val screenId: String,
    val version: String = "1.0",
    val metadata: ScreenMetadata = ScreenMetadata(),
    val layout: LayoutModel = LayoutModel(),
    val components: List<ComponentModel> = emptyList(),
    val actions: Map<String, ActionModel> = emptyMap(),
)
