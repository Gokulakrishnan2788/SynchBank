package com.architect.banking.engine.sdui.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.architect.banking.engine.sdui.model.ComponentModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the COLUMN SDUI component. */
@Serializable
data class ColumnComponentProps(
    val arrangement: String = "TOP",
    val spacing: Int = 0,
    val scrollable: Boolean = true,
    val padding: PaddingProps? = null,
    val children: List<ComponentModel> = emptyList(),
)

@Serializable
data class PaddingProps(
    val horizontal: Int = 0,
    val vertical: Int = 0,
)

/**
 * Renders a COLUMN SDUI component with vertical child layout.
 */
@Composable
fun ColumnComponent(
    props: JsonObject,
    onAction: (String) -> Unit,
    childRenderer: @Composable (ComponentModel, (String) -> Unit) -> Unit,
) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<ColumnComponentProps>(props)
    }.getOrDefault(ColumnComponentProps())

    val scrollModifier = if (decoded.scrollable) {
        Modifier.verticalScroll(rememberScrollState())
    } else {
        Modifier
    }

    val paddingModifier = Modifier.padding(
        horizontal = decoded.padding?.horizontal?.dp ?: 0.dp,
        vertical = decoded.padding?.vertical?.dp ?: 0.dp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .then(scrollModifier)
            .then(paddingModifier),
        verticalArrangement = decoded.arrangement.toVerticalArrangement(decoded.spacing),
    ) {
        decoded.children.forEach { child ->
            if (child.visible) {
                childRenderer(child, onAction)
            }
        }
    }
}

private fun String.toVerticalArrangement(spacing: Int): Arrangement.Vertical {
    val spaced = Arrangement.spacedBy(spacing.dp)

    return when (this.uppercase()) {
        "CENTER" -> Arrangement.Center
        "BOTTOM" -> Arrangement.Bottom
        else -> spaced // default TOP with spacing
    }
}