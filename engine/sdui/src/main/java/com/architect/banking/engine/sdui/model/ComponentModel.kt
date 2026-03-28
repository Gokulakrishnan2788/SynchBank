package com.architect.banking.engine.sdui.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * All SDUI component type identifiers used in the "type" field of JSON components.
 */
@Serializable
enum class SduiComponentType {
    TEXT,
    TEXT_FIELD,
    BUTTON,
    ICON_BUTTON,
    CARD,
    IMAGE,
    SPACER,
    DIVIDER,
    ROW,
    COLUMN,
    LINK_TEXT,
    BIOMETRIC_ROW,
    HEADER_BAR,
    LINE_CHART,
    ICON,
    ACTIVITY_ITEM,
    SOURCE_ACCOUNT_SELECTOR,
    AMOUNT_INPUT_CARD,
    SECTION_HEADER_ROW,
    BENEFICIARY_GRID,
    TRANSFER_LIMIT_BANNER,
    TRANSFER_PROGRESS,
    ADD_BENEFICIARY_FORM,
    VERIFICATION_CARD,
    UNKNOWN,
}

/**
 * Serializable component model parsed from the SDUI screen JSON.
 *
 * @property id Unique identifier within the screen — used for form field mapping.
 * @property type Component type resolved to [SduiComponentType].
 * @property visible When false, the component is not rendered.
 * @property props Raw JSON object; deserialized per-component inside the registry.
 * @property action Optional action ID referencing the screen's "actions" map.
 */
@Serializable
//data class ComponentModel(
//    val id: String,
//    val type: SduiComponentType = SduiComponentType.UNKNOWN,
//    val visible: Boolean = true,
//    val props: JsonObject = JsonObject(emptyMap()),
//    val action: String? = null,
//)
data class ComponentModel(
    val id: String,
    val type: SduiComponentType = SduiComponentType.UNKNOWN,
    val visible: Boolean = true,
    val props: JsonObject = JsonObject(emptyMap()),
    val children: List<ComponentModel> = emptyList(),
    val action: String? = null,
)

/**
 * Layout type for the top-level screen container.
 */
@Serializable
enum class SduiLayoutType { SCROLL, COLUMN, LAZY_COLUMN }

/**
 * Padding spec used in the screen layout.
 */
@Serializable
data class LayoutPadding(
    val horizontal: Int = 0,
    val vertical: Int = 0,
)

/**
 * Top-level layout descriptor for a SDUI screen.
 */
@Serializable
data class LayoutModel(
    val type: SduiLayoutType = SduiLayoutType.COLUMN,
    val padding: LayoutPadding = LayoutPadding(),
)
